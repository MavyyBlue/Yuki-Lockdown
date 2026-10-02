"""Guarded companion Home update. The bundled workflow must be uploaded manually."""
import hashlib,json,os,re,shutil,stat,subprocess,tempfile,zipfile
from pathlib import Path,PurePosixPath
ARCHIVE='yuki-lockdown-source-v1.5.0.zip'
MARKER='.yuki-bootstrap.json'
WORKFLOW='.github/workflows/bootstrap.yml'
ROOTS={'app','assets','tools','PROJECT_HANDOFF','gradle','gradlew','gradlew.bat','settings.gradle','build.gradle','gradle.properties','README.md','.gitignore'}
REQUIRED={'app/build.gradle','app/src/main/AndroidManifest.xml','app/src/main/java/com/mavyy/yukilockdown/MiniYukiView.java','app/src/main/java/com/mavyy/yukilockdown/CompanionSize.java','app/src/main/res/drawable-nodpi/yuki_adult_kiss_03.png','app/src/main/res/drawable-nodpi/yuki_adult_carry_00.png','app/src/main/java/com/mavyy/yukilockdown/IdleYukiDrawable.java','app/src/main/java/com/mavyy/yukilockdown/CompanionWalk.java','gradlew','settings.gradle','tools/home_baseline.json','app/src/main/java/com/mavyy/yukilockdown/HomeYuki.java'}
def blob(data):return hashlib.sha1(b'blob '+str(len(data)).encode()+b'\0'+data).hexdigest()
def payload_hash(z):
 h=hashlib.sha256()
 for n in sorted(i.filename for i in z.infolist() if i.filename!=WORKFLOW):
  h.update(n.encode()+b'\0'+hashlib.sha256(z.read(n)).digest())
 return h.hexdigest()
def install(root,expected):
 root=Path(root).resolve();marker=root/MARKER
 if marker.is_symlink():raise ValueError('Symbolic marker refused')
 if marker.is_file() and json.loads(marker.read_text()).get('payload_sha256')==expected:
  if not all((root/n).is_file() for n in REQUIRED):raise ValueError('Expanded source incomplete')
  print('Yuki Home update installed: building current source; ZIP ignored.');return []
 archive=root/ARCHIVE
 if not archive.is_file() or archive.is_symlink():raise ValueError('Upload '+ARCHIVE+' to repository root')
 with zipfile.ZipFile(archive) as z:
  infos=z.infolist();names=set();folded=set()
  if len(infos)>5000 or sum(i.file_size for i in infos)>100*1024*1024:raise ValueError('Archive too large')
  for i in infos:
   n=i.filename;p=PurePosixPath(n)
   if not n or p.is_absolute() or str(p)!=n or '..' in p.parts or not re.fullmatch(r'[A-Za-z0-9_./-]+',n):raise ValueError('Unsafe path: '+n)
   if n!=WORKFLOW and (p.parts[0] not in ROOTS or any(x in {'.git','.github'} for x in p.parts) or n.lower().endswith('.zip')):raise ValueError('Forbidden path: '+n)
   if i.is_dir() or stat.S_IFMT(i.external_attr>>16) not in (0,stat.S_IFREG):raise ValueError('Only regular files accepted')
   if n.casefold() in folded:raise ValueError('Duplicate path: '+n)
   folded.add(n.casefold())
   if n!=WORKFLOW:names.add(n)
  if not REQUIRED<=names or z.testzip() is not None:raise ValueError('Incomplete source archive')
  if payload_hash(z)!=expected:raise ValueError('Payload checksum mismatch')
  baseline=json.loads(z.read('tools/home_baseline.json'))['files']
  if not marker.is_file() or not (root/'app').is_dir():raise ValueError('Requires existing bootstrapped Yuki Lockdown app')
  for n in names:
   dest=root/n
   if any(p.is_symlink() for p in [dest,*dest.parents]):raise ValueError('Symlink destination: '+n)
   if dest.exists() and not dest.is_file():raise ValueError('Non-file destination: '+n)
   if dest.exists():
    if blob(dest.read_bytes()) not in {baseline.get(n),blob(z.read(n))}:raise ValueError('Owner/source edits detected: '+n)
   elif n in baseline:raise ValueError('Baseline file missing: '+n)
  with tempfile.TemporaryDirectory() as td:
   for n in sorted(names):
    staged=Path(td)/n;staged.parent.mkdir(parents=True,exist_ok=True);staged.write_bytes(z.read(n))
   for n in sorted(names):
    dest=root/n;dest.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(Path(td)/n,dest)
 (root/'gradlew').chmod(0o755)
 marker.write_text(json.dumps({'version':7,'archive':ARCHIVE,'payload_sha256':expected},indent=2)+'\n')
 return sorted(names)+[MARKER]
if __name__=='__main__':
 files=install(Path.cwd(),os.environ['SOURCE_PAYLOAD_SHA256'])
 if files:subprocess.run(['git','add','--force','--',*files],check=True)
