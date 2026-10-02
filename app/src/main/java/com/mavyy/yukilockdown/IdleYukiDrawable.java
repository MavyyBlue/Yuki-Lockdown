package com.mavyy.yukilockdown;
import android.graphics.*;
import android.graphics.drawable.Drawable;

/** Only eyelids change during idle; both frames share the exact same body pixels. */
final class IdleYukiDrawable extends Drawable {
 private final RectF left=new RectF(218,71,247,89),right=new RectF(250,59,280,85);
 private final Paint leftMask=mask(left),rightMask=mask(right);
 private final Bitmap body,blink;private final boolean closed;private final Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
 IdleYukiDrawable(Bitmap body,Bitmap blink,boolean closed){this.body=body;this.blink=blink;this.closed=closed;}
 @Override public void draw(Canvas canvas){int save=canvas.save();Rect b=getBounds();canvas.translate(b.left,b.top);canvas.scale(b.width()/418f,b.height()/627f);canvas.drawBitmap(body,null,new RectF(0,0,418,627),paint);
  if(closed){eye(canvas,left,leftMask);eye(canvas,right,rightMask);}canvas.restoreToCount(save);
 }
 private static Paint mask(RectF target){Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);RadialGradient shader=new RadialGradient(0,0,1,new int[]{Color.WHITE,Color.WHITE,Color.TRANSPARENT},new float[]{0,.65f,1},Shader.TileMode.CLAMP);Matrix m=new Matrix();m.setScale(target.width()/2,target.height()/2);m.postTranslate(target.centerX(),target.centerY());shader.setLocalMatrix(m);p.setShader(shader);p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));return p;}
 private void eye(Canvas canvas,RectF target,Paint mask){int save=canvas.save();canvas.clipRect(target);int layer=canvas.saveLayer(target,null);canvas.drawBitmap(blink,null,new RectF(34,-6,452,621),paint);canvas.drawRect(target,mask);canvas.restoreToCount(layer);canvas.restoreToCount(save);}

 @Override public int getIntrinsicWidth(){return 418;}
 @Override public int getIntrinsicHeight(){return 627;}
 @Override public void setAlpha(int alpha){paint.setAlpha(alpha);invalidateSelf();}
 @Override public void setColorFilter(ColorFilter filter){paint.setColorFilter(filter);invalidateSelf();}
 @Override public int getOpacity(){return PixelFormat.TRANSLUCENT;}
}
