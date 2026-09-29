package com.mavyy.yukilockdown;

import android.content.Context;
import android.graphics.Color;
import android.view.*;
import android.view.animation.DecelerateInterpolator;
import android.widget.*;

/** One presentation for real interventions and an explicitly labelled, non-enforcing preview. */
public final class Intervention {
 public static View create(Context c,String mode,String reason,String dialogue,int reaction,Runnable home,Runnable talk,Runnable owner,boolean preview){
  FrameLayout root=new FrameLayout(c);root.setBackgroundColor(0xb3090d16);root.setClickable(true);root.setFocusableInTouchMode(true);
  root.setOnApplyWindowInsetsListener((v,insets)->{v.setPadding(insets.getSystemWindowInsetLeft(),insets.getSystemWindowInsetTop(),insets.getSystemWindowInsetRight(),insets.getSystemWindowInsetBottom());return insets.consumeSystemWindowInsets();});
  // A full-size touchable window protects the transparent regions too. No touch-through flags.
  root.setOnKeyListener((v,key,event)->{if(key==KeyEvent.KEYCODE_BACK){if(event.getAction()==KeyEvent.ACTION_UP)home.run();return true;}return false;});
  ScrollView scroll=new ScrollView(c);scroll.setFillViewport(true);scroll.setVerticalScrollBarEnabled(false);scroll.setClipChildren(false);root.addView(scroll,new FrameLayout.LayoutParams(-1,-1));
  LinearLayout stage=Ui.column(c);stage.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);stage.setPadding(Ui.dp(c,20),Ui.dp(c,20),Ui.dp(c,20),Ui.dp(c,24));scroll.addView(stage,new ScrollView.LayoutParams(-1,-2));
  LinearLayout content=Ui.column(c);content.setClipChildren(false);stage.addView(content,new LinearLayout.LayoutParams(Math.min(c.getResources().getDisplayMetrics().widthPixels-Ui.dp(c,40),Ui.dp(c,480)),-2));
  TextView tag=Ui.text(content,preview?"PREVIEW · NO RULE TRIGGERED":"YUKI’S HERE",11,Ui.BLUE);tag.setLetterSpacing(.14f);
  FrameLayout artStage=new FrameLayout(c);artStage.setClipChildren(false);ImageView art=Ui.characterView(c,mode);
  int availableHeight=c.getResources().getDisplayMetrics().heightPixels;int artHeight=Math.max(120,Math.min(290,Math.round(availableHeight/c.getResources().getDisplayMetrics().density*.32f)));if(c.getResources().getConfiguration().fontScale>=1.5f)artHeight=120;
  FrameLayout.LayoutParams ap=new FrameLayout.LayoutParams(Ui.dp(c,artHeight+40),Ui.dp(c,artHeight),Gravity.END|Gravity.BOTTOM);artStage.addView(art,ap);LinearLayout.LayoutParams area=new LinearLayout.LayoutParams(-1,Ui.dp(c,artHeight));area.bottomMargin=-Ui.dp(c,22);content.addView(artStage,area);artStage.setTranslationZ(Ui.dp(c,4));
  LinearLayout speech=Ui.column(c);speech.setPadding(Ui.dp(c,22),Ui.dp(c,28),Ui.dp(c,22),Ui.dp(c,20));speech.setBackground(Ui.shape(c,0xffeaf5ff,26));speech.setElevation(Ui.dp(c,6));content.addView(speech,new LinearLayout.LayoutParams(-1,-2));
  Ui.text(speech,reason,13,0xff44617b);TextView words=Ui.text(speech,dialogue,23,0xff142436);words.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);Ui.text(speech,preview?"This is how I’ll appear when a rule is active.":"Your boundary · reminder "+reaction+" of 4",12,0xff526b83);
  LinearLayout actions=Ui.column(c);LinearLayout.LayoutParams actionLp=new LinearLayout.LayoutParams(-1,-2);actionLp.topMargin=Ui.dp(c,12);content.addView(actions,actionLp);Ui.button(actions,preview?"Close preview":"Return Home",home);Ui.secondary(actions,"Talk to Yuki",talk);Button edit=Ui.secondary(actions,preview?"Back to settings":"Emergency bypass / Edit rules",owner);edit.setTextSize(13);
  root.post(()->{root.requestFocus();root.requestApplyInsets();if(Ui.motion(c)){art.setAlpha(0);art.setTranslationX(Ui.dp(c,90));art.setRotation(6);art.animate().alpha(1).translationX(0).rotation(0).setDuration(420).setInterpolator(new DecelerateInterpolator()).start();speech.setAlpha(0);speech.setTranslationY(Ui.dp(c,18));speech.animate().alpha(1).translationY(0).setStartDelay(100).setDuration(280).start();}});
  return root;
 }
}
