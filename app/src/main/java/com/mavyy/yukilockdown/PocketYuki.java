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
 private ImageView character; private View menu; private WindowManager.LayoutParams position;
 private ValueAnimator idle; private boolean moving; private int width,height,originX,originY;
 private float downX,downY; private boolean dragged; private String reaction="neutral"; private long reactionUntil;
 public PocketYuki(Context c,WindowManager w){context=c;windows=w;prefs=c.getSharedPreferences("pocket_yuki",0);}
 public void sync(boolean eligible){
  if(!eligible||!prefs.getBoolean("enabled",true)){hide();return;}
  Point size=new Point();windows.getDefaultDisplay().getSize(size);
  if(width!=size.x||height!=size.y){hide();width=size.x;height=size.y;}
  if(character==null)show();
  if(character!=null){character.setImageResource(Ui.characterResource(SystemClock.elapsedRealtime()<reactionUntil?reaction:"neutral"));if(!moving&&idle==null&&Ui.motion(context))animate(false);else if(!Ui.motion(context))stopAnimation();}
 }
 private WindowManager.LayoutParams params(int w,int h){WindowManager.LayoutParams p=new WindowManager.LayoutParams(w,h,WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,PixelFormat.TRANSLUCENT);p.gravity=Gravity.TOP|Gravity.LEFT;return p;}
 private int margin(){return Ui.dp(context,16);}
 private int maxX(){return Math.max(margin(),width-position.width-margin());}
 private int maxY(){return Math.max(margin(),height-position.height-Ui.dp(context,48));}
 private int clamp(int v,int maximum){return Math.max(margin(),Math.min(maximum,v));}
 private void show(){
  character=Ui.characterView(context,"neutral");character.setContentDescription("Pocket Yuki. Tap for companion actions.");character.setFocusable(true);character.setClickable(true);
  int side=Math.min(Ui.dp(context,104),Math.max(Ui.dp(context,48),width-2*margin()));position=params(side,side);position.setTitle("Pocket Yuki companion");
  position.x=clamp(margin()+Math.round(prefs.getFloat("x",.85f)*(maxX()-margin())),maxX());position.y=clamp(margin()+Math.round(prefs.getFloat("y",.7f)*(maxY()-margin())),maxY());
  character.setOnClickListener(v->{if(moving){moving=false;animate(false);character.setContentDescription("Pocket Yuki. Tap for companion actions.");}else toggleMenu();});
  character.setOnTouchListener((v,e)->{
   if(!moving)return false;
   switch(e.getActionMasked()){
    case MotionEvent.ACTION_DOWN:downX=e.getRawX();downY=e.getRawY();originX=position.x;originY=position.y;dragged=false;animate(true);return true;
    case MotionEvent.ACTION_MOVE:float dx=e.getRawX()-downX,dy=e.getRawY()-downY;dragged|=Math.hypot(dx,dy)>ViewConfiguration.get(context).getScaledTouchSlop();position.x=clamp(originX+Math.round(dx),maxX());position.y=clamp(originY+Math.round(dy),maxY());windows.updateViewLayout(character,position);return true;
    case MotionEvent.ACTION_UP:savePosition();if(!dragged)v.performClick();moving=false;animate(false);character.setContentDescription("Pocket Yuki. Tap for companion actions.");return true;
    case MotionEvent.ACTION_CANCEL:position.x=originX;position.y=originY;windows.updateViewLayout(character,position);moving=false;animate(false);return true;
    default:return true;
   }
  });
  try{windows.addView(character,position);animate(false);}catch(RuntimeException e){character=null;stopAnimation();}
 }
 private void savePosition(){prefs.edit().putFloat("x",(position.x-margin())/(float)Math.max(1,maxX()-margin())).putFloat("y",(position.y-margin())/(float)Math.max(1,maxY()-margin())).apply();}
 private void animate(boolean carried){
  stopAnimation();if(character==null||!Ui.motion(context))return;
  character.setPivotX(position.width*.5f);character.setPivotY(position.height*(carried?.25f:.9f));
  idle=ValueAnimator.ofFloat(0,1);idle.setDuration(carried?1100:3000);idle.setRepeatCount(ValueAnimator.INFINITE);idle.setRepeatMode(ValueAnimator.REVERSE);
  idle.addUpdateListener(a->{if(character==null)return;float f=(float)a.getAnimatedValue();character.setRotation(carried?-7+14*f:-1+2*f);character.setScaleY(carried?1:.985f+.015f*f);character.setTranslationY(carried?Ui.dp(context,3):Ui.dp(context,2)*f);});idle.start();
 }
 private void stopAnimation(){if(idle!=null){idle.cancel();idle=null;}if(character!=null){character.setRotation(0);character.setScaleY(1);character.setTranslationY(0);}}
 public void react(String mode){reaction=mode;reactionUntil=SystemClock.elapsedRealtime()+8000;if(character!=null)character.setImageResource(Ui.characterResource(mode));}
 private void toggleMenu(){if(menu!=null){closeMenu();return;}if(character==null)return;
  LinearLayout content=Ui.column(context);content.setPadding(Ui.dp(context,12),Ui.dp(context,8),Ui.dp(context,12),Ui.dp(context,8));content.setBackground(Ui.shape(context,Ui.CARD,20));
  Ui.text(content,"Stay close, Darling. ♡",15,Ui.WHITE);
  Ui.secondary(content,"Boop ♡",()->{closeMenu();Toast.makeText(context,"Booped. I noticed, Darling. ♡",Toast.LENGTH_SHORT).show();if(character!=null&&Ui.motion(context))character.animate().scaleX(1.08f).setDuration(120).withEndAction(()->{if(character!=null)character.animate().scaleX(1).setDuration(120).start();}).start();});
  Ui.secondary(content,"Talk — open ChatGPT",()->{closeMenu();Device.talk(context);});
  Ui.secondary(content,"Move — pick me up",()->{closeMenu();moving=true;animate(true);character.setContentDescription("Yuki is ready to move. Drag to place her, or tap to cancel.");character.announceForAccessibility("Drag Yuki to place her. Tap to cancel.");});
  Ui.secondary(content,"Hide Yuki",()->{prefs.edit().putBoolean("enabled",false).apply();hide();});Ui.secondary(content,"Close menu",this::closeMenu);
  int w=Math.min(Ui.dp(context,248),Math.max(1,width-2*margin()));content.measure(View.MeasureSpec.makeMeasureSpec(w,View.MeasureSpec.EXACTLY),View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED));
  int h=Math.min(content.getMeasuredHeight(),Math.max(1,height-Ui.dp(context,80)));ScrollView scroll=new ScrollView(context);scroll.addView(content);menu=scroll;
  WindowManager.LayoutParams p=params(w,h);p.setTitle("Pocket Yuki actions");p.x=clamp(position.x+position.width/2-w/2,Math.max(margin(),width-w-margin()));int above=position.y-h-Ui.dp(context,8);p.y=above>=margin()?above:Math.min(position.y+position.height+Ui.dp(context,8),Math.max(margin(),height-h-Ui.dp(context,48)));
  try{windows.addView(menu,p);}catch(RuntimeException e){menu=null;}
 }
 private void closeMenu(){if(menu!=null){try{windows.removeView(menu);}catch(RuntimeException ignored){}menu=null;}}
 public void hide(){closeMenu();moving=false;stopAnimation();if(character!=null){character.animate().cancel();try{windows.removeView(character);}catch(RuntimeException ignored){}character=null;}}
}
