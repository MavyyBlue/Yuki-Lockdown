import hashlib,stat,tempfile,unittest,zipfile
from pathlib import Path
from bootstrap_extract import install,ARCHIVE,REQUIRED,MARKER
class BootstrapTest(unittest.TestCase):
 def setUp(self):self.tmp=tempfile.TemporaryDirectory();self.root=Path(self.tmp.name)
 def tearDown(self):self.tmp.cleanup()
 def archive(self,extra=()):
  with zipfile.ZipFile(self.root/ARCHIVE,'w') as z:
   for n in sorted(REQUIRED):z.writestr(n,'test')
   for n,data in extra:z.writestr(n,data)
  return hashlib.sha256((self.root/ARCHIVE).read_bytes()).hexdigest()
 def rejects(self,extra):
  h=self.archive(extra)
  with self.assertRaises(ValueError):install(self.root,h)
  self.assertFalse((self.root/'app').exists())
 def testRerun(self):
  h=self.archive();install(self.root,h);(self.root/'build.gradle').write_text('owner changes');self.assertEqual([],install(self.root,h));self.assertEqual('owner changes',(self.root/'build.gradle').read_text())
 def testWorkflow(self):self.rejects([('.github/workflows/bad.yml','x')])
 def testTraversal(self):self.rejects([('app/../../bad','x')])
 def testDuplicate(self):self.rejects([('build.gradle','x')])
 def testRecursive(self):self.rejects([('tools/source.zip','x')])
 def testBadHash(self):
  self.archive()
  with self.assertRaises(ValueError):install(self.root,'bad')
 def testMissing(self):
  with self.assertRaises(ValueError):install(self.root,'bad')
 def testExisting(self):
  h=self.archive();(self.root/'app').mkdir()
  with self.assertRaises(ValueError):install(self.root,h)
 def testSymlink(self):
  i=zipfile.ZipInfo('tools/link');i.create_system=3;i.external_attr=(stat.S_IFLNK|0o777)<<16;self.rejects([(i,'/tmp')])
 def testMarker(self):
  h=self.archive();install(self.root,h)
  with self.assertRaises(ValueError):install(self.root,'other')
if __name__=='__main__':unittest.main()
