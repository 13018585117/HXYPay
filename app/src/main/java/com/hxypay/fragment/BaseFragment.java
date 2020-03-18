package com.hxypay.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.hxypay.customview.CustomAlertDialog;

public abstract class BaseFragment extends Fragment {
    protected View mRootView;
    protected ListView mListView;
    public CustomAlertDialog mDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(attachLayoutRes(), container, false);
        initViews();
        mDialog = new CustomAlertDialog(getActivity(), "加载中...");
        return mRootView;
    }
    /**
     * 找到控件ID
     */
    protected <T extends View> T findViewById(int id) {
        if (mRootView == null) {
            return null;
        }

        return (T) mRootView.findViewById(id);
    }

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    protected abstract int attachLayoutRes();

    /**
     * 初始化视图控件
     */
    protected abstract void initViews();

    /**
     * 加载等待 <一句话功能简述> <功能详细描述>
     *
     * @param msg
     * @see [类、类#方法、类#成员]
     */
    public void showDialog(String msg) {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.setDialogShowMessage(msg);
            mDialog.setCancelable(true);
            mDialog.show();
        }
    }

    private void setMessage(String msg) {
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.setDialogShowMessage(msg);
        }
    }

    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
