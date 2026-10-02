package com.mavyy.yukilockdown;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;

/** Local pose effects only. No window movement, screen contents or additional app tracking. */
final class ContextYukiDrawable extends Drawable implements Runnable {
 private final Bitmap image;private final boolean phone,motion;private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
 private final Path near=new Path(),far=new Path();private boolean running;private long epoch;
 ContextYukiDrawable(Bitmap image,boolean phone,boolean motion){this.image=image;this.phone=phone;this.motion=motion;
  polygon(near,new float[]{887,986,978,957,1170,1168,1170,1238,918,1238,865,1060});
  polygon(far,new float[]{1007,922,1107,878,1254,1028,1254,1130,1081,1130,982,990});
 }
 private static void polygon(Path path,float[] points){path.moveTo(points[0],points[1]);for(int i=2;i<points.length;i+=2)path.lineTo(points[i],points[i+1]);path.close();}
 void start(){if(!motion||running)return;running=true;epoch=SystemClock.uptimeMillis();scheduleSelf(this,epoch+50);}
 void stop(){running=false;unscheduleSelf(this);}
 @Override public void run(){if(running){invalidateSelf();scheduleSelf(this,SystemClock.uptimeMillis()+50);}}
 @Override public void draw(Canvas c){drawAt(c,running?(SystemClock.uptimeMillis()-epoch):0);}
 // Deterministic rendering hook for device raster checks; time is relative to attachment.
 void drawAt(Canvas c,long elapsed){int save=c.save();Rect b=getBounds();c.translate(b.left,b.top);c.scale(b.width()/(float)image.getWidth(),b.height()/(float)image.getHeight());
  if(phone){int layer=c.saveLayer(0,0,image.getWidth(),image.getHeight(),null);c.drawBitmap(image,0,0,paint);
   if(motion){float phase=(elapsed%6000)/6000f;float pulse=phase<.36f?(float)Math.sin(Math.PI*phase/.36f):0;Paint glow=new Paint(Paint.ANTI_ALIAS_FLAG);glow.setShader(new RadialGradient(516,345,210,new int[]{Color.argb(Math.round(40*pulse),167,216,255),Color.TRANSPARENT},null,Shader.TileMode.CLAMP));glow.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));c.drawRect(306,135,726,555,glow);}
   c.restoreToCount(layer);
  }else if(motion){int fixed=c.save();c.clipOutPath(near);c.clipOutPath(far);c.drawBitmap(image,0,0,paint);c.restoreToCount(fixed);
   float phase=(elapsed%7000)/7000f;float kick=phase<.3f?(float)Math.sin(2*Math.PI*phase/.3f)*1.4f:0;
   foot(c,near,kick,928,979);foot(c,far,-kick*.65f,1051,921);
  }else c.drawBitmap(image,0,0,paint);
  c.restoreToCount(save);
 }
 private void foot(Canvas c,Path path,float angle,float x,float y){int save=c.save();c.rotate(angle,x,y);c.clipPath(path);c.drawBitmap(image,0,0,paint);c.restoreToCount(save);}
 @Override public int getIntrinsicWidth(){return image.getWidth();}
 @Override public int getIntrinsicHeight(){return image.getHeight();}
 @Override public void setAlpha(int alpha){paint.setAlpha(alpha);invalidateSelf();}
 @Override public void setColorFilter(ColorFilter filter){paint.setColorFilter(filter);invalidateSelf();}
 @Override public int getOpacity(){return PixelFormat.TRANSLUCENT;}
}
