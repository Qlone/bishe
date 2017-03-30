package com.example.weina.bishe.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weina.bishe.R;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.service.serviceImpl.BaseUserService;
import com.example.weina.bishe.service.serviceImpl.CommentService;
import com.example.weina.bishe.service.serviceImpl.GoodsService;
import com.example.weina.bishe.service.serviceImpl.OrderService;
import com.example.weina.bishe.util.view.AddressConfirmView;
import com.example.weina.bishe.util.view.ChooseNumberView;
import com.example.weina.bishe.util.view.GifWaitBg;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weina on 2017/2/28.
 */
public class GoodDetailActivity extends AppCompatActivity{
    public final static int UPDATA_OVER = 1001;

    /**
     * 地址选择框
     */
    private AddressConfirmView mAddressConfirmView;
    /**
     * 标题
     */
    private TextView mTitle;
    private TextView mSales;
    private TextView mPrice;
    private TextView mGoodSales;

    private GoodsEntity mGoodsEntity =null;
    private List<GoodsEntity> mGoodsEntities;
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
    /**
     * 是否直接购买
     */
    boolean isBuy;
    //句柄
    private static Handler mHandler;
    // 分数，评论人数
    private double mScore;
    private long mCommentCount;
    private Button mCommentBtn;
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
        if(mGifWaitBg.getGifVisibility() == View.VISIBLE){
            //do nothing just wait
        }else if(mChoose.getVisibility()!= View.GONE){
            mbg.setVisibility(View.GONE);
            mChoose.startAnimation(mHiddenAction);
            mChoose.setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
        if(null != mAddressConfirmView) {
            mAddressConfirmView.reinit();
        }
    }
    private void initData(Bundle savedInstanceState){
        isBuy =false;
        mGoodsEntities = new ArrayList<>();
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
        mHandler = new  Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case UPDATA_OVER:{
                        reinit();
                        break;
                    }
                }
            }
        };

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
        mCommentBtn =(Button) findViewById(R.id.good_detail_comment);
        mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CommentListActivity.COMMENTLIS_BUNDLE,mGoodsEntity.getGoodsId());
                intent.putExtras(bundle);
                intent.setClass(GoodDetailActivity.this,CommentListActivity.class);
                startActivity(intent);
            }
        });


        mTitle = (TextView) findViewById(R.id.good_detail_title);
        mSales = (TextView) findViewById(R.id.good_sales);
        mPrice = (TextView) findViewById(R.id.good_price);
        mGoodSales = (TextView) findViewById(R.id.good_goodsales);

        mSimpleDraweeView = (SimpleDraweeView) findViewById(R.id.good_detail_img);

        //设置对其方式

        mSimpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);

        //设置
        mbg = (TextView) findViewById(R.id.good_detail_bg);
        mChoose = (LinearLayout) findViewById(R.id.good_detail_confirm);
        mCartButton = (Button) findViewById(R.id.good_detail_bt_cart);

        mCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isBuy = false;
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
                isBuy = true;
                if(mChoose.getVisibility()== View.GONE){
                    mbg.setVisibility(View.VISIBLE);
                    mChoose.startAnimation(mShowAction);
                    mChoose.setVisibility(View.VISIBLE);
                }
            }
        });

        //设置选择数量
        mChooseNumberView = (ChooseNumberView) findViewById(R.id.good_detail_inner_choose);
        mStockText = (TextView) findViewById(R.id.view_choose_text_stock);
        //设置
        mConfirmButton = (Button) findViewById(R.id.good_detail_btn_confirm);
        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseUserService.getInstatnce().checkUser(GoodDetailActivity.this,mButtonBackCall)) {
                    if(!isBuy){
                        cart();
                    }else {
                        buy();
                    }
                }
            }
        });
        mGifWaitBg = (GifWaitBg) findViewById(R.id.good_detail_gifView);
        //获取数据
        GoodsService.getGoodsDetail(mGoodsEntity.getGoodsId(),mGoodsEntities);
    }

    private void existGoods(){
        Toast.makeText(this,"订单已经存在，不要重复购买 ",Toast.LENGTH_SHORT).show();
    }

    private void errorMsgShow(){
        Toast.makeText(this,"创建 订单失败 请刷新后重试 ",Toast.LENGTH_SHORT).show();
    }
    private void successShow(){
        Toast.makeText(this,"购买成功 ",Toast.LENGTH_SHORT).show();
    }

    //重新赋值
    private void reinit(){
        if(mGoodsEntities.size()>0) {
            mTitle.setText(mGoodsEntities.get(0).getTitle());
            mSales.setText("" + mGoodsEntities.get(0).getViews() + " 人付款");
            mGoodSales.setText("" + mGoodsEntities.get(0).getSales() + " 件售出");
            mSimpleDraweeView.setImageURI(mGoodsEntities.get(0).getPicture());
            mChooseNumberView.setMaxNumber(mGoodsEntities.get(0).getStock());
            mStockText.setText("库存数量: " + mGoodsEntities.get(0).getStock());
            mPrice.setText("￥ " + mGoodsEntities.get(0).getPrice());
            getScore();
        }
    }

    public static Handler getmHandler() {
        return mHandler;
    }


    private void cart(){
        mGifWaitBg.setGifShow();//显示等待条
        OrderService.addOrder(BaseUserService.getGsonLogin().getUserEntity().getUserId(), 0, mGoodsEntity.getGoodsId(), mChooseNumberView.getNumber(), new OrderService.OrderCallBack() {
            @Override
            public void callBack(final String data) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if ("success".equals(data)) {
                            successShow();
                        }else if ("exists".equals(data)) {
                            existGoods();
                        } else {
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
    private void buy(){
        mAddressConfirmView = new AddressConfirmView(GoodDetailActivity.this,mChooseNumberView.getNumber(),mGoodsEntity.getPrice()*mChooseNumberView.getNumber());
        mAddressConfirmView.setAddressCallBack(new AddressConfirmView.AddressCallBack() {
            @Override
            public void confirm(int addressId) {
                if(addressId>0){
                    mGifWaitBg.setGifShow();
                    mAddressConfirmView.onBackPressed();//关闭对话框
                    OrderService.addOrderNoCart(BaseUserService.getGsonLogin().getUserEntity().getUserId(), addressId, mGoodsEntity.getGoodsId(), mChooseNumberView.getNumber(), new OrderService.OrderCallBack() {
                        @Override
                        public void callBack(final String data) {
                            GoodDetailActivity.getmHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    mGifWaitBg.setGifGone();
                                    if(null != data) {
                                        try {//订单生成完毕 ，开始支付
                                            int orderId = Integer.parseInt(data);
                                            ArrayList<Integer> list = new ArrayList<>();
                                            list.add(orderId);
                                            PayActivity.openPay(GoodDetailActivity.this, list);

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            GoodDetailActivity.getmHandler().post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(GoodDetailActivity.this, "请稍后重试", Toast.LENGTH_SHORT).show();
                                                    mGifWaitBg.setGifGone();
                                                }
                                            });
                                        }
                                    }
                                }
                            });

                        }

                        @Override
                        public void error(String msg) {
                            GoodDetailActivity.getmHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(GoodDetailActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                                    mGifWaitBg.setGifGone();
                                }
                            });
                        }
                    });
                }else {
                    Toast.makeText(GoodDetailActivity.this,"请确认收货地址",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void change() {
                Intent intent = new Intent();
                intent.setClass(GoodDetailActivity.this, AddressMgActivity.class);
                startActivity(intent);
            }
        });
        mAddressConfirmView.show();
    }

    private void getScore(){
        CommentService.getScore(mGoodsEntity.getGoodsId(), new CommentService.CommentServiceCallBack() {
            @Override
            public void onCallBack(String datas) {
                if(null!=datas&&!"".equals(datas)){
                    mScore = Double.parseDouble(datas);
                }else {
                    mScore =0;
                }
                getCommentCount();
            }

            @Override
            public void onError() {

            }
        });
    }
    private void getCommentCount(){
        CommentService.getCommentCount(mGoodsEntity.getGoodsId(), new CommentService.CommentServiceCallBack() {
            @Override
            public void onCallBack(String datas) {
                if(null!=datas&&!"".equals(datas)){
                    mCommentCount =Long.parseLong(datas);
                }else {
                    mCommentCount=0;
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCommentBtn.setText(mScore+" 分 "+"查看所有评论("+mCommentCount+")");
                    }
                });
            }

            @Override
            public void onError() {

            }
        });
    }

}
