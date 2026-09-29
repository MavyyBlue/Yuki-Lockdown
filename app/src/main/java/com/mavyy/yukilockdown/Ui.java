package com.mavyy.yukilockdown;
import android.content.*;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.view.*;
import android.widget.*;
public final class Ui {
 public static final int BG=Color.rgb(16,19,26),CARD=Color.rgb(27,33,44),BLUE=Color.rgb(157,220,255),WHITE=Color.rgb(242,248,255),MUTED=Color.rgb(170,183,199);
 public static int dp(Context c,int n){return (int)(n*c.getResources().getDisplayMetrics().density+.5f);}
 public static LinearLayout column(Context c){LinearLayout l=new LinearLayout(c);l.setOrientation(LinearLayout.VERTICAL);return l;}
 public static LinearLayout card(LinearLayout p){Context c=p.getContext();LinearLayout l=column(c);l.setPadding(dp(c,16),dp(c,14),dp(c,16),dp(c,14));GradientDrawable g=new GradientDrawable();g.setColor(CARD);g.setCornerRadius(dp(c,20));l.setBackground(g);LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,dp(c,8),0,dp(c,8));p.addView(l,lp);return l;}
 public static TextView text(LinearLayout p,String s,int size,int color){TextView t=new TextView(p.getContext());t.setText(s);t.setTextSize(size);t.setTextColor(color);t.setPadding(0,dp(p.getContext(),5),0,dp(p.getContext(),7));if(size>=24)t.setTypeface(null,Typeface.BOLD);p.addView(t);return t;}
 public static Button button(LinearLayout p,String s,Runnable action){Button b=new Button(p.getContext());b.setText(s);b.setAllCaps(false);b.setTextColor(BG);b.setBackgroundTintList(android.content.res.ColorStateList.valueOf(BLUE));b.setMinHeight(dp(p.getContext(),48));p.addView(b,new LinearLayout.LayoutParams(-1,-2));b.setOnClickListener(v->action.run());return b;}
 public static CheckBox check(LinearLayout p,String s,boolean checked){CheckBox b=new CheckBox(p.getContext());b.setText(s);b.setTextColor(WHITE);b.setChecked(checked);b.setMinHeight(dp(p.getContext(),48));p.addView(b);return b;}
 public static EditText field(LinearLayout p,String label,String value,boolean numeric){text(p,label,13,MUTED);EditText e=new EditText(p.getContext());e.setText(value);e.setTextColor(WHITE);e.setTextSize(16);e.setSingleLine(true);e.setInputType(numeric?android.text.InputType.TYPE_CLASS_NUMBER:android.text.InputType.TYPE_CLASS_TEXT);p.addView(e,new LinearLayout.LayoutParams(-1,-2));return e;}
 public static void character(LinearLayout p,String mode,int height){int id=switch(mode){case "bedtime"->R.drawable.yuki_bedtime;case "focus"->R.drawable.yuki_focus;case "outdoors"->R.drawable.yuki_outdoors;case "warning"->R.drawable.yuki_warning;case "annoyed"->R.drawable.yuki_annoyed;default->R.drawable.yuki_neutral;};ImageView i=new ImageView(p.getContext());i.setImageResource(id);i.setScaleType(ImageView.ScaleType.FIT_CENTER);i.setContentDescription("Yuki, "+mode);i.setBackgroundColor(Color.BLACK);p.addView(i,new LinearLayout.LayoutParams(-1,dp(p.getContext(),height)));}
 public static String time(int m){return String.format(java.util.Locale.ROOT,"%02d:%02d",m/60,m%60);}
 public static int time(String s){String[]v=s.trim().split(":");if(v.length!=2)throw new IllegalArgumentException("Use HH:MM, such as 00:00 or 08:00.");int h=Integer.parseInt(v[0]),m=Integer.parseInt(v[1]);if(h<0||h>23||m<0||m>59)throw new IllegalArgumentException("Time must be 00:00–23:59.");return h*60+m;}
}
