package com.example.weina.bishe.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weina.bishe.R;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.service.serviceImpl.BaseUserService;
import com.example.weina.bishe.service.serviceImpl.OrderService;
import com.example.weina.bishe.util.view.ChooseNumberView;
import com.example.weina.bishe.util.view.GifWaitBg;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

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

    private SimpleDraweeView mSimpleDraweeView;

    /**
     * 选择 购买商品的属性 页面
     */
    private LinearLayout mChoose;
    private ChooseNumberView mChooseNumberView;
    private TextView mStockText;
    private Button mCartButton;
    private Button mBuyButton;
    private Button mConfirmButton;

    private TextView mbg;
    private TranslateAnimation mShowAction;
    private TranslateAnimation mHiddenAction;
    private GifWaitBg mGifWaitBg;
    private BaseUserService.ButtonBackCall mButtonBackCall;

    //句柄
    private Handler mHandler;
    /**
     *
     * 等待条
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_detail);
        initData(savedInstanceState);
        initView();

    }
    public void onBackPressed(){
        if(mChoose.getVisibility()!= View.GONE){
            mbg.setVisibility(View.GONE);
            mChoose.startAnimation(mHiddenAction);
            mChoose.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }

    private void initData(Bundle savedInstanceState){
        Intent intent = getIntent();
        mGoodsEntity = (GoodsEntity) intent.getParcelableExtra(MainActivity.GOOD_BUNDLE);
        //HomeService.getPicture(mGoodsEntity, 0, mHandler);
        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                +1.0f);
        mHiddenAction.setDuration(500);
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                +1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);

        mButtonBackCall = new BaseUserService.ButtonBackCall() {
            @Override
            public void doConfirm() {

            }

            @Override
            public void doCancel() {

            }
        };
        mHandler = new Handler(Looper.getMainLooper());

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
//        mImageView = (ImageView) findViewById(R.id.good_detail_img);
        mTitle = (TextView) findViewById(R.id.good_detail_title);
        mSales = (TextView) findViewById(R.id.good_sales);
//        mImageView.setImageBitmap(mGoodsEntity.getBitmap());

        mTitle.setText(mGoodsEntity.getTitle());
        mSales.setText(""+mGoodsEntity.getSales()+" 人付款");
        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.good_detail_img);

        //设置对其方式

        mSimpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        mSimpleDraweeView.setImageURI(mGoodsEntity.getPicture());

        //设置
        mbg = (TextView) findViewById(R.id.good_detail_bg);
        mChoose = (LinearLayout) findViewById(R.id.good_detail_confirm);
        mCartButton = (Button) findViewById(R.id.good_detail_bt_cart);

        mCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mChoose.getVisibility()== View.GONE){
                    mbg.setVisibility(View.VISIBLE);
                    mChoose.startAnimation(mShowAction);
                    mChoose.setVisibility(View.VISIBLE);
                }
            }
        });
        mBuyButton = (Button) findViewById(R.id.good_detail_bt_buy);
        mBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        //设置选择数量
        mChooseNumberView = (ChooseNumberView) findViewById(R.id.good_detail_inner_choose);
        mChooseNumberView.setMaxNumber(mGoodsEntity.getStock());
        mStockText = (TextView) findViewById(R.id.view_choose_text_stock);
        mStockText.setText("库存数量: "+mGoodsEntity.getStock());
        //设置
        mConfirmButton = (Button) findViewById(R.id.good_detail_btn_confirm);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseUserService.getInstatnce().checkUser(GoodDetailActivity.this,mButtonBackCall)) {
                    mGifWaitBg.setGifShow();//显示等待条
                    OrderService.addOrder(BaseUserService.getGsonLogin().getUserEntity().getUserId(), 0, mGoodsEntity.getGoodsId(), mChooseNumberView.getNumber(), new OrderService.OrderCallBack() {
                        @Override
                        public void callBack(final String data) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if("success".equals(data)){
                                        successShow();
                                    }else {
                                        errorMsgShow();
                                    }
                                    mGifWaitBg.setGifGone();
                                }
                            });
                        }

                        @Override
                        public void error(final String msg) {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    errorMsgShow();
                                    mGifWaitBg.setGifGone();
                                }
                            });
                        }
                    });
                }
            }
        });
        mGifWaitBg = (GifWaitBg) findViewById(R.id.good_detail_gifView);
    }


    private void errorMsgShow(){
        Toast.makeText(this,"创建 订单失败 请刷新后重试 ",Toast.LENGTH_SHORT).show();
    }
    private void successShow(){
        Toast.makeText(this,"购买成功 ",Toast.LENGTH_SHORT).show();
    }
    private void waitTwo(){
        try {
             Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
