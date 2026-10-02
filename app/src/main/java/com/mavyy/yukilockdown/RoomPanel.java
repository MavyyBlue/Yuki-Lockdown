package com.mavyy.yukilockdown;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
/** One embedded, scrollable bottom panel. Header/exit remain outside the scroll. */
public final class RoomPanel extends LinearLayout {
 private final RoomScene scene;private final Runnable commit;private final boolean editable;
 private final ScrollView scroll;private final TextView error;private final Button exit;
 private View content;
 RoomPanel(RoomScene scene,String title,View content,Runnable commit,boolean editable){
  super(scene.getContext());this.scene=scene;this.commit=commit;this.editable=editable;Context c=getContext();setOrientation(VERTICAL);setTag("room_panel");setPadding(Ui.dp(c,8),Ui.dp(c,6),Ui.dp(c,8),Ui.dp(c,8));setBackground(Ui.shape(c,0xf51a2638,24));setElevation(Ui.dp(c,12));setClickable(true);
  LinearLayout header=new LinearLayout(c);header.setGravity(Gravity.CENTER_VERTICAL);header.setPadding(Ui.dp(c,12),0,0,0);
  TextView label=new TextView(c);label.setText(title);label.setTextColor(Ui.WHITE);label.setTextSize(20);header.addView(label,new LinearLayout.LayoutParams(0,-2,1));
  exit=new Button(c);exit.setText("×");exit.setTextSize(25);exit.setAllCaps(false);exit.setTextColor(Ui.WHITE);exit.setPadding(0,0,0,0);exit.setMinWidth(0);exit.setMinimumWidth(0);exit.setBackground(Ui.ripple(c,0xff2a3c54,16));exit.setContentDescription(editable?"Save changes and close panel":"Close panel and return to Yuki");exit.setTag("room_panel_exit");header.addView(exit,new LinearLayout.LayoutParams(Ui.dp(c,48),Ui.dp(c,48)));exit.setOnClickListener(v->saveAndClose());addView(header);
  error=Ui.text(this,"",14,Ui.BLUE);error.setPadding(Ui.dp(c,12),0,Ui.dp(c,12),0);error.setVisibility(GONE);error.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
  scroll=new ScrollView(c);scroll.setClipToPadding(false);addView(scroll,new LinearLayout.LayoutParams(-1,0,1));setContent(content);
 }
 void setContent(View content){scroll.removeAllViews();this.content=content;scroll.addView(content);}
 View content(){return content;}
 public void show(){scene.present(this);}
 public boolean isShowing(){return scene.currentPanel()==this;}
 public boolean saveAndClose(){
  try{if(commit!=null)commit.run();scene.finished(this,editable?"All saved, Darling. What shall we do next? ♡":"I’m here. What shall we do next, Darling?");return true;}
  catch(Exception e){reportError(e);return false;}
 }
 public void reportError(Exception e){error.setText(e.getMessage()==null?"Please check your entries before closing.":e.getMessage());error.setVisibility(VISIBLE);error.announceForAccessibility(error.getText());}
 public void dismiss(){scene.finished(this,"We’re back, Darling. ♡");}
 public void discard(){scene.finished(this,"Unsaved edits discarded. We can come back to that later, Darling.");}
}
