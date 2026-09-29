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
}
