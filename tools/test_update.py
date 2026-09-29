import hashlib,json,stat,tempfile,unittest,zipfile
from pathlib import Path
from install_update import ARCHIVE,MARKER,REQUIRED,install,blob
class UpdateTest(unittest.TestCase):
 def setUp(self):self.tmp=tempfile.TemporaryDirectory();self.root=Path(self.tmp.name)
 def tearDown(self):self.tmp.cleanup()
 def package(self,extra=()):
  baseline={n:blob(b'old') for n in REQUIRED if n!='tools/update_baseline.json'}
  with zipfile.ZipFile(self.root/ARCHIVE,'w') as z:
   for n in REQUIRED:z.writestr(n,json.dumps({'files':baseline}) if n=='tools/update_baseline.json' else 'new')
   for n,data in extra:z.writestr(n,data)
  return hashlib.sha256((self.root/ARCHIVE).read_bytes()).hexdigest()
 def base(self):
  for n in REQUIRED:
   if n=='tools/update_baseline.json':continue
   p=self.root/n;p.parent.mkdir(parents=True,exist_ok=True);p.write_text('old')
  (self.root/MARKER).write_text('{"archive_sha256":"previous"}')
 def testUpgradeAndRerun(self):
  h=self.package();self.base();install(self.root,h);(self.root/'app/build.gradle').write_text('owner edit');self.assertEqual([],install(self.root,h));self.assertEqual('owner edit',(self.root/'app/build.gradle').read_text())
 def testOwnerChangesPreventAnyWrite(self):
  h=self.package();self.base();(self.root/'app/build.gradle').write_text('owner edit')
  with self.assertRaises(ValueError):install(self.root,h)
  self.assertEqual('old',(self.root/'gradlew').read_text())
 def testMissingBaseline(self):
  h=self.package();self.base();(self.root/'gradlew').unlink()
  with self.assertRaises(ValueError):install(self.root,h)
 def testWorkflowPreserved(self):
  h=self.package();self.base();p=self.root/'.github/workflows/bootstrap.yml';p.parent.mkdir(parents=True);p.write_text('owner workflow');install(self.root,h);self.assertEqual('owner workflow',p.read_text())
 def testSymlink(self):
  h=self.package();self.base();(self.root/'gradlew').unlink();(self.root/'gradlew').symlink_to('/tmp/anything')
  with self.assertRaises(ValueError):install(self.root,h)
 def testArchivePaths(self):
  for path in ['../bad','tools/../../bad','.github/workflows/new.yml','tools/nested.zip']:
   h=self.package([(path,'bad')])
   with self.assertRaises(ValueError):install(self.root,h)
 def testFreshInstall(self):h=self.package();install(self.root,h);self.assertTrue((self.root/MARKER).exists())
if __name__=='__main__':unittest.main()
