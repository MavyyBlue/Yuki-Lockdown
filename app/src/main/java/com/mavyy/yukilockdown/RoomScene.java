package com.mavyy.yukilockdown;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
import java.util.function.Consumer;

/** One persistent room, one dialogue surface, one bottom panel; no launch controls. */
public final class RoomScene extends FrameLayout {
 private final FrameLayout safe;private final MiniYukiView yuki;private final RoomDialogue flow;
 private final LinearLayout dialogue;private final ScrollView dialogueWindow;private final Consumer<String> choose;private RoomPanel panel;
 public RoomScene(Context c,RoomDialogue flow,Consumer<String> choose){
  super(c);this.flow=flow;this.choose=choose;setBackgroundColor(Ui.BG);
  ImageView room=new ImageView(c);room.setImageResource(R.drawable.yuki_room);room.setScaleType(ImageView.ScaleType.CENTER_CROP);room.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);addView(room,new FrameLayout.LayoutParams(-1,-1));
  View shade=new View(c);shade.setBackground(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,new int[]{0x00101b32,0x0d091525,0x77081221}));shade.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);addView(shade,new FrameLayout.LayoutParams(-1,-1));
  safe=new FrameLayout(c);addView(safe,new FrameLayout.LayoutParams(-1,-1));
  yuki=new MiniYukiView(c);yuki.setTag("room_yuki");yuki.setContentDescription("Yuki, tap to talk");yuki.setFocusable(true);yuki.setOnClickListener(v->{if(panel==null){flow.tap();updateDialogue();}});safe.addView(yuki,new FrameLayout.LayoutParams(1,1,Gravity.CENTER_HORIZONTAL|Gravity.TOP));
  dialogue=Ui.column(c);dialogue.setTag("room_dialogue");dialogue.setPadding(Ui.dp(c,18),Ui.dp(c,12),Ui.dp(c,18),Ui.dp(c,12));dialogueWindow=new ScrollView(c){protected void onMeasure(int width,int height){int available=safe.getHeight()-safe.getPaddingTop()-safe.getPaddingBottom();int limit=Math.max(1,Math.round(available*.64f));super.onMeasure(width,available>0?MeasureSpec.makeMeasureSpec(limit,MeasureSpec.AT_MOST):height);}};dialogueWindow.setBackground(Ui.shape(c,0xf01a2638,22));dialogueWindow.setElevation(Ui.dp(c,8));dialogueWindow.addView(dialogue);safe.addView(dialogueWindow,new FrameLayout.LayoutParams(-1,-2,Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL));
  safe.addOnLayoutChangeListener((v,l,t,r,b,ol,ot,or,ob)->resize());
  setOnApplyWindowInsetsListener((v,insets)->{safe.setPadding(insets.getSystemWindowInsetLeft(),insets.getSystemWindowInsetTop(),insets.getSystemWindowInsetRight(),insets.getSystemWindowInsetBottom());safe.post(this::resize);return insets.consumeSystemWindowInsets();});updateDialogue();
 }
 private void resize(){
  int w=safe.getWidth()-safe.getPaddingLeft()-safe.getPaddingRight(),h=safe.getHeight()-safe.getPaddingTop()-safe.getPaddingBottom();if(w<=0||h<=0)return;Context c=getContext();
  int charHeight=Math.min(Math.round(h*.88f),Math.round(w*1.5f));FrameLayout.LayoutParams yp=(FrameLayout.LayoutParams)yuki.getLayoutParams();yp.height=charHeight;yp.width=Math.round(charHeight*418f/627);yp.topMargin=Math.round(h*.06f);yuki.setLayoutParams(yp);
  int width=Math.max(1,Math.min(w-Ui.dp(c,24),Ui.dp(c,600)));
  FrameLayout.LayoutParams dp=(FrameLayout.LayoutParams)dialogueWindow.getLayoutParams();dp.width=width;dp.bottomMargin=Ui.dp(c,12);dialogueWindow.setLayoutParams(dp);
  if(panel!=null){FrameLayout.LayoutParams pp=(FrameLayout.LayoutParams)panel.getLayoutParams();pp.width=width;pp.height=Math.max(1,Math.min(h-Ui.dp(c,16),Math.round(h*.66f)));pp.bottomMargin=Ui.dp(c,8);panel.setLayoutParams(pp);}
 }
 public RoomPanel createPanel(String title,View content,Runnable commit,boolean editable){return new RoomPanel(this,title,content,commit,editable);}
 void present(RoomPanel next){if(panel!=null)safe.removeView(panel);panel=next;flow.panel();dialogueWindow.setVisibility(GONE);yuki.setClickable(false);yuki.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);safe.addView(panel,new FrameLayout.LayoutParams(1,1,Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL));resize();panel.setFocusableInTouchMode(true);panel.requestFocus();}
 void finished(RoomPanel closing,String line){if(panel!=closing)return;safe.removeView(panel);panel=null;hideKeyboard();yuki.setClickable(true);yuki.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);flow.returnToDialogue(line);updateDialogue();}
 public RoomPanel currentPanel(){return panel;}
 public void reply(String line){if(panel!=null)safe.removeView(panel);panel=null;yuki.setClickable(true);yuki.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);flow.returnToDialogue(line);updateDialogue();}
 public void updateDialogue(){
  dialogue.removeAllViews();RoomDialogue.Step step=flow.step();if(step==RoomDialogue.Step.QUIET||step==RoomDialogue.Step.PANEL){dialogueWindow.setVisibility(GONE);return;}dialogueWindow.setVisibility(VISIBLE);
  Ui.text(dialogue,"Yuki",14,Ui.BLUE);TextView line=Ui.text(dialogue,flow.text(),18,Ui.WHITE);line.setAccessibilityLiveRegion(ACCESSIBILITY_LIVE_REGION_POLITE);
  if(step==RoomDialogue.Step.CHOICES){
   LinearLayout options=Ui.column(getContext());for(String name:new String[]{"Talk","Protected Apps","Sites","Plans"})Ui.secondary(options,name,()->choose.accept(name));
   dialogue.addView(options);
  }else{dialogue.setOnClickListener(v->{flow.tap();updateDialogue();});line.setOnClickListener(v->{flow.tap();updateDialogue();});Ui.text(dialogue,"Tap to continue  ›",12,Ui.MUTED);}
  dialogue.setClickable(step!=RoomDialogue.Step.CHOICES);resize();
 }
 private void hideKeyboard(){android.view.inputmethod.InputMethodManager input=(android.view.inputmethod.InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);if(input!=null)input.hideSoftInputFromWindow(getWindowToken(),0);}
 public boolean back(){if(panel!=null){panel.saveAndClose();return true;}if(flow.step()!=RoomDialogue.Step.QUIET){flow.quiet();updateDialogue();return true;}return false;}
 public void pause(){yuki.pausePlayback();}
 public void resume(){yuki.restart("idle");}
}
