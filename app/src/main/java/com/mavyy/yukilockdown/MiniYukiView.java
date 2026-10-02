package com.mavyy.yukilockdown;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/** Adult companion sprite animation; approved intervention artwork is separate. */
public final class MiniYukiView extends ImageView {
 private Bitmap idleBody,idleBlink;
 private String state=""; private boolean animated; private AnimationDrawable loop;
 public MiniYukiView(Context c){super(c);setScaleType(ScaleType.FIT_CENTER);setBackgroundColor(Color.TRANSPARENT);play("idle");}
 public void restart(String next){state="";play(next);}
 public void play(String next){boolean motion=Ui.motion(getContext());if(state.equals(next)&&animated==motion)return;stop();state=next;animated=motion;setScaleX(next.equals("running_left")?-1:1);
  if(next.equals("idle")&&motion){idle();return;}int[] frames=frames(next);
  if(!motion||frames.length==1){Drawable d=getContext().getDrawable(next.equals("kiss")?R.drawable.yuki_adult_kiss_03:frames[0]);smooth(d);setImageDrawable(d);return;}
  loop=new AnimationDrawable();loop.setOneShot(next.equals("kiss"));for(int i=0;i<frames.length;i++){Drawable d=getContext().getDrawable(frames[i]);smooth(d);loop.addFrame(d,duration(next,i));}setImageDrawable(loop);if(isAttachedToWindow())loop.start();
 }
 private void idle(){
  if(idleBody==null){idleBody=BitmapFactory.decodeResource(getResources(),R.drawable.yuki_adult_base_00);idleBlink=BitmapFactory.decodeResource(getResources(),R.drawable.yuki_adult_base_01);}
  loop=new AnimationDrawable();loop.setOneShot(false);loop.addFrame(new IdleYukiDrawable(idleBody,idleBlink,false),4200);loop.addFrame(new IdleYukiDrawable(idleBody,idleBlink,true),120);setImageDrawable(loop);if(isAttachedToWindow())loop.start();
 }
 private void smooth(Drawable d){if(d instanceof BitmapDrawable b){b.setFilterBitmap(true);b.setAntiAlias(true);}}
 public void pausePlayback(){if(loop!=null)loop.stop();}
 private void stop(){if(loop!=null){loop.stop();loop=null;}}
 protected void onAttachedToWindow(){super.onAttachedToWindow();if(loop!=null&&Ui.motion(getContext()))loop.start();}
 protected void onDetachedFromWindow(){stop();state="";super.onDetachedFromWindow();}
 private static int duration(String state,int frame){if(state.equals("idle"))return frame==1?140:1200;return state.startsWith("running_")?220:180;}
 private static int[] frames(String state){return switch(state){
  case "idle" -> new int[]{R.drawable.yuki_adult_base_00};
  case "running_right","running_left" -> new int[]{R.drawable.yuki_adult_base_03,R.drawable.yuki_adult_base_04};
  case "kiss" -> new int[]{R.drawable.yuki_adult_kiss_00,R.drawable.yuki_adult_kiss_01,R.drawable.yuki_adult_kiss_02,R.drawable.yuki_adult_kiss_03,R.drawable.yuki_adult_kiss_04,R.drawable.yuki_adult_kiss_05};
  case "carried" -> new int[]{R.drawable.yuki_adult_carry_00,R.drawable.yuki_adult_carry_01,R.drawable.yuki_adult_carry_02,R.drawable.yuki_adult_carry_03,R.drawable.yuki_adult_carry_04,R.drawable.yuki_adult_carry_05};
  case "waiting","failed" -> new int[]{R.drawable.yuki_adult_base_02};
  default -> new int[]{R.drawable.yuki_adult_base_00};
 };}
}
