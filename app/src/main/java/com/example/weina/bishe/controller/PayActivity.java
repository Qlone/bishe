package com.example.weina.bishe.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weina.bishe.R;
import com.example.weina.bishe.bean.GsonResAddOrder;
import com.example.weina.bishe.service.serviceImpl.BaseUserService;
import com.example.weina.bishe.service.serviceImpl.OrderService;
import com.example.weina.bishe.util.view.GifWaitBg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weina on 2017/3/21.
 */
public class PayActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String PAY_BUNDLE = "payBundle";
    public static final String PAY_RES = "payResBundle";
    /**
     * 订单
     */
    private List<Integer> orderIdList;
    /**
     * 按钮
     */
    private List<Button> key;
    private ImageButton deleteKey;
    //密码显示
    private List<TextView> password;
    //计算输入多少个密码
    int numberSize;
    //信息提示
    private TextView mtips;

    /**
     *控制 view 的显示
     */
    private LinearLayout mKeyBox;
    private TranslateAnimation mShowAction;
    private TranslateAnimation mHiddenAction;
    private GifWaitBg mGifWaitBg;
    //结果集
    private GsonResAddOrder mGsonResAddOrder;
    private LinearLayout mResLinear;
    private ScrollView mResScroll;
    private ImageView mResImg;
    private TextView mResTips;

    private static Handler sHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.pay_toolbar);
        SysApplication.getInstance().addActivity(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        orderIdList = (List<Integer>) intent.getIntegerArrayListExtra(PAY_BUNDLE);
        mGsonResAddOrder = (GsonResAddOrder) intent.getSerializableExtra(PAY_RES);
        initData();
        initView();
    }




    private void initData(){
        if(null == orderIdList){
            finish();
        }
        key =  new ArrayList<>();
        password = new ArrayList<>();
        numberSize=0;

        sHandler = new Handler(){
            public void handleMessage(Message msg) {
            }
        };


        mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                +1.0f);
        mHiddenAction.setDuration(500);
        mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                +1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mShowAction.setDuration(500);
    }

    private void initView(){
        /**
         * 绑定ui
         */
        mResScroll = (ScrollView) findViewById(R.id.pay_result_scroll);
        mResScroll.setVisibility(View.GONE);
        mResTips = (TextView) findViewById(R.id.pay_res_tips);
        mResImg = (ImageView) findViewById(R.id.pay_res_img);
        mResLinear = (LinearLayout) findViewById(R.id.pay_result);
        mResLinear.setVisibility(View.GONE);
        mKeyBox = (LinearLayout) findViewById(R.id.key_layout);
        mKeyBox.setAnimation(mShowAction);
        mKeyBox.setVisibility(View.VISIBLE);
        mGifWaitBg = (GifWaitBg) findViewById(R.id.pay_wait);
        mGifWaitBg.setGifGone();
        /**
         * 绑定控件
         */
        key.add((Button)findViewById(R.id.pay_key_0));
        key.add((Button)findViewById(R.id.pay_key_1));
        key.add((Button)findViewById(R.id.pay_key_2));
        key.add((Button)findViewById(R.id.pay_key_3));
        key.add((Button)findViewById(R.id.pay_key_4));
        key.add((Button)findViewById(R.id.pay_key_5));
        key.add((Button)findViewById(R.id.pay_key_6));
        key.add((Button)findViewById(R.id.pay_key_7));
        key.add((Button)findViewById(R.id.pay_key_8));
        key.add((Button)findViewById(R.id.pay_key_9));
        for(Button button:key){
            button.setOnClickListener(this);
        }
        password.add((TextView)findViewById(R.id.pay_password1));
        password.add((TextView)findViewById(R.id.pay_password2));
        password.add((TextView)findViewById(R.id.pay_password3));
        password.add((TextView)findViewById(R.id.pay_password4));
        password.add((TextView)findViewById(R.id.pay_password5));
        password.add((TextView)findViewById(R.id.pay_password6));
        //删除按钮
        deleteKey = (ImageButton) findViewById(R.id.pay_key_delete);
        deleteKey.setOnClickListener(this);
        //提示按钮
        mtips = (TextView) findViewById(R.id.pay_tips);
        for(TextView textView:password){
            textView.setText("");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pay_key_0:{
                if(numberSize <6 && numberSize>=0) {
                    password.get(numberSize).setText("0");
                    numberSize++;
                }
                break;
            }
            case R.id.pay_key_1:{
                if(numberSize <6 && numberSize>=0) {
                    password.get(numberSize).setText("1");
                    numberSize++;
                }
                break;
            }
            case R.id.pay_key_2:{
                if(numberSize <6 && numberSize>=0) {
                    password.get(numberSize).setText("2");
                    numberSize++;
                }
                break;
            }
            case R.id.pay_key_3:{
                if(numberSize <6 && numberSize>=0) {
                    password.get(numberSize).setText("3");
                    numberSize++;
                }
                break;
            }
            case R.id.pay_key_4:{
                if(numberSize <6 && numberSize>=0) {
                    password.get(numberSize).setText("4");
                    numberSize++;
                }
                break;
            }
            case R.id.pay_key_5:{
                if(numberSize <6 && numberSize>=0) {
                    password.get(numberSize).setText("5");
                    numberSize++;
                }
                break;
            }
            case R.id.pay_key_6:{
                if(numberSize <6 && numberSize>=0) {
                    password.get(numberSize).setText("6");
                    numberSize++;
                }
                break;
            }
            case R.id.pay_key_7:{
                if(numberSize <6 && numberSize>=0) {
                    password.get(numberSize).setText("7");
                    numberSize++;
                }
                break;
            }
            case R.id.pay_key_8:{
                if(numberSize <6 && numberSize>=0) {
                    password.get(numberSize).setText("8");
                    numberSize++;
                }
                break;
            }
            case R.id.pay_key_9:{
                if(numberSize <6 && numberSize>=0) {
                    password.get(numberSize).setText("9");
                    numberSize++;
                }
                break;
            }
            case R.id.pay_key_delete:{
                if(numberSize <=6 && numberSize>0) {
                    password.get(numberSize-1).setText(null);
                    mtips.setText("");
                    numberSize--;
                }
                break;
            }
        }
        if(numberSize ==6) {
            if (checkPassword()) {
                mKeyBox.setAnimation(mHiddenAction);
                mKeyBox.setVisibility(View.GONE);
                mGifWaitBg.setGifShow();
                OrderService.pay(orderIdList, new OrderService.OrderCallBack() {
                    @Override
                    public void callBack(final String datas) {
                        PayActivity.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mGifWaitBg.setGifGone();
                                String msg="";
                                if ("true".equals(datas)) {
                                    Toast.makeText(PayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                }else if("noMoney".equals(datas)){
                                    Toast.makeText(PayActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
                                    msg = ",余额不足";
                                }
                                else {
                                    Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                                }
                                showResult("true".equals(datas),msg);
                            }
                        });
                    }

                    @Override
                    public void error(String msg) {
                        PayActivity.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mGifWaitBg.setGifGone();
                                Toast.makeText(PayActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }else{
                mtips.setText("密码错误");
            }
        }
    }
    //显示结果
    private void showResult(boolean payRes,String msg){
        mResScroll.setVisibility(View.VISIBLE);
        mResLinear.setVisibility(View.VISIBLE);
        if(payRes){
            mResImg.setImageResource(R.drawable.pay_success);
            mResTips.setText("支付成功了呢");
        }else{
            mResImg.setImageResource(R.drawable.pay_fail);
            mResTips.setText("支付失败"+msg);
        }
        if(null!=mGsonResAddOrder){
            for(int i =0;i<mGsonResAddOrder.getOrderName().size();i++){
                View view = View.inflate(PayActivity.this, R.layout.pay_item, null);
                TextView orderName = (TextView) view.findViewById(R.id.pay_item_order);
                TextView res = (TextView) view.findViewById(R.id.pay_item_res);
                orderName.setText(mGsonResAddOrder.getOrderName().get(i));
                res.setText(mGsonResAddOrder.getMsg().get(i));
                mResLinear.addView(view);
                Log.d("payRes","yep"+mGsonResAddOrder.getOrderName().get(i)+"  "+mGsonResAddOrder.getOrderName().get(i));
            }
        }else{
            Log.d("payRes","null");
        }
    }

    public static Handler getHandler() {
        return sHandler;
    }


    private boolean checkPassword(){
        if(BaseUserService.getGsonLogin().isBoolean()){
            StringBuffer psw = new StringBuffer();
            for(TextView textView:password){
                psw.append(textView.getText().toString());
            }
            try {
                int iPsw = Integer.parseInt(psw.toString());
                if(iPsw == BaseUserService.getGsonLogin().getUserEntity().getPayPassword()){
                    return true;
                }else {
                    return false;
                }
            }catch (Exception e){
                return false;
            }
        }else{
            return false;
        }
    }

    //打开支付页面
    public static void openPay(Context context, ArrayList<Integer> list, GsonResAddOrder gsonResAddOrder){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList(PAY_BUNDLE,list);
        if(null!=gsonResAddOrder){
            bundle.putSerializable(PAY_RES,gsonResAddOrder);
        }
        intent.putExtras(bundle);
        intent.setClass(context,PayActivity.class);
        context.startActivity(intent);
    }
}
