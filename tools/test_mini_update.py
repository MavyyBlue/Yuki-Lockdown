import importlib.util,json,tempfile,unittest,zipfile
from pathlib import Path
spec=importlib.util.spec_from_file_location('update',Path(__file__).with_name('install_mini_update.py'));update=importlib.util.module_from_spec(spec);spec.loader.exec_module(update)
class UpdateTest(unittest.TestCase):
 def fixture(self,root,extra=None):
  baseline={n:b'old' for n in update.REQUIRED if n!='tools/mini_baseline.json'}
  for n,data in baseline.items():p=root/n;p.parent.mkdir(parents=True,exist_ok=True);p.write_bytes(data)
  (root/update.MARKER).write_text('{}');workflow=root/update.WORKFLOW;workflow.parent.mkdir(parents=True);workflow.write_text('owner workflow')
  files={n:b'new' for n in update.REQUIRED};files['tools/mini_baseline.json']=json.dumps({'files':{n:update.blob(d) for n,d in baseline.items()}}).encode();files[update.WORKFLOW]=b'bundled workflow';files.update(extra or {})
  with zipfile.ZipFile(root/update.ARCHIVE,'w') as z:
   for n,d in files.items():z.writestr(n,d)
  with zipfile.ZipFile(root/update.ARCHIVE) as z:return update.payload_hash(z)
 def test_upgrade_workflow_preserved_and_rerun_preserves_edit(self):
  with tempfile.TemporaryDirectory() as td:
   root=Path(td);digest=self.fixture(root);changed=update.install(root,digest);self.assertNotIn(update.WORKFLOW,changed);self.assertEqual((root/update.WORKFLOW).read_text(),'owner workflow');(root/'app/build.gradle').write_text('later edit');self.assertEqual(update.install(root,digest),[]);self.assertEqual((root/'app/build.gradle').read_text(),'later edit')
 def test_owner_edit_refused_before_any_write(self):
  with tempfile.TemporaryDirectory() as td:
   root=Path(td);digest=self.fixture(root);(root/'app/build.gradle').write_text('owner edit')
   with self.assertRaisesRegex(ValueError,'Owner/source'):update.install(root,digest)
   self.assertEqual((root/'gradlew').read_bytes(),b'old')
 def test_bad_checksum_refused(self):
  with tempfile.TemporaryDirectory() as td:
   root=Path(td);self.fixture(root)
   with self.assertRaisesRegex(ValueError,'checksum'):update.install(root,'bad')
 def test_traversal_and_other_workflows_refused(self):
  for name in ['../escape','.github/workflows/other.yml','tools/nested.zip']:
   with tempfile.TemporaryDirectory() as td:
    root=Path(td);digest=self.fixture(root,{name:b'bad'})
    with self.assertRaises(ValueError):update.install(root,digest)
if __name__=='__main__':unittest.main()
