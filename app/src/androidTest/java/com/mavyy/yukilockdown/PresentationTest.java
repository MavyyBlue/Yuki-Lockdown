package com.mavyy.yukilockdown;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.View;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class PresentationTest {
 @Test public void allCharacterExportsKeepAlpha(){Context c=InstrumentationRegistry.getInstrumentation().getTargetContext();for(String mode:new String[]{"neutral","warning","annoyed","bedtime","focus","outdoors"}){Bitmap b=BitmapFactory.decodeResource(c.getResources(),Ui.characterResource(mode));assertNotNull(b);assertTrue(mode+" needs alpha",b.hasAlpha());assertEquals(mode+" corner must be transparent",0,Color.alpha(b.getPixel(0,0)));b.recycle();}}
 @Test public void transparentPresentationStillConsumesTouches(){InstrumentationRegistry.getInstrumentation().runOnMainSync(()->{Context c=new ContextThemeWrapper(InstrumentationRegistry.getInstrumentation().getTargetContext(),R.style.AppTheme);View v=Intervention.create(c,"bedtime","Preview","Time for bed",1,()->{},()->{},()->{},true);assertTrue(v.isClickable());assertTrue(v.isFocusable());assertTrue(v instanceof android.widget.FrameLayout);});}
 @Test public void idleBlinkKeepsBodyPixelsFixed(){Context c=InstrumentationRegistry.getInstrumentation().getTargetContext();Bitmap body=BitmapFactory.decodeResource(c.getResources(),R.drawable.yuki_adult_base_00),blink=BitmapFactory.decodeResource(c.getResources(),R.drawable.yuki_adult_base_01);Bitmap open=Bitmap.createBitmap(418,627,Bitmap.Config.ARGB_8888),closed=Bitmap.createBitmap(418,627,Bitmap.Config.ARGB_8888);IdleYukiDrawable a=new IdleYukiDrawable(body,blink,false),b=new IdleYukiDrawable(body,blink,true);a.setBounds(0,0,418,627);b.setBounds(0,0,418,627);a.draw(new android.graphics.Canvas(open));b.draw(new android.graphics.Canvas(closed));int changed=0;for(int y=0;y<627;y++)for(int x=0;x<418;x++){int before=open.getPixel(x,y),after=closed.getPixel(x,y);if(y>=100)assertEquals("Body moved at "+x+","+y,before,after);else if(before!=after)changed++;}assertTrue("Blink must change eyelids",changed>0);open.recycle();closed.recycle();body.recycle();blink.recycle();}

}
