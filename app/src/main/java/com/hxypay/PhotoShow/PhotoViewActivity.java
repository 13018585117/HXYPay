package com.hxypay.PhotoShow;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hxypay.R;
import com.hxypay.util.MediaScanner;

import java.io.Serializable;
import java.util.List;

public class PhotoViewActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private PhotoViewPager mViewPager;
    private int currentPosition;
    private MyImageAdapter adapter;
    private TextView mTvImageCount;
    private List<String> imgs;
    private ImageView iv_back;
    private MediaScanner mediaScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView() {
        mediaScanner = new MediaScanner(this);
        mViewPager =  findViewById(R.id.view_pager_photo);
        mTvImageCount = findViewById(R.id.tv_image_count);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

    }

    private void initData() {
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("currentPosition", 0);
        imgs = (List<String>) intent.getSerializableExtra("imgs");
        adapter = new MyImageAdapter(imgs, this,mediaScanner,findViewById(R.id.layout));
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(currentPosition, false);
        mTvImageCount.setText(currentPosition + 1 + "/" + imgs.size());
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPosition = position;
                mTvImageCount.setText(currentPosition + 1 + "/" + imgs.size());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(this, "长按了", Toast.LENGTH_SHORT).show();

        return true;
    }
}