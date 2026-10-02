package com.mavyy.yukilockdown;
import android.content.Context;
import android.graphics.*;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class ContextPresentationTest {
 private Bitmap render(Bitmap source,boolean phone,boolean motion,long time){Bitmap b=Bitmap.createBitmap(source.getWidth(),source.getHeight(),Bitmap.Config.ARGB_8888);ContextYukiDrawable d=new ContextYukiDrawable(source,phone,motion);d.setBounds(0,0,b.getWidth(),b.getHeight());d.drawAt(new Canvas(b),time);return b;}
 @Test public void watchKeepsChairAndBodyFixed(){Context c=InstrumentationRegistry.getInstrumentation().getTargetContext();Bitmap src=BitmapFactory.decodeResource(c.getResources(),R.drawable.yuki_adult_watch),a=render(src,false,true,0),b=render(src,false,true,525);int changed=0;for(int y=0;y<a.getHeight();y++)for(int x=0;x<a.getWidth();x++){if(y<870||x<850)assertEquals("Chair/body moved",a.getPixel(x,y),b.getPixel(x,y));else if(a.getPixel(x,y)!=b.getPixel(x,y))changed++;}assertTrue("Feet should gently kick",changed>0);src.recycle();a.recycle();b.recycle();}
 @Test public void phoneGlowNeverMovesBodyOrAddsOpaqueBackground(){Context c=InstrumentationRegistry.getInstrumentation().getTargetContext();Bitmap src=BitmapFactory.decodeResource(c.getResources(),R.drawable.yuki_adult_phone),a=render(src,true,true,0),b=render(src,true,true,1080);int changed=0;for(int y=0;y<a.getHeight();y++)for(int x=0;x<a.getWidth();x++){int before=a.getPixel(x,y),after=b.getPixel(x,y);assertEquals("Glow changed alpha",Color.alpha(before),Color.alpha(after));if(y>=555||x<306||x>=726)assertEquals(before,after);else if(before!=after)changed++;}assertTrue(changed>0);src.recycle();a.recycle();b.recycle();}
 @Test public void reducedMotionFreezesBothPoses(){Context c=InstrumentationRegistry.getInstrumentation().getTargetContext();for(boolean phone:new boolean[]{false,true}){Bitmap src=BitmapFactory.decodeResource(c.getResources(),phone?R.drawable.yuki_adult_phone:R.drawable.yuki_adult_watch),a=render(src,phone,false,0),b=render(src,phone,false,1080);assertTrue(a.sameAs(b));src.recycle();a.recycle();b.recycle();}}
}
