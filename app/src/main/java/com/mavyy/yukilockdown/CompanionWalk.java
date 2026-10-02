package com.mavyy.yukilockdown;
/** Bounded travel around saved placement; gait must never start for a zero-distance path. */
public final class CompanionWalk {
 public static final long PAUSE_MS=12000;
 private CompanionWalk(){}
 public static int target(int from,int anchor,int minimum,int maximum,int step){
  int direction=from>=anchor?-1:1;
  int to=Math.max(minimum,Math.min(maximum,anchor+direction*step));
  if(to==from)to=Math.max(minimum,Math.min(maximum,anchor-direction*step));
  return to;
 }
 public static long duration(int distancePx,float density){return Math.max(1200,Math.min(7000,Math.round(Math.abs(distancePx)/Math.max(.1f,density)*1000/32)));}
}
