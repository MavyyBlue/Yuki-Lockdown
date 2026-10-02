package com.mavyy.yukilockdown;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.SystemClock;
import android.view.*;
import android.widget.*;

/** Small accessibility overlay, owned and cleaned up by GuardService. No rule authority. */
public final class PocketYuki {
 private final Context context; private final WindowManager windows; private final SharedPreferences prefs;
 private MiniYukiView character; private View menu; private WindowManager.LayoutParams position;
 private ValueAnimator wander;private long nextWander,boopUntil;private int anchorX; private boolean moving; private int width,height,originX,originY;
 private int sizeDp;private CompanionContext.Mode mode=CompanionContext.Mode.NORMAL;
 private float downX,downY; private boolean dragged; private String reaction="neutral"; private long reactionUntil;
 public PocketYuki(Context c,WindowManager w){context=c;windows=w;prefs=c.getSharedPreferences("pocket_yuki",0);}
 public void sync(boolean eligible,String foregroundPackage){
  CompanionContext.Mode next=CompanionContext.mode(foregroundPackage);if(next!=mode){stopWander();mode=next;}
  if(!eligible||!prefs.getBoolean("enabled",true)){hide();return;}
  Point size=new Point();windows.getDefaultDisplay().getSize(size);
  int requested=CompanionSize.clamp(prefs.getInt("size_dp",CompanionSize.DEFAULT));
  if(width!=size.x||height!=size.y||sizeDp!=requested){hide();width=size.x;height=size.y;sizeDp=requested;}
  if(character==null)show();
  if(character!=null){
   long now=SystemClock.elapsedRealtime();
   if(!Ui.motion(context)||CompanionContext.stationary(mode)){stopWander();}
   if(wander==null)character.play(pose());
   if(!CompanionContext.stationary(mode)&&!moving&&menu==null&&wander==null&&now>=nextWander&&now>=boopUntil&&now>=reactionUntil&&prefs.getBoolean("wander",true)&&Ui.motion(context))wander();
  }
 }
 private String pose(){long now=SystemClock.elapsedRealtime();boolean left=position!=null&&CompanionContext.faceLeft(position.x,position.width,width);return CompanionContext.pose(mode,left,moving,now<boopUntil,now<reactionUntil,reaction.equals("warning"));}
 private WindowManager.LayoutParams params(int w,int h){WindowManager.LayoutParams p=new WindowManager.LayoutParams(w,h,WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,PixelFormat.TRANSLUCENT);p.gravity=Gravity.TOP|Gravity.LEFT;return p;}
 private int margin(){return Ui.dp(context,16);}
 private int maxX(){return Math.max(margin(),width-position.width-margin());}
 private int maxY(){return Math.max(margin(),height-position.height-Ui.dp(context,48));}
 private int clamp(int v,int maximum){return Math.max(margin(),Math.min(maximum,v));}
 private void show(){
  character=new MiniYukiView(context);character.setContentDescription("Pocket Yuki. Tap for companion actions.");character.setFocusable(true);character.setClickable(true);
  int h=CompanionSize.height(Ui.dp(context,sizeDp),width-2*margin(),height-margin()-Ui.dp(context,48));int w=CompanionSize.width(h,Ui.dp(context,48),width-2*margin());position=params(w,h);position.setTitle("Pocket Yuki companion");
  position.x=clamp(margin()+Math.round(prefs.getFloat("x",.85f)*(maxX()-margin())),maxX());position.y=clamp(margin()+Math.round(prefs.getFloat("y",.7f)*(maxY()-margin())),maxY());
  anchorX=position.x;nextWander=SystemClock.elapsedRealtime()+CompanionWalk.PAUSE_MS;
  character.setOnClickListener(v->{stopWander();nextWander=SystemClock.elapsedRealtime()+CompanionWalk.PAUSE_MS;if(moving){moving=false;animate(false);character.setContentDescription("Pocket Yuki. Tap for companion actions.");}else toggleMenu();});
  character.setOnTouchListener((v,e)->{
   if(!moving){if(e.getActionMasked()==MotionEvent.ACTION_DOWN)stopWander();return false;}
   switch(e.getActionMasked()){
    case MotionEvent.ACTION_DOWN:downX=e.getRawX();downY=e.getRawY();originX=position.x;originY=position.y;dragged=false;animate(true);return true;
    case MotionEvent.ACTION_MOVE:float dx=e.getRawX()-downX,dy=e.getRawY()-downY;dragged|=Math.hypot(dx,dy)>ViewConfiguration.get(context).getScaledTouchSlop();position.x=clamp(originX+Math.round(dx),maxX());position.y=clamp(originY+Math.round(dy),maxY());windows.updateViewLayout(character,position);return true;
    case MotionEvent.ACTION_UP:savePosition();if(!dragged)v.performClick();moving=false;animate(false);character.setContentDescription("Pocket Yuki. Tap for companion actions.");return true;
    case MotionEvent.ACTION_CANCEL:position.x=originX;position.y=originY;windows.updateViewLayout(character,position);moving=false;animate(false);return true;
    default:return true;
   }
  });
  try{windows.addView(character,position);animate(false);}catch(RuntimeException e){character=null;}
 }
 private void savePosition(){anchorX=position.x;prefs.edit().putFloat("x",(position.x-margin())/(float)Math.max(1,maxX()-margin())).putFloat("y",(position.y-margin())/(float)Math.max(1,maxY()-margin())).apply();}
 private void animate(boolean carried){
  stopWander();if(character==null)return;character.play(carried?"carried":pose());

 }

 public void react(String mode){stopWander();reaction=mode;reactionUntil=SystemClock.elapsedRealtime()+8000;if(character!=null)character.play(mode.equals("warning")?"waiting":"failed");}
 private void toggleMenu(){if(menu!=null){closeMenu();return;}if(character==null)return;
  LinearLayout content=Ui.column(context);content.setPadding(Ui.dp(context,12),Ui.dp(context,8),Ui.dp(context,12),Ui.dp(context,8));content.setBackground(Ui.shape(context,Ui.CARD,20));
  Ui.text(content,"Stay close, Darling. ♡",15,Ui.WHITE);
  Ui.secondary(content,"Boop ♡",()->{closeMenu();Toast.makeText(context,"A kiss for you, Darling. ♡",Toast.LENGTH_SHORT).show();boopUntil=SystemClock.elapsedRealtime()+1440;if(character!=null)character.restart("kiss");});
  Ui.secondary(content,"Talk — open ChatGPT",()->{closeMenu();Device.talk(context);});
  Ui.secondary(content,"Move — pick me up",()->{closeMenu();moving=true;animate(true);character.setContentDescription("Yuki is ready to move. Drag to place her, or tap to cancel.");character.announceForAccessibility("Drag Yuki to place her. Tap to cancel.");});
  Ui.secondary(content,"Hide Yuki",()->{prefs.edit().putBoolean("enabled",false).apply();hide();});Ui.secondary(content,"Close menu",this::closeMenu);
  int w=Math.min(Ui.dp(context,248),Math.max(1,width-2*margin()));content.measure(View.MeasureSpec.makeMeasureSpec(w,View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
  int h=Math.min(content.getMeasuredHeight(),Math.max(1,height-Ui.dp(context,80)));ScrollView scroll=new ScrollView(context);scroll.addView(content);menu=scroll;
  WindowManager.LayoutParams p=params(w,h);p.setTitle("Pocket Yuki actions");p.x=clamp(position.x+position.width/2-w/2,Math.max(margin(),width-w-margin()));int above=position.y-h-Ui.dp(context,8);p.y=above>=margin()?above:Math.min(position.y+position.height+Ui.dp(context,8),Math.max(margin(),height-h-Ui.dp(context,48)));
  try{windows.addView(menu,p);}catch(RuntimeException e){menu=null;}
 }
 private void wander(){
  final int from=position.x;final int to=CompanionWalk.target(from,anchorX,margin(),maxX(),Ui.dp(context,96));nextWander=SystemClock.elapsedRealtime()+CompanionWalk.PAUSE_MS;if(from==to)return;character.play(to>from?"running_right":"running_left");wander=ValueAnimator.ofInt(from,to);wander.setDuration(CompanionWalk.duration(to-from,context.getResources().getDisplayMetrics().density));wander.addUpdateListener(a->{if(character==null)return;position.x=(int)a.getAnimatedValue();windows.updateViewLayout(character,position);});wander.addListener(new android.animation.AnimatorListenerAdapter(){public void onAnimationEnd(android.animation.Animator a){wander=null;nextWander=SystemClock.elapsedRealtime()+CompanionWalk.PAUSE_MS;if(character!=null)character.play(pose());}});wander.start();
 }
 private void stopWander(){if(wander!=null){ValueAnimator old=wander;wander=null;old.cancel();}nextWander=SystemClock.elapsedRealtime()+CompanionWalk.PAUSE_MS;}
 private void closeMenu(){if(menu!=null){try{windows.removeView(menu);}catch(RuntimeException ignored){}menu=null;}}
 public void hide(){closeMenu();moving=false;stopWander();if(character!=null){character.animate().cancel();try{windows.removeView(character);}catch(RuntimeException ignored){}character=null;}}
}
