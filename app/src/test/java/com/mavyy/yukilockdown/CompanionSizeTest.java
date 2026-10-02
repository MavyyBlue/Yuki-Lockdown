package com.mavyy.yukilockdown;
import org.junit.Test;
import static org.junit.Assert.*;
public class CompanionSizeTest {
 @Test public void stalePreferenceClamped(){assertEquals(80,CompanionSize.clamp(-1));assertEquals(240,CompanionSize.clamp(1000));assertEquals(160,CompanionSize.clamp(160));}
 @Test public void shortLandscapeFits(){int h=CompanionSize.height(720,500,180);assertEquals(180,h);assertEquals(120,CompanionSize.width(h,48,500));}
 @Test public void narrowScreenFits(){int h=CompanionSize.height(720,100,900);assertEquals(150,h);assertEquals(100,CompanionSize.width(h,144,100));}
 @Test public void collapsedViewportDoesNotCreateInvalidWindow(){assertEquals(1,CompanionSize.height(240,0,0));assertEquals(1,CompanionSize.width(1,48,0));}
}
