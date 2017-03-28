package com.example.weina.bishe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.weina.bishe.R;
import com.example.weina.bishe.adapter.PayOrderAdapter;
import com.example.weina.bishe.entity.OrderEntity;
import com.example.weina.bishe.service.IOrderService;
import com.example.weina.bishe.service.serviceImpl.BaseUserService;
import com.example.weina.bishe.service.serviceImpl.OrderService;
import com.example.weina.bishe.util.SpacesItemDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

/**
 * Created by weina on 2017/3/21.
 */
public class OrderMgActivity extends AppCompatActivity implements View.OnClickListener{
    /**
     * 静态常量
     */
    public final static int UPDATA_OVER = 100;
    public final static int DELETE_OVER= 101;
    public final static int GET_OVER= 102;

    private ArrayList<OrderEntity> data;
    private PayOrderAdapter mPayOrderAdapter;
    private XRecyclerView mXRecyclerView;
    private static Handler mHandler;

    private Button mAll;//所有订单
    private Button mNotPay;//待付款订单
    private Button mNotSend;//代发货订单
    private Button mNotGet;//待收货订单
    private Button mNotComment;//待评价订单
    /**
     * 当前显示订单的内容
     */
    private String mStauts;

    /**
     * 适配器  按钮设置回调
     */
    private PayOrderAdapter.OrderButtonCallBack mOrderButtonCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_manage_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.order_toolbar);
        setSupportActionBar(toolbar);
        initData();
        initView();
    }
    @Override
    public void onResume(){
        super.onResume();
        if(null !=data && null != mStauts){
            getData();
        }
    }



    private void initData(){
        mStauts = "";
        data = new ArrayList<>();
        mPayOrderAdapter = new PayOrderAdapter(data);
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case OrderMgActivity.UPDATA_OVER: {
                        updataOver();
                        break;
                    }

                    case OrderMgActivity.DELETE_OVER:
                    case OrderMgActivity.GET_OVER:{
                        getData();
                        break;
                    }
                }
            }
        };

        mOrderButtonCallBack = new PayOrderAdapter.OrderButtonCallBack() {
            @Override
            public void buttonLeft(Button button, String status,int position) {
                switch (status){
                    case IOrderService.ORDER_STATUS_NOPAY:{
                        buttonToDelete(button,position);
                        break;
                    }
                    case IOrderService.ORDER_STATUS_PAID:{
                        buttonNothing(button);
                        break;
                    }
                    case IOrderService.ORDER_STATUS_ONWAY:{
                        buttonNothing(button);
                        break;
                    }
                    case IOrderService.ORDER_STATUS_GET:{
                        buttonToDelete(button,position);
                        break;
                    }
                    default:{
                        buttonNothing(button);
                        break;
                    }
                }
            }

            @Override
            public void buttonRight(Button button, String status,int position) {
                switch (status){
                    case IOrderService.ORDER_STATUS_NOPAY:{
                        buttonToPay(button,position);
                        break;
                    }
                    case IOrderService.ORDER_STATUS_PAID:{
                        buttonToDelete(button,position);
                        break;
                    }
                    case IOrderService.ORDER_STATUS_ONWAY:{
                        buttonGet(button,position);
                        break;
                    }
                    case IOrderService.ORDER_STATUS_GET:{
                        buttonComment(button,position);
                        break;
                    }
                    default:{
                        buttonToDelete(button,position);
                        break;
                    }
                }
            }
        };
        mPayOrderAdapter.setOrderButtonCallBack(mOrderButtonCallBack);

    }

    private void initView(){
        /**
         * 绑定 5个 按钮
         */
        mAll = (Button) findViewById(R.id.pay_order_btn_all);
        mNotPay = (Button) findViewById(R.id.pay_order_btn_notPay);
        mNotSend = (Button) findViewById(R.id.pay_order_btn_notSend);
        mNotGet = (Button) findViewById(R.id.pay_order_btn_notGet);
        mNotComment = (Button) findViewById(R.id.pay_order_btn_notComment);
        mNotGet.setOnClickListener(this);
        mAll.setOnClickListener(this);
        mNotSend.setOnClickListener(this);
        mNotComment.setOnClickListener(this);
        mNotPay.setOnClickListener(this);
        /**
         * recycleview 初始化
         */

        mXRecyclerView = (XRecyclerView) findViewById(R.id.pay_order_recycler);
        mXRecyclerView.setLayoutManager( new LinearLayoutManager(this));
        /**
         * 设置分割线
         */
        SpacesItemDecoration decoration=new SpacesItemDecoration(8);
        mXRecyclerView.addItemDecoration(decoration);
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.Pacman);

        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
               getData();
            }

            @Override
            public void onLoadMore() {
                mXRecyclerView.loadMoreComplete();
            }
        });

        mXRecyclerView.setAdapter(mPayOrderAdapter);

        //第一次获取数据
        getData();
        mAll.setBackgroundResource(0);//选中第一个

    }

    public static Handler getmHandler() {
        return mHandler;
    }

    private void updataOver(){
        mPayOrderAdapter.notifyDataSetChanged();
        mXRecyclerView.refreshComplete();
    }

    /**
     * 按钮 点击绑定监听
     * @param view
     */
    @Override
    public void onClick(View view) {
        changeButtonBg(view.getId());
        switch (view.getId()){
            case R.id.pay_order_btn_all:{
                mStauts = "";
                getData();
                break;
            }
            case R.id.pay_order_btn_notPay:{
                mStauts = IOrderService.ORDER_STATUS_NOPAY;
                getData();
                break;
            }
            case R.id.pay_order_btn_notSend:{
                mStauts = IOrderService.ORDER_STATUS_PAID;
                getData();
                break;
            }
            case R.id.pay_order_btn_notGet:{
                mStauts = IOrderService.ORDER_STATUS_ONWAY;
                getData();
                break;
            }
            case R.id.pay_order_btn_notComment:{
                mStauts = IOrderService.ORDER_STATUS_GET;
                getData();
                break;
            }

        }
    }

    private void changeButtonBg(int id){
        mAll.setBackgroundResource(R.drawable.order_stauts_button);
        mNotGet.setBackgroundResource(R.drawable.order_stauts_button);
        mNotComment.setBackgroundResource(R.drawable.order_stauts_button);
        mNotSend.setBackgroundResource(R.drawable.order_stauts_button);
        mNotPay.setBackgroundResource(R.drawable.order_stauts_button);
        if(mAll.getId() == id){
            mAll.setBackgroundResource(0);
        }else if(mNotPay.getId() == id){
            mNotPay.setBackgroundResource(0);
        }else if(mNotComment.getId() == id){
            mNotComment.setBackgroundResource(0);
        }else if(mNotSend.getId() == id){
            mNotSend.setBackgroundResource(0);
        }else if(mNotGet.getId()==id){
            mNotGet.setBackgroundResource(0);
        }
    }

    private void getData(){
        OrderService.getOrderToMg(BaseUserService.getGsonLogin().getUserEntity().getUserId(),this.mStauts,1,100,data);
    }

    //删除订单按钮
    private void buttonToDelete(final Button button, final int position){
        button.setText("删除订单");
        button.setBackgroundResource(R.drawable.order_edit_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseUserService.getGsonLogin().isBoolean()) {//检查是否合法
                    OrderService.delete(BaseUserService.getGsonLogin().getUserEntity().getUserId(), data.get(position).getOrdersId());
                }
            }
        });
    }
    private void buttonToPay(final Button button,final int position){
        button.setText("立即支付");
        button.setBackgroundResource(R.drawable.order_edit_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                ArrayList<Integer> arrayList = new ArrayList<>();
                arrayList.add(data.get(position).getOrdersId());
                bundle.putIntegerArrayList(PayActivity.PAY_BUNDLE,arrayList);
                intent.putExtras(bundle);
                intent.setClass(OrderMgActivity.this,PayActivity.class);
                startActivity(intent);
            }
        });
    }
    private void buttonNothing(Button button){
        button.setText("");
        button.setBackgroundResource(0);
        button.setOnClickListener(null);
    }
    private void buttonGet(Button button, final int position){
        button.setText("确认收货");
        button.setBackgroundResource(R.drawable.order_edit_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BaseUserService.getGsonLogin().isBoolean()) {//检查是否合法
                    OrderService.orderGet(BaseUserService.getGsonLogin().getUserEntity().getUserId(),data.get(position).getOrdersId());
                }
            }
        });
    }
    private void buttonComment(Button button,int position){
        button.setText("立即评价");
        button.setBackgroundResource(R.drawable.order_edit_btn);
        button.setOnClickListener(null);
    }

}
