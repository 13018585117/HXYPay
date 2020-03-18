package com.hxypay.customview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hxypay.R;

public class SharePop extends PopupWindow {

    private Button cancelBtn;
    private TextView mWxText,mWxFText;
    private TextView mQqText,mQqFText;
    public SharePop(Context context, final ISharePopListener iSharePopListener) {
        final View view = LayoutInflater.from(context).inflate(R.layout.share_pop, null);
        mWxText = (TextView) view.findViewById(R.id.text_wx_share);
        mWxFText = (TextView) view.findViewById(R.id.text_wx_friend_share);
        mQqText = (TextView) view.findViewById(R.id.text_qq_share);
        mQqFText = (TextView) view.findViewById(R.id.text_qq_friend_share);
        cancelBtn = (Button) view.findViewById(R.id.cancel_btn);

        mWxText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSharePopListener.wx(SharePop.this);
            }
        });
        mWxFText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSharePopListener.wxf(SharePop.this);
            }
        });
        mQqText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSharePopListener.qq(SharePop.this);
            }
        });
        mQqFText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iSharePopListener.qqf(SharePop.this);
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        this.setContentView(view);
        this.setOutsideTouchable(true);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.operation_anim_style);
        ColorDrawable dw = new ColorDrawable(0x66797979);
        this.setBackgroundDrawable(dw);

        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.popLayout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }


    public interface ISharePopListener {
        void wx(SharePop dialog);

        void wxf(SharePop dialog);

        void qq(SharePop dialog);

        void qqf(SharePop dialog);
    }
}
