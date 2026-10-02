package com.mavyy.yukilockdown;
import android.graphics.*;
import android.graphics.drawable.Drawable;
/** Crops the unchanged adult sprite/animation to its waist, preserving proportions. */
final class WaistYukiDrawable extends Drawable implements Drawable.Callback {
 static final int WIDTH=354,HEIGHT=330;private final Drawable child;
 WaistYukiDrawable(Drawable child){this.child=child;child.setCallback(this);}
 public void draw(Canvas canvas){int save=canvas.save();Rect b=getBounds();canvas.clipRect(b);canvas.translate(b.left,b.top);canvas.scale(b.width()/(float)WIDTH,b.height()/(float)HEIGHT);canvas.translate(-32,0);child.setBounds(0,0,418,627);child.draw(canvas);canvas.restoreToCount(save);}
 public int getIntrinsicWidth(){return WIDTH;}public int getIntrinsicHeight(){return HEIGHT;}
 public void setAlpha(int alpha){child.setAlpha(alpha);}public void setColorFilter(ColorFilter filter){child.setColorFilter(filter);}public int getOpacity(){return PixelFormat.TRANSLUCENT;}
 public void invalidateDrawable(Drawable who){invalidateSelf();}public void scheduleDrawable(Drawable who,Runnable task,long when){scheduleSelf(task,when);}public void unscheduleDrawable(Drawable who,Runnable task){unscheduleSelf(task);}
}
