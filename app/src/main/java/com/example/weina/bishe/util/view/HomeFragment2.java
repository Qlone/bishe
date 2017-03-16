package com.example.weina.bishe.util.view;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.adapter.OrderAdapter;
import com.example.weina.bishe.controller.MainActivity;
import com.example.weina.bishe.entity.OrderEntity;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.service.IOrderService;
import com.example.weina.bishe.service.serviceImpl.BaseUserService;
import com.example.weina.bishe.service.serviceImpl.OrderService;
import com.example.weina.bishe.util.SpacesItemDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

/**
 * Created by weina on 2017/3/8.
 */
public class HomeFragment2 extends Fragment {
    private View mView;
    private XRecyclerView mXRecyclerView;
    private OrderAdapter mOrderAdapter;
    private ArrayList<OrderEntity> data;
    private int MAX_CART = 10000;//购物车最大量
    private BaseUserService.ButtonBackCall mButtonBackCall;
    private OrderAdapter.ChangeOrderCallBack mChangeOrderCallBack;
    private TextView mTotalMoney;
    /**
     *  选定框
     */
    private boolean isChooseAll =false;
    private Button mChooseButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null == mView) {
            mView = inflater.inflate(R.layout.main_fragment2, null);
            initData();
            initView(mView);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误
        ViewGroup viewGroup = (ViewGroup) mView.getParent();
        if(null != viewGroup){
            viewGroup.removeView(viewGroup);
        }
        if(BaseUserService.getInstatnce().checkUser(mView.getContext(), mButtonBackCall)) {
            OrderService.getOrder(BaseUserService.getGsonLogin().getUserEntity().getUserId(), IOrderService.ORDER_STATUS_CART, 1, MAX_CART, data);
        }else {
            onDestroy();
        }

        return  mView;

    }
    public void initData(){
        if(null == data){
            data = new ArrayList<>();
        }
        if(null == mOrderAdapter){
            mOrderAdapter = new OrderAdapter(data);
        }
        mButtonBackCall = new BaseUserService.ButtonBackCall() {
            @Override
            public void doConfirm() {
                OrderService.getOrder(BaseUserService.getGsonLogin().getUserEntity().getUserId(), IOrderService.ORDER_STATUS_CART, 1, MAX_CART, data);
            }

            @Override
            public void doCancel() {
                MainActivity.getHandle().sendEmptyMessage(IHomeService.HOST_TAB_ID1);
            }
        };
        mChangeOrderCallBack = new OrderAdapter.ChangeOrderCallBack() {
            @Override
            public void saveAmount(int orderId,int amount) {
                if(BaseUserService.getInstatnce().checkUser(mView.getContext(),mButtonBackCall)) {
                    OrderService.updataOrderNumber(BaseUserService.getGsonLogin().getUserEntity().getUserId(),orderId,amount);
                }
            }

            @Override
            public void deleteOrder(int orderId,int position) {
                if(BaseUserService.getInstatnce().checkUser(mView.getContext(),mButtonBackCall)) {
                    OrderService.deleteOrder(BaseUserService.getGsonLogin().getUserEntity().getUserId(),orderId,position);
                }
            }

            @Override
            public void changeChoose() {
                updateOver();
                mChooseButton.setBackgroundResource(R.drawable.choose_button);//撤销全选
            }
        };
    }

    public void initView(View view){
        mTotalMoney = (TextView) view.findViewById(R.id.order_buttom_text_text);
        mXRecyclerView = (XRecyclerView) view.findViewById(R.id.order_recycle);
        mXRecyclerView.setLayoutManager( new LinearLayoutManager(view.getContext()));
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
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if(BaseUserService.getInstatnce().checkUser(mView.getContext(),mButtonBackCall)){
                            OrderService.getOrder(BaseUserService.getGsonLogin().getUserEntity().getUserId(), IOrderService.ORDER_STATUS_CART,1,MAX_CART,data);
                        }
                    }
                });
            }

            @Override
            public void onLoadMore() {
                mXRecyclerView.loadMoreComplete();
            }
        });
        mOrderAdapter.setChangeOrderCallBack(mChangeOrderCallBack);
        mXRecyclerView.setAdapter(mOrderAdapter);

        mChooseButton = (Button) view.findViewById(R.id.order_buttom_btn_chooseAll);
        mChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChooseAll = !isChooseAll;
                if(isChooseAll) { //选择所有订单
                    mChooseButton.setBackgroundResource(R.drawable.ischoose_bg);
                }else {
                    mChooseButton.setBackgroundResource(R.drawable.choose_button);
                }
                for(OrderEntity orderEntity:data){
                    orderEntity.setChoose(isChooseAll);
                }
                updateOver();
            }
        });

    }

    public void updateOver(){
        changeTotalMoney();
        mOrderAdapter.notifyDataSetChanged();
        mXRecyclerView.refreshComplete();
    }
    public void loadOver(){
        mOrderAdapter.notifyDataSetChanged();
        mXRecyclerView.loadMoreComplete();
    }
    public void positionMsg(){
        mOrderAdapter.notifyDataSetChanged();
    }

    private double money =0.0;
    private void changeTotalMoney(){
        money=0.0;
        for(OrderEntity orderEntity:data){
            if(orderEntity.isChoose())
                money+=orderEntity.getAmount()*orderEntity.getPrice();
        }
        mTotalMoney.setText("总价: ￥ "+money);
    }

    public void updataData(int position){
        data.remove(position);
        updateOver();
    }
}
