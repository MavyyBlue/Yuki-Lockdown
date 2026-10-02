package com.mavyy.yukilockdown;
import org.junit.Test;
import static org.junit.Assert.*;
public class CompanionWalkTest {
 @Test public void defaultAtRightEdgeWalksInward(){assertEquals(804,CompanionWalk.target(900,900,16,900,96));}
 @Test public void leftEdgeCannotWalkInPlace(){assertEquals(112,CompanionWalk.target(16,16,16,900,96));}
 @Test public void alternatesAroundAnchor(){assertEquals(404,CompanionWalk.target(500,500,16,900,96));assertEquals(596,CompanionWalk.target(404,500,16,900,96));assertEquals(404,CompanionWalk.target(596,500,16,900,96));}
 @Test public void narrowScreenAndNoRoom(){assertEquals(30,CompanionWalk.target(16,16,16,30,96));assertEquals(16,CompanionWalk.target(16,16,16,16,96));}
 @Test public void speedAndDurationBounded(){assertEquals(3000,CompanionWalk.duration(288,3));assertEquals(1200,CompanionWalk.duration(1,3));assertEquals(7000,CompanionWalk.duration(5000,3));}
}
