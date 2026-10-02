package com.mavyy.yukilockdown;
/** Floating companion height in dp, constrained to the available display. */
public final class CompanionSize {
 public static final int MIN=80, MAX=240, DEFAULT=160;
 private CompanionSize(){}
 public static int clamp(int dp){return Math.max(MIN,Math.min(MAX,dp));}
 public static int height(int requestedPx,int availableWidth,int availableHeight){
  return Math.max(1,Math.min(requestedPx,Math.min(Math.max(1,availableHeight),Math.max(1,availableWidth)*3/2)));
 }
 public static int width(int heightPx,int minimumTouchPx,int availableWidth){return Math.max(1,Math.min(Math.max(1,availableWidth),Math.max(minimumTouchPx,Math.round(heightPx*2f/3))));}
}
