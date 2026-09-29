package com.mavyy.yukilockdown;
import android.content.Context;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class StoreTest {
 private Context getContext(){return InstrumentationRegistry.getInstrumentation().getTargetContext();}
 private Store s;
 @Before public void setUp() {s=Store.get(getContext());clear();}
 private void clear(){for(String table:new String[]{"config","attempts","bypass"})s.getWritableDatabase().delete(table,null,null);s.error="";}
 @After public void tearDown() {clear();}
 @Test public void testPersistence() throws Exception {Rules.Config c=new Rules.Config();Rules.AppRule a=new Rules.AppRule();a.minutes=20;c.apps.put("video",a);Rules.Schedule p=new Rules.Schedule();p.apps.add("video");p.strict=true;c.schedules.add(p);c.domains.add("example.com");assertTrue(s.save(c));s.close();assertEquals(Store.encode(c),Store.encode(s.load()));}
 @Test public void testAttempts(){assertEquals(1,s.attempt("period"));s.close();assertEquals(2,s.attempt("period"));assertEquals(1,s.attempt("new"));}
 @Test public void testCorruptionFailsOpen(){s.getWritableDatabase().execSQL("INSERT INTO config VALUES (1,'broken')");assertFalse(s.load().enabled);assertFalse(s.error.isEmpty());assertFalse(s.save(new Rules.Config()));}
 @Test public void testBypass(){s.bypass(5);s.close();assertTrue(s.bypassActive());s.clearBypass();assertFalse(s.bypassActive());}
 @Test public void testPermissionState(){assertNotNull(Device.usage(getContext()));}
}
