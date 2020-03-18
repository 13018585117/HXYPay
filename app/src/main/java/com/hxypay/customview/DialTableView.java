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
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.hxypay.R;

public class DialTableView extends View {

    private Bitmap btmNormalDial, btmCheckedDial, btmNeedle;
    private int needleWidth, needleHeight;   //指针精心化控制，宽高比为10:1。指针宽度是半径的2分之1
    private Paint mPaint;
    private Path maskPath; //遮罩层路径
    private RectF maskRectF; //遮罩层画弧度需要的方形区域
    private float radius = 0;  //表盘半径
    private float degrees = 0;  //指针角度

    /**
     * @return 角度的浮点值  0-180之间
     */
    public float getDegrees() {
        return degrees;
    }

    /**
     * @param degrees 角度的浮点值  0-180之间
     */
    public void setDegrees(@FloatRange(from = 0, to = 180) float degrees) {
        this.degrees = degrees;
    }

    /**
     * @return 角度的整数值 0-180之间
     */
    public int getDegreesInt() {
        return (int) degrees;
    }

    /**
     * @param degrees 角度的整数值 0-180之间
     */
    public void setDegreesInt(@IntRange(from = 0, to = 180) int degrees) {
        this.degrees = degrees;
    }

    /**
     * @return 角度的百分比 0-100之间
     */
    public int getDegreesPer() {
        return (int) (degrees * 100 / 180);
    }

    /**
     * @param percent 角度的百分比 0-100之间
     */
    public void setDegreesPer(@IntRange(from = 0, to = 100) int percent) {
        this.degrees = percent * 180.0f / 100;
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

        btmNeedle = getScaledBitmap(R.drawable.needle, needleWidth, needleHeight);
        btmNormalDial = getScaledBitmap(R.drawable.dial_normal, (int) (2 * radius), (int) radius);
        btmCheckedDial = getScaledBitmap(R.drawable.dial_checked, (int) (2 * radius), (int) radius);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);

        maskPath = new Path();
        maskRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算运算变量
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        float rotateX = width / 2; //旋转中心X
        float rotateY = height - needleHeight / 2; //指针旋转中心Y有一些偏移

        canvas.drawColor(Color.WHITE);

        //画白色
        canvas.save();
        canvas.drawBitmap(btmNormalDial, 0, 0, mPaint);
        canvas.restore();

        //画红色
        canvas.save();
        maskPath.reset();
        maskPath.moveTo(width / 2, height);
        maskPath.lineTo(0, height);
        maskRectF.set(0, 0, width, 2 * height);
        maskPath.arcTo(maskRectF, 180, degrees);
        maskPath.close();
        canvas.clipPath(maskPath);
        canvas.drawBitmap(btmCheckedDial, 0, 0, mPaint);
        canvas.restore();

        //画指针
        canvas.save();
        canvas.rotate(degrees, rotateX, rotateY);
        canvas.drawBitmap(btmNeedle, width / 4 + needleHeight / 2, height - needleHeight, mPaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        if (event.getY() >= radius) {
            return true;
        }
        return gestureDetector.onTouchEvent(event);
    }

    GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
//            //获取点击点计算角度，然后开始动画
//            double pointX = e.getX() - radius;  //转成圆形计算
//            double pointY = e.getY() - radius;  //同上
//            double angel = Math.atan2(pointX, pointY);    //根据斜率求弧度，tan函数的逆运算
//            int newDegrees = (int) (180 * angel / Math.PI);
//            if (newDegrees >= 90 && newDegrees <= 180) {
//                newDegrees = 180 - newDegrees + 90;
//            } else if (newDegrees >= -180 && newDegrees <= -90) {
//                newDegrees = -newDegrees - 90;
//            }
//            //Log.i("myk","角度："+newDegrees+"");
//            degrees = newDegrees;
//            if (mDegreesChangedListener != null) {
//                mDegreesChangedListener.onDegreesChanged(degrees);
//            }
//            invalidate();
//            return true;
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

            float oldDegrees = getDegrees();    //先获取旧角度
            if (newDegrees < oldDegrees - 10 || newDegrees > oldDegrees + 10) {     //若当前手指触摸范围不在旧角度之附近
                return false;
            } else {
                return true;
            }

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
            //Log.i("myk","角度："+newDegrees+"");
            degrees = newDegrees;
            invalidate();
            if (mDegreesChangedListener != null) {
                mDegreesChangedListener.onDegreesChanged(newDegrees);
            }
            return true;
        }
    });

    //表盘指针事件监听
    private DialView.DegreesChangedListener mDegreesChangedListener;

    public void setOnDegreesChangedListener(DialView.DegreesChangedListener degreesChangedListener) {
        this.mDegreesChangedListener = degreesChangedListener;
    }

    public interface DegreesChangedListener {
        void onDegreesChanged(float newDegrees);
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

    public DialTableView(Context context) {
        this(context, null);
    }

    public DialTableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
