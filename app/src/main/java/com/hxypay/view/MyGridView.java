package com.hxypay.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.GridView;

public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//public MyGridView(Context context, AttributeSet attrs, int  //defStyleAttr, int defStyleRes) {
//    super(context, attrs, defStyleAttr, defStyleRes);
//  }

    /**
     * 重写测量GridView的内容空间（有多少数据内容）
     * @param widthMeasureSpec 占用宽度
     * @param heightMeasureSpec 占用高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // expandSpec：拓展空间，其中MeasureSpec.AT_MOST为“最大模式”
        // AT_MOST：最大模式，比喻为布局里的match_parent
        // EXACTLY：精确模式，比喻为布局里的"50dp"
        // UNSPECIFIED：未指定模式，比喻为布局里的wrap_content
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 重写事件分发：因为两个都是ViewGroup，这个方法不知道是否可行，
     * 有兴趣的朋友自行脑补
     */
//  @Override
//  public boolean dispatchTouchEvent(MotionEvent ev) {
//    if (ev.getAction() == MotionEvent.ACTION_MOVE){
//      //返回true直接结束当前事件消费
//      return true;
//    }
//    return super.dispatchTouchEvent(ev);
//  }

    /**
     * 如果是嵌套在ScollView中的，则这样写
     * 设置是否有ScrollBar，当要在ScollView中显示时，应当设置为
     * false。 默认为 true
     */
//  boolean haveScrollbars = false;
//  public void setHaveScrollbar(boolean haveScrollbar) {
//    this.haveScrollbar = haveScrollbar;
//  }
//  @Override
//  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//    if (haveScrollbars == false) {
//      int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
//      super.onMeasure(widthMeasureSpec, expandSpec);
//    } else {
//      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }
//  }

}
