package com.mavyy.yukilockdown;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
/** Structural form snapshots for rotation; no database or browsing content. */
final class RoomFormState {
 static Bundle capture(View root){Bundle out=new Bundle();visit(root,"root",out,false);return out;}
 static void restore(View root,Bundle state){if(state!=null)visit(root,"root",state,true);}
 private static void visit(View view,String key,Bundle b,boolean restore){
  if(view instanceof EditText edit){if(restore&&b.containsKey(key))edit.setText(b.getString(key));else if(!restore)b.putString(key,edit.getText().toString());}
  else if(view instanceof CompoundButton check){if(restore&&b.containsKey(key))check.setChecked(b.getBoolean(key));else if(!restore)b.putBoolean(key,check.isChecked());}
  else if(view instanceof Spinner spinner){if(restore&&b.containsKey(key))spinner.setSelection(b.getInt(key));else if(!restore)b.putInt(key,spinner.getSelectedItemPosition());}
  else if(view instanceof SeekBar bar){if(restore&&b.containsKey(key))bar.setProgress(b.getInt(key));else if(!restore)b.putInt(key,bar.getProgress());}
  if(view instanceof ViewGroup group)for(int i=0;i<group.getChildCount();i++)visit(group.getChildAt(i),key+"/"+i,b,restore);
 }
}
