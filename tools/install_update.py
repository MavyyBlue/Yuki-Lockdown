"""Hash-guarded full-source install/upgrade. Never overwrites independent source edits."""
import hashlib,json,os,re,shutil,stat,subprocess,tempfile,zipfile
from pathlib import Path,PurePosixPath
ARCHIVE='yuki-lockdown-source-v1.1.0.zip'
MARKER='.yuki-bootstrap.json'
ROOTS={'app','assets','tools','PROJECT_HANDOFF','gradle','gradlew','gradlew.bat','settings.gradle','build.gradle','gradle.properties','README.md','.gitignore'}
REQUIRED={'app/build.gradle','app/src/main/AndroidManifest.xml','app/src/main/java/com/mavyy/yukilockdown/Intervention.java','gradlew','settings.gradle','PROJECT_HANDOFF/CURRENT_STATE.md','tools/update_baseline.json'}
def blob(data):return hashlib.sha1(b'blob '+str(len(data)).encode()+b'\0'+data).hexdigest()
def install(root,expected):
 root=Path(root).resolve();marker=root/MARKER
 if marker.is_symlink():raise ValueError('Refusing symbolic marker')
 if marker.is_file() and json.loads(marker.read_text()).get('archive_sha256')==expected:
  if not all((root/n).is_file() for n in REQUIRED):raise ValueError('Expanded source incomplete; restore through git')
  print('Already updated: expanded source is authoritative; ZIP ignored.');return []
 archive=root/ARCHIVE
 if not archive.is_file() or archive.is_symlink():raise ValueError('Upload '+ARCHIVE+' to repository root first')
 if hashlib.sha256(archive.read_bytes()).hexdigest()!=expected:raise ValueError('ZIP checksum mismatch: upload the matching ZIP and workflow')
 with zipfile.ZipFile(archive) as z:
  infos=z.infolist();names=set();folded=set()
  if len(infos)>5000 or sum(i.file_size for i in infos)>100*1024*1024:raise ValueError('Archive too large')
  for i in infos:
   n=i.filename;p=PurePosixPath(n)
   if not n or p.is_absolute() or str(p)!=n or '..' in p.parts or not re.fullmatch(r'[A-Za-z0-9_./-]+',n):raise ValueError('Unsafe path: '+n)
   if p.parts[0] not in ROOTS or any(x in {'.git','.github'} for x in p.parts) or n.lower().endswith('.zip'):raise ValueError('Forbidden path: '+n)
   if i.is_dir() or stat.S_IFMT(i.external_attr>>16) not in (0,stat.S_IFREG):raise ValueError('Only regular files are accepted')
   if n.casefold() in folded:raise ValueError('Duplicate path: '+n)
   names.add(n);folded.add(n.casefold())
  if not REQUIRED<=names or z.testzip() is not None:raise ValueError('Incomplete or malformed source archive')
  baseline=json.loads(z.read('tools/update_baseline.json'))['files'];upgrading=(root/'app').exists()
  if upgrading and not marker.is_file():raise ValueError('Existing tree lacks bootstrap marker; refusing unknown project')
  if not upgrading:
   for e in root.iterdir():
    if e.name not in {'.git','.github',ARCHIVE,'README.md'}:raise ValueError('Unknown initial repository entry: '+e.name)
  # Validate ALL destinations before writing anything. Source edits cause a clear failure.
  for n in names:
   dest=root/n
   if any(p.is_symlink() for p in [dest,*dest.parents]):raise ValueError('Symlink destination: '+n)
   if dest.exists() and not dest.is_file():raise ValueError('Non-file destination: '+n)
   if upgrading:
    if dest.exists():
     actual=blob(dest.read_bytes());target=blob(z.read(n))
     if actual not in {baseline.get(n),target}:raise ValueError('Owner/source edits detected; refusing overwrite: '+n)
    elif n in baseline:raise ValueError('Baseline source missing: '+n)
  with tempfile.TemporaryDirectory() as td:
   for n in names:
    f=Path(td)/n;f.parent.mkdir(parents=True,exist_ok=True);f.write_bytes(z.read(n))
   for n in sorted(names):
    f=root/n;f.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(Path(td)/n,f)
 (root/'gradlew').chmod(0o755)
 marker.write_text(json.dumps({'version':2,'archive':ARCHIVE,'archive_sha256':expected},indent=2)+'\n')
 return sorted(names)+[MARKER]
if __name__=='__main__':
 files=install(Path.cwd(),os.environ['SOURCE_SHA256'])
 if files:subprocess.run(['git','add','--force','--',*files],check=True)
