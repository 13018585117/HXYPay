package com.hxypay.customview;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hxypay.R;

public class SavePop extends PopupWindow {

    private Button mBtn1, cancelBtn;

    public SavePop(Context context, final SaveListener iCardSettingPopListener) {
        final View view = LayoutInflater.from(context).inflate(R.layout.save_pop, null);
        mBtn1 = (Button) view.findViewById(R.id.btn1);
        cancelBtn = (Button) view.findViewById(R.id.cancel_btn);

        mBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iCardSettingPopListener.saveCard(SavePop.this);
                dismiss();
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


    public interface SaveListener {
        void saveCard(SavePop dialog);

    }
}
