package com.mavyy.yukilockdown;
import android.content.Context;
import android.graphics.*;
import android.view.View;
/** Small, density-independent navigation glyphs; text labels carry semantics. */
public final class NavIcon extends View {
 private final int item,color;private final Paint pen=new Paint(Paint.ANTI_ALIAS_FLAG);
 public NavIcon(Context c,int item,int color){super(c);this.item=item;this.color=color;setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO);}
 protected void onDraw(Canvas canvas){super.onDraw(canvas);canvas.save();float scale=Math.min(getWidth(),getHeight())/24f;canvas.translate((getWidth()-24*scale)/2,(getHeight()-24*scale)/2);canvas.scale(scale,scale);pen.setColor(color);pen.setStyle(Paint.Style.STROKE);pen.setStrokeWidth(1.7f);pen.setStrokeCap(Paint.Cap.ROUND);pen.setStrokeJoin(Paint.Join.ROUND);
  switch(item){case 0->{Path p=new Path();p.moveTo(3,10);p.lineTo(12,3);p.lineTo(21,10);p.moveTo(5,9);p.lineTo(5,21);p.lineTo(10,21);p.lineTo(10,14);p.lineTo(14,14);p.lineTo(14,21);p.lineTo(19,21);p.lineTo(19,9);canvas.drawPath(p,pen);}case 1->{for(int y:new int[]{4,14})for(int x:new int[]{4,14})canvas.drawRoundRect(x,y,x+6,y+6,2,2,pen);}case 2->{canvas.drawCircle(12,12,9,pen);canvas.drawOval(8,3,16,21,pen);canvas.drawLine(3,12,21,12,pen);}case 3->{canvas.drawRoundRect(4,5,20,21,3,3,pen);canvas.drawLine(4,10,20,10,pen);canvas.drawLine(8,3,8,7,pen);canvas.drawLine(16,3,16,7,pen);canvas.drawLine(8,15,11,18,pen);canvas.drawLine(11,18,16,13,pen);}default->{canvas.drawCircle(12,12,4,pen);canvas.drawCircle(12,12,9,pen);for(int k=0;k<8;k++){double a=k*Math.PI/4;canvas.drawLine(12+(float)Math.cos(a)*9,12+(float)Math.sin(a)*9,12+(float)Math.cos(a)*11,12+(float)Math.sin(a)*11,pen);}}}canvas.restore();
 }
}
