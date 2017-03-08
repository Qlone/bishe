package com.example.weina.bishe.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.service.serviceImpl.HomeService;

/**
 * Created by weina on 2017/2/28.
 */
public class GoodDetailActivity extends AppCompatActivity{
    private ImageView mImageView;
    /**
     * 标题
     */
    private TextView mTitle;
    private TextView mSales;
    private GoodsEntity mGoodsEntity =null;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail);
        initData(savedInstanceState);
        initView();

    }

    private void initData(Bundle savedInstanceState){
        Intent intent = getIntent();
        mGoodsEntity = (GoodsEntity) intent.getParcelableExtra(MainActivity.GOOD_BUNDLE);
        mHandler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case IHomeService.POSITION_MSG:{
                        mImageView.setImageBitmap(mGoodsEntity.getBitmap());
                        break;
                    }
                }
                super.handleMessage(msg);
            }
        };
        if(null == mGoodsEntity.getBitmap()) {
            HomeService.getPicture(mGoodsEntity, 0, mHandler);
        }

    }

    private void initView(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //半透明头部状态栏，底部导航栏   布局在状态栏，导航栏下方
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(0xb4000000);
                getWindow().setNavigationBarColor(0xb4000000);
            }
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        mImageView = (ImageView) findViewById(R.id.good_detail_img);
        mTitle = (TextView) findViewById(R.id.good_detail_title);
        mSales = (TextView) findViewById(R.id.good_sales);
//        mImageView.setImageBitmap(mGoodsEntity.getBitmap());

        mTitle.setText(mGoodsEntity.getTitle());
        mSales.setText(""+mGoodsEntity.getSales()+" 人付款");
    }
}
