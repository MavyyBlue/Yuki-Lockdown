"""Fail-closed initial source installer. Later runs retain authoritative repository files."""
import hashlib,json,os,re,shutil,stat,subprocess,tempfile,zipfile
from pathlib import Path,PurePosixPath
ARCHIVE='yuki-lockdown-source-v1.0.0.zip'
MARKER='.yuki-bootstrap.json'
ROOTS={'app','assets','tools','PROJECT_HANDOFF','gradle','gradlew','gradlew.bat','settings.gradle','build.gradle','gradle.properties','README.md','.gitignore'}
REQUIRED={'settings.gradle','build.gradle','gradlew','app/build.gradle','app/src/main/AndroidManifest.xml','app/src/main/java/com/mavyy/yukilockdown/MainActivity.java','PROJECT_HANDOFF/CURRENT_STATE.md'}
def install(root,expected):
 root=Path(root)
 if (root/MARKER).exists():
  if (root/MARKER).is_symlink():raise ValueError('Marker cannot be a symlink')
  m=json.loads((root/MARKER).read_text())
  if m.get('archive_sha256')!=expected:raise ValueError('Bootstrap marker belongs to another package')
  if not all((root/n).is_file() for n in REQUIRED):raise ValueError('Authoritative source is incomplete; restore from git, not the ZIP')
  print('Already bootstrapped: repository source is authoritative; ZIP ignored.')
  return []
 archive=root/ARCHIVE
 if not archive.is_file() or archive.is_symlink():raise ValueError('Missing regular source archive: '+ARCHIVE)
 if hashlib.sha256(archive.read_bytes()).hexdigest()!=expected:raise ValueError('Source ZIP checksum mismatch')
 for entry in root.iterdir():
  if entry.name not in {'.git','.github',ARCHIVE,'README.md'}:raise ValueError('Refusing to overwrite existing repository tree: '+entry.name)
 with zipfile.ZipFile(archive) as z:
  infos=z.infolist();names=set();folded=set()
  if len(infos)>5000 or sum(i.file_size for i in infos)>100*1024*1024:raise ValueError('Archive exceeds safety limits')
  for i in infos:
   n=i.filename;p=PurePosixPath(n)
   if not n or p.is_absolute() or str(p)!=n or '..' in p.parts or not re.fullmatch(r'[A-Za-z0-9_./-]+',n):raise ValueError('Unsafe archive path: '+n)
   if p.parts[0] not in ROOTS or any(x in {'.git','.github'} for x in p.parts) or n.lower().endswith('.zip'):raise ValueError('Forbidden archive path: '+n)
   if i.is_dir() or stat.S_IFMT(i.external_attr>>16) not in (0,stat.S_IFREG):raise ValueError('Only regular archive files are accepted')
   if n.casefold() in folded:raise ValueError('Duplicate archive path: '+n)
   names.add(n);folded.add(n.casefold())
  if not REQUIRED<=names:raise ValueError('Archive missing required project files')
  if z.testzip() is not None:raise ValueError('Archive CRC failure')
  # All validation precedes writes; staging avoids partial decompression in repository.
  with tempfile.TemporaryDirectory() as stage:
   for i in infos:
    f=Path(stage)/i.filename;f.parent.mkdir(parents=True,exist_ok=True);f.write_bytes(z.read(i))
   for n in sorted(names):
    dest=root/n
    if any(x.is_symlink() for x in [dest,*dest.parents]):raise ValueError('Refusing symlink destination')
    dest.parent.mkdir(parents=True,exist_ok=True);shutil.copyfile(Path(stage)/n,dest)
   (root/'gradlew').chmod(0o755)
 (root/MARKER).write_text(json.dumps({'version':1,'archive':ARCHIVE,'archive_sha256':expected},indent=2)+'\n')
 return sorted(names)+[MARKER]
if __name__=='__main__':
 files=install(Path.cwd(),os.environ['SOURCE_SHA256'])
 if files:subprocess.run(['git','add','--force','--',*files],check=True)
