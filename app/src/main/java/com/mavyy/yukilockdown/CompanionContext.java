package com.mavyy.yukilockdown;
/** Ephemeral presentation only: uses the foreground package already observed by Guard. */
public final class CompanionContext {
 public enum Mode { NORMAL, WATCH, PHONE }
 public static Mode mode(String pkg){return switch(pkg==null?"":pkg){case "com.zhiliaoapp.musically","com.google.android.youtube"->Mode.WATCH;case "com.openai.chatgpt"->Mode.PHONE;default->Mode.NORMAL;};}
 public static boolean stationary(Mode mode){return mode!=Mode.NORMAL;}
 /** The watch sprite faces right; mirror only when placed in the right half. */
 public static boolean faceLeft(int x,int width,int screenWidth){return x+width/2>screenWidth/2;}
 public static String pose(Mode mode,boolean left,boolean moving,boolean kiss,boolean reaction,boolean warning){return moving?"carried":kiss?"kiss":reaction?(warning?"waiting":"failed"):mode==Mode.WATCH?(left?"watch_left":"watch_right"):mode==Mode.PHONE?"phone":"idle";}
}
