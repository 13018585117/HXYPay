package com.hxypay.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.hxypay.R;

public class DialView extends View {

    private Paint mPaint;
    private Path mPath; //遮罩层路径
    private RectF mRectF; //遮罩层区域
    private Bitmap bmNormalDial, bmCheckedDial, bmNeedle;
    private float radius = 0; //表盘半径
    private float degrees = 0;  //指针角度
    private int needleWidth, needleHeight;   //指针精心化控制，宽高比为10:1。
    private int bgColor = Color.WHITE; //背景颜色

    public float getDegrees() {
        return degrees;
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //无论宽高设置多少，最终View的高总是取 （width/2 ：height）中的最小值，且宽总是高的2倍。
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int measureRadius = Math.min(measureWidth / 2, measureHeight);
        setMeasuredDimension(measureRadius * 2, measureRadius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        radius = Math.min(w / 2, h);
        needleWidth = (int) (radius / 2);
        needleHeight = needleWidth / 10;
        bmNeedle = getScaledBitmap(R.drawable.needle, needleWidth, needleHeight);
        bmNormalDial = getScaledBitmap(R.drawable.dial_checked, (int) (2 * radius), (int) radius);
        bmCheckedDial = getScaledBitmap(R.drawable.dial_checked, (int) (2 * radius), (int) radius);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);

        mPath = new Path();
        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        float rotateX = width / 2; //计算旋转中心，指针尾部的中点
        float rotateY = height - radius / 10 + needleHeight / 2;//同上
        canvas.drawColor(bgColor);

        //画白色
        canvas.save();
        canvas.drawBitmap(bmNormalDial, 0, dp2px(getContext(), -2), mPaint);
        canvas.restore();

        //画红色,（切割扇形路径，显示与图片的交集）
        canvas.save();
        mPath.reset();
        mPath.moveTo(rotateX, rotateY);
        mPath.lineTo(0, rotateY);
        mRectF.set(0, 0, width, 2 * height);
        mPath.arcTo(mRectF, 180, degrees);
        mPath.close();
        canvas.clipPath(mPath);
        canvas.drawBitmap(bmCheckedDial, 0, dp2px(getContext(), -2), mPaint);
        canvas.restore();

        //画指针
        canvas.save();
        canvas.rotate(degrees, rotateX, rotateY);
        canvas.drawBitmap(bmNeedle, width / 4 + radius / 20 / 2, height - radius / 10, mPaint);
        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        float rotateY = radius - needleHeight / 2 + dp2px(getContext(), 2);    //限制转动范围在2-179之间，背景图不标准最高只能这个精度了。如果一定要微调，注意要用dp2px()方法换算，否则会不适配不同尺寸的屏幕。
        if (event.getY() >= rotateY) {
            return true;
        }
        return gestureDetector.onTouchEvent(event);
    }

    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            //获取点击点计算角度，然后开始动画
            double pointX = e.getX() - radius;  //转成圆形计算
            double pointY = e.getY() - radius;  //同上
            double angel = Math.atan2(pointX, pointY);    //根据斜率求弧度，tan函数的逆运算
            int newDegrees = (int) (180 * angel / Math.PI);
            if (newDegrees >= 90 && newDegrees <= 180) {
                newDegrees = 180 - newDegrees + 90;
            } else if (newDegrees >= -180 && newDegrees <= -90) {
                newDegrees = -newDegrees - 90;
            }
            // Log.i("myk","角度："+newDegrees+"");
            degrees = newDegrees;
            if (mDegreesChangedListener != null) {
                mDegreesChangedListener.onDegreesChanged(degrees);
            }
            invalidate();
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            double pointX = e2.getX() - radius;  //转成圆形计算
            double pointY = e2.getY() - radius;  //+同上
            double angel = Math.atan2(pointX, pointY);    //根据斜率求弧度，tan函数的逆运算
            int newDegrees = (int) (180 * angel / Math.PI);
            if (newDegrees >= 90 && newDegrees <= 180) {
                newDegrees = 180 - newDegrees + 90;
            } else if (newDegrees >= -180 && newDegrees <= -90) {
                newDegrees = -newDegrees - 90;
            }
            // Log.i("myk","角度："+newDegrees+"");
            degrees = newDegrees;
            invalidate();
            if (mDegreesChangedListener != null) {
                mDegreesChangedListener.onDegreesChanged(newDegrees);
            }
            return true;
        }
    });

    //++监听事件，指针转动，就会把角度传出
    private DegreesChangedListener mDegreesChangedListener;

    public void setOnDegreesChangedListener(DegreesChangedListener degreesChangedListener) {
        this.mDegreesChangedListener = degreesChangedListener;
    }

    public interface DegreesChangedListener {
        void onDegreesChanged(float newDegrees);
    }

    //用抛物线函数来制造噪声，修正degrees的偏移
    private float reviseDegrees(float degrees) {
        //float newDegrees = needleHeight*((degrees-9)*(degrees-9)); //初中数学抛物线公式忘记了。。。
        float newDegrees = degrees;
        if (degrees < 10) {
            newDegrees += 5;
        } else if (degrees < 20) {
            newDegrees += 4;
        } else if (degrees < 30) {
            newDegrees += 3;
        } else if (degrees < 40) {
            newDegrees += 2;
        } else if (degrees < 50) {
            newDegrees += 1;
        } else if (degrees > 130) {
            newDegrees -= 1;
        } else if (degrees > 140) {
            newDegrees -= 2;
        } else if (degrees > 150) {
            newDegrees -= 3;
        } else if (degrees > 160) {
            newDegrees -= 4;
        } else if (degrees > 170) {
            newDegrees -= 5;
        }
        return newDegrees;
    }

    private Bitmap getScaledBitmap(@DrawableRes int sourceId, int width, int height) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), sourceId);
        if (bitmap.getWidth() == width && bitmap.getHeight() == height) {
            return bitmap;
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    private int dp2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    private int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    public DialView(Context context) {
        this(context, null);
    }

    public DialView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
