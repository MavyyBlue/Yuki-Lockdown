package com.mavyy.yukilockdown;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/** Frame animation from owner-supplied Mini Yuki. No generated/redrawn frames. */
public final class MiniYukiView extends ImageView {
 private String state=""; private boolean animated; private AnimationDrawable loop;
 public MiniYukiView(Context c){super(c);setScaleType(ScaleType.FIT_CENTER);setBackgroundColor(Color.TRANSPARENT);play("idle");}
 public void play(String next){boolean motion=Ui.motion(getContext());if(state.equals(next)&&animated==motion)return;stop();state=next;animated=motion;int[] frames=frames(next);
  if(!motion||next.equals("carried")){Drawable d=getContext().getDrawable(frames[0]);smooth(d);setImageDrawable(d);return;}
  loop=new AnimationDrawable();loop.setOneShot(false);for(int i=0;i<frames.length;i++){Drawable d=getContext().getDrawable(frames[i]);smooth(d);loop.addFrame(d,duration(next,i));}setImageDrawable(loop);if(isAttachedToWindow())loop.start();
 }
 private void smooth(Drawable d){if(d instanceof BitmapDrawable b){b.setFilterBitmap(true);b.setAntiAlias(true);}}
 private void stop(){if(loop!=null){loop.stop();loop=null;}}
 protected void onAttachedToWindow(){super.onAttachedToWindow();if(loop!=null&&Ui.motion(getContext()))loop.start();}
 protected void onDetachedFromWindow(){stop();state="";super.onDetachedFromWindow();}
 private static int duration(String state,int frame){if(state.equals("idle"))return frame==0||frame==3||frame==5?750:140;return state.startsWith("running_")?110:180;}
 private static int[] frames(String state){return switch(state){
   case "idle" -> new int[]{R.drawable.mini_yuki_idle_00,R.drawable.mini_yuki_idle_01,R.drawable.mini_yuki_idle_02,R.drawable.mini_yuki_idle_03,R.drawable.mini_yuki_idle_04,R.drawable.mini_yuki_idle_05};
   case "running_right" -> new int[]{R.drawable.mini_yuki_running_right_00,R.drawable.mini_yuki_running_right_01,R.drawable.mini_yuki_running_right_02,R.drawable.mini_yuki_running_right_03,R.drawable.mini_yuki_running_right_04,R.drawable.mini_yuki_running_right_05,R.drawable.mini_yuki_running_right_06,R.drawable.mini_yuki_running_right_07};
   case "running_left" -> new int[]{R.drawable.mini_yuki_running_left_00,R.drawable.mini_yuki_running_left_01,R.drawable.mini_yuki_running_left_02,R.drawable.mini_yuki_running_left_03,R.drawable.mini_yuki_running_left_04,R.drawable.mini_yuki_running_left_05,R.drawable.mini_yuki_running_left_06,R.drawable.mini_yuki_running_left_07};
   case "waving" -> new int[]{R.drawable.mini_yuki_waving_00,R.drawable.mini_yuki_waving_01,R.drawable.mini_yuki_waving_02,R.drawable.mini_yuki_waving_03};
   case "jumping" -> new int[]{R.drawable.mini_yuki_jumping_00,R.drawable.mini_yuki_jumping_01,R.drawable.mini_yuki_jumping_02,R.drawable.mini_yuki_jumping_03,R.drawable.mini_yuki_jumping_04};
   case "failed" -> new int[]{R.drawable.mini_yuki_failed_00,R.drawable.mini_yuki_failed_01,R.drawable.mini_yuki_failed_02,R.drawable.mini_yuki_failed_03,R.drawable.mini_yuki_failed_04,R.drawable.mini_yuki_failed_05,R.drawable.mini_yuki_failed_06,R.drawable.mini_yuki_failed_07};
   case "waiting" -> new int[]{R.drawable.mini_yuki_waiting_00,R.drawable.mini_yuki_waiting_01,R.drawable.mini_yuki_waiting_02,R.drawable.mini_yuki_waiting_03,R.drawable.mini_yuki_waiting_04,R.drawable.mini_yuki_waiting_05};
   case "running" -> new int[]{R.drawable.mini_yuki_running_00,R.drawable.mini_yuki_running_01,R.drawable.mini_yuki_running_02,R.drawable.mini_yuki_running_03,R.drawable.mini_yuki_running_04,R.drawable.mini_yuki_running_05};
   case "review" -> new int[]{R.drawable.mini_yuki_review_00,R.drawable.mini_yuki_review_01,R.drawable.mini_yuki_review_02,R.drawable.mini_yuki_review_03,R.drawable.mini_yuki_review_04,R.drawable.mini_yuki_review_05};
   case "carried" -> new int[]{R.drawable.mini_yuki_jumping_02};
   default -> new int[]{R.drawable.mini_yuki_idle_00};
  };}
}
