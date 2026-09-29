package com.mavyy.yukilockdown;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;

/** Shared native controls. All artwork keeps its original alpha; layout uses dp/sp. */
public final class Ui {
 public static final int BG=0xff0c1018,CARD=0xff171f2d,BLUE=0xffa4ddff,WHITE=0xfff2f6fd,MUTED=0xffa2afc2,LINE=0xff2b384c;
 public static int dp(Context c,int n){return Math.round(n*c.getResources().getDisplayMetrics().density);}
 public static LinearLayout column(Context c){LinearLayout l=new LinearLayout(c);l.setOrientation(LinearLayout.VERTICAL);return l;}
 public static GradientDrawable shape(Context c,int color,int radius){GradientDrawable g=new GradientDrawable();g.setColor(color);g.setCornerRadius(dp(c,radius));return g;}
 public static android.graphics.drawable.Drawable ripple(Context c,int color,int radius){return new RippleDrawable(ColorStateList.valueOf(0x339fdcff),shape(c,color,radius),shape(c,Color.WHITE,radius));}
 public static LinearLayout card(LinearLayout p){Context c=p.getContext();LinearLayout l=column(c);l.setPadding(dp(c,20),dp(c,18),dp(c,20),dp(c,18));GradientDrawable g=shape(c,CARD,24);g.setStroke(dp(c,1),LINE);l.setBackground(g);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,dp(c,8),0,dp(c,8));p.addView(l,lp);return l;}
 public static TextView text(LinearLayout p,String s,int size,int color){TextView t=new TextView(p.getContext());t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setFontFeatureSettings("kern");t.setLineSpacing(dp(p.getContext(),2),1.05f);t.setPadding(0,dp(p.getContext(),4),0,dp(p.getContext(),6));t.setTypeface(Typeface.create(size>=20?"sans-serif-medium":"sans-serif",Typeface.NORMAL));p.addView(t);return t;}
 public static Button button(LinearLayout p,String s,Runnable action){return button(p,s,action,false);}
 public static Button secondary(LinearLayout p,String s,Runnable action){return button(p,s,action,true);}
 private static Button button(LinearLayout p,String s,Runnable action,boolean secondary){Context c=p.getContext();Button b=new Button(c);b.setText(s);b.setAllCaps(false);b.setTextSize(15);b.setTypeface(Typeface.create("sans-serif-medium",Typeface.NORMAL));b.setTextColor(secondary?WHITE:BG);b.setBackgroundTintList(null);b.setBackground(ripple(c,secondary?0xff253146:BLUE,16));b.setStateListAnimator(null);b.setMinimumHeight(dp(c,52));b.setMinHeight(dp(c,52));b.setPadding(dp(c,16),dp(c,12),dp(c,16),dp(c,12));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,dp(c,6),0,dp(c,6));p.addView(b,lp);b.setOnClickListener(v->action.run());return b;}
 public static CheckBox check(LinearLayout p,String s,boolean checked){Context c=p.getContext();CheckBox b=new CheckBox(c);b.setText(s);b.setTextSize(15);b.setTextColor(WHITE);b.setButtonTintList(ColorStateList.valueOf(BLUE));b.setChecked(checked);b.setMinHeight(dp(c,52));b.setPadding(dp(c,4),dp(c,6),dp(c,8),dp(c,6));p.addView(b,new LinearLayout.LayoutParams(-1,-2));return b;}
 public static Switch toggle(LinearLayout p,String s,boolean checked){Context c=p.getContext();Switch b=new Switch(c);b.setText(s);b.setTextColor(WHITE);b.setTextSize(15);b.setShowText(false);b.setSwitchPadding(dp(c,12));b.setThumbTintList(new ColorStateList(new int[][]{new int[]{android.R.attr.state_checked},new int[]{}},new int[]{BLUE,MUTED}));b.setChecked(checked);b.setMinHeight(dp(c,56));p.addView(b,new LinearLayout.LayoutParams(-1,-2));return b;}
 public static EditText field(LinearLayout p,String label,String value,boolean numeric){Context c=p.getContext();text(p,label,13,MUTED);EditText e=new EditText(c);e.setText(value);e.setTextColor(WHITE);e.setTextSize(16);e.setSingleLine(true);e.setInputType(numeric?android.text.InputType.TYPE_CLASS_NUMBER:android.text.InputType.TYPE_CLASS_TEXT);GradientDrawable g=shape(c,0xff101722,14);g.setStroke(dp(c,1),LINE);e.setBackground(g);e.setPadding(dp(c,14),dp(c,14),dp(c,14),dp(c,14));e.setMinHeight(dp(c,54));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,dp(c,3),0,dp(c,12));p.addView(e,lp);return e;}
 public static int characterResource(String mode){return switch(mode){case "bedtime"->R.drawable.yuki_bedtime;case "focus"->R.drawable.yuki_focus;case "outdoors"->R.drawable.yuki_outdoors;case "warning"->R.drawable.yuki_warning;case "annoyed"->R.drawable.yuki_annoyed;default->R.drawable.yuki_neutral;};}
 public static ImageView characterView(Context c,String mode){ImageView i=new ImageView(c);i.setImageResource(characterResource(mode));i.setScaleType(ImageView.ScaleType.FIT_CENTER);i.setContentDescription("Yuki, "+mode);i.setBackgroundColor(Color.TRANSPARENT);return i;}
 public static void character(LinearLayout p,String mode,int height){p.addView(characterView(p.getContext(),mode),new LinearLayout.LayoutParams(-1,dp(p.getContext(),height)));}
 public static boolean motion(Context c){return ValueAnimator.areAnimatorsEnabled()&&!c.getSharedPreferences("presentation",0).getBoolean("reduce_motion",false);}
 public static void sheet(AlertDialog d){android.view.Window w=d.getWindow();if(w==null)return;Context c=d.getContext();w.setBackgroundDrawable(shape(c,CARD,28));w.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);WindowManager.LayoutParams a=w.getAttributes();a.dimAmount=.65f;w.setAttributes(a);w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);w.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);w.setLayout(Math.min(c.getResources().getDisplayMetrics().widthPixels-dp(c,16),dp(c,600)),-2);for(int which:new int[]{-1,-2,-3}){Button b=d.getButton(which);if(b!=null){b.setAllCaps(false);b.setTextColor(BLUE);b.setMinHeight(dp(c,48));}}}
 public static void progress(LinearLayout p,float fraction){Context c=p.getContext();ProgressBar bar=new ProgressBar(c,null,android.R.attr.progressBarStyleHorizontal);bar.setMax(1000);bar.setProgress(Math.round(Math.max(0,Math.min(1,fraction))*1000));bar.setProgressTintList(ColorStateList.valueOf(BLUE));bar.setProgressBackgroundTintList(ColorStateList.valueOf(LINE));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,dp(c,6));lp.setMargins(0,dp(c,6),0,dp(c,12));p.addView(bar,lp);}
 public static String time(int m){return String.format(java.util.Locale.ROOT,"%02d:%02d",m/60,m%60);}
 public static int time(String s){String[]v=s.trim().split(":");if(v.length!=2)throw new IllegalArgumentException("Use HH:MM, such as 00:00 or 08:00.");int h=Integer.parseInt(v[0]),m=Integer.parseInt(v[1]);if(h<0||h>23||m<0||m>59)throw new IllegalArgumentException("Time must be 00:00–23:59.");return h*60+m;}
}
