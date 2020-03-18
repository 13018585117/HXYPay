package com.hxypay.tab;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.billy.android.swipe.SmartSwipeBack;
import com.gyf.immersionbar.ImmersionBar;
import com.hxypay.App;
import com.hxypay.R;
import com.hxypay.dialogs.TiShiDialog;

/**
 * 
 * @funnction TabHost
 * @author liujian
 * 
 */
@SuppressWarnings("deprecaton")
public abstract class OTabHost extends TabActivity {

	public static android.widget.TabHost mTabHost;
	private TabWidget mTabWidget;
	public TextView mTxtExit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set theme because we do not want the shadow
		setContentView(R.layout.tab_host);
		mTabHost = getTabHost();
		mTabWidget = getTabWidget();
		ImmersionBar.with(this).statusBarDarkFont(true,0.2f).init();  //状态栏设置；
		// mTabWidget.setStripEnabled(false); // need android2.2

		prepare();

		initTabSpec();

	}

	private void initTabSpec() {

		int count = getTabItemCount();

		for (int i = 0; i < count; i++) {
			// set text view
			View tabItem = LayoutInflater.from(OTabHost.this).inflate(R.layout.tab_item, null);

			TextView tvTabItem = (TextView) tabItem.findViewById(R.id.tab_item_tv);
			setTabItemTextView(tvTabItem, i);
			// set id
			String tabItemId = getTabItemId(i);
			// set tab spec
			TabSpec tabSpec = mTabHost.newTabSpec(tabItemId);
			tabSpec.setIndicator(tabItem);
			tabSpec.setContent(getTabItemIntent(i));

			mTabHost.addTab(tabSpec);
		}

		mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

			private TiShiDialog shareDialog;

			@Override
			public void onTabChanged(String tabId) {
				switch (tabId){
					case "首页":
						break;
					case "卡包":
						break;
					case "分享":
						break;
					case "资讯":
						break;
					case "我的":
						break;
				}
			}
		});
	}

	/** 在初始化界面之前调用 */
	protected void prepare() {
		// do nothing or you override it

	}

	protected int getTabCount() {
		return mTabHost.getTabWidget().getTabCount();
	}

	/** 设置TabItem的图标和标题 */
	abstract protected void setTabItemTextView(TextView textView, int position);

	abstract protected String getTabItemId(int position);

	abstract protected Intent getTabItemIntent(int position);

	abstract protected int getTabItemCount();

	public void setCurrentTab(int index) {
		mTabHost.setCurrentTab(index);
	}

	protected void focusCurrentTab(int index) {
		mTabWidget.focusCurrentTab(index);
	}



}
