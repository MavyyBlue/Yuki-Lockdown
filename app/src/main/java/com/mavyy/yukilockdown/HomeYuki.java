package com.mavyy.yukilockdown;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import java.util.function.Consumer;

/** In-app presence and local dialogue navigation. Owns no rules or preferences. */
public final class HomeYuki extends LinearLayout {
 static final String TAG="home_yuki";
 private final MiniYukiView character;
 private final Consumer<String> navigate;
 private AlertDialog choices;
 public HomeYuki(Context context,Consumer<String> navigate){
  super(context);this.navigate=navigate;setOrientation(VERTICAL);setTag(TAG);
  FrameLayout stage=new FrameLayout(context);
  android.graphics.drawable.GradientDrawable glow=new android.graphics.drawable.GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,new int[]{0xff1d344e,Ui.BG});glow.setCornerRadius(Ui.dp(context,32));stage.setBackground(glow);
  character=new MiniYukiView(context);character.setContentDescription("Yuki, open dialogue choices");character.setFocusable(true);character.setOnClickListener(v->openChoices());
  stage.addView(character,new FrameLayout.LayoutParams(-1,-1));
  addView(stage,new LinearLayout.LayoutParams(-1,Ui.dp(context,360)));
  stage.addOnLayoutChangeListener((v,l,t,r,b,ol,ot,or,ob)->{int height=Math.max(Ui.dp(context,240),Math.min(Ui.dp(context,400),Math.round((r-l)*1.1f)));if(r>l&&stage.getLayoutParams().height!=height){stage.getLayoutParams().height=height;stage.requestLayout();}});
  Ui.text(this,"Tap me. What shall we take care of, Darling?",16,Ui.BLUE).setGravity(Gravity.CENTER);
  Ui.secondary(this,"Talk with Yuki",this::openChoices);
 }
 public void openChoices(){
  if(choices!=null&&choices.isShowing())return;
  Context c=getContext();LinearLayout content=Ui.column(c);content.setPadding(Ui.dp(c,20),Ui.dp(c,8),Ui.dp(c,20),Ui.dp(c,12));
  Ui.text(content,"What shall we take care of?",21,Ui.WHITE);
  Ui.text(content,"Choose where we go next. ♡",15,Ui.MUTED);
  Ui.button(content,"Settings",()->choose("Settings"));
  Ui.secondary(content,"Protected apps",()->choose("Apps"));
  Ui.secondary(content,"Restricted domains",()->choose("Websites"));
  ScrollView scroll=new ScrollView(c);scroll.setFillViewport(false);scroll.addView(content);
  AlertDialog dialog=new AlertDialog.Builder(c).setTitle("Yuki").setView(scroll).setNegativeButton("Close",null).create();choices=dialog;
  dialog.setOnDismissListener(d->{if(choices==dialog)choices=null;});dialog.show();
  Window w=dialog.getWindow();if(w!=null){w.setBackgroundDrawable(Ui.shape(c,Ui.CARD,28));w.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);w.setDimAmount(.45f);w.setGravity(Gravity.CENTER);w.setLayout(Math.min(c.getResources().getDisplayMetrics().widthPixels-Ui.dp(c,24),Ui.dp(c,480)),-2);}
  dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Ui.BLUE);
 }
 private void choose(String page){dismissChoices();navigate.accept(page);}
 public boolean choicesOpen(){return choices!=null&&choices.isShowing();}
 AlertDialog dialogue(){return choices;}
 public void dismissChoices(){if(choices!=null)choices.dismiss();}
 public void pause(){dismissChoices();character.pausePlayback();}
 protected void onDetachedFromWindow(){dismissChoices();super.onDetachedFromWindow();}
}
