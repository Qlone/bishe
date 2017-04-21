package com.example.weina.bishe.util.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weina.bishe.R;
import com.example.weina.bishe.controller.AddressMgActivity;
import com.example.weina.bishe.controller.MainActivity;
import com.example.weina.bishe.controller.OrderMgActivity;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.service.serviceImpl.BaseUserService;

/**
 * Created by weina on 2017/3/20.
 */
public class HomeFragment3 extends Fragment implements View.OnClickListener{
    private static View mView;
    //绑定 菜单
    private TextView mMenuAddress;
    private TextView mMenuOrder;
    private TextView mBalance;
    private TextView mChangePay;
    private TextView mChangePsw;
    ChangePsw changePsw;
    private BaseUserService.ButtonBackCall mButtonBackCall;
    private static Handler sHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(null == mView) {
            mView = inflater.inflate(R.layout.main_fragment3, null);
            initData();
            initView();
        }
        ViewGroup viewGroup = (ViewGroup) mView.getParent();
        if(BaseUserService.getInstatnce().checkUser(mView.getContext(), mButtonBackCall)) {
            //检测是否登录
            reinit();
        }else {
            onDestroy();
        }
        if(null != viewGroup){
            viewGroup.removeView(viewGroup);
        }
        return  mView;
    }

    /**
     * 初始化数据
     */
    private void initData(){
        if(null == sHandler) {
            sHandler = new Handler();
        }
        if(null == changePsw) {
            changePsw = new ChangePsw(getContext());
        }
        changePsw.setChangePswCallBack(new ChangePsw.ChangePswCallBack() {
            @Override
            public void success() {
                showMsg("修改成功");
            }

            @Override
            public void fail() {
                showMsg("修改失败");
            }
        });
        mButtonBackCall = new BaseUserService.ButtonBackCall() {
            @Override
            public void doConfirm() {
                reinit();
            }

            @Override
            public void doCancel() {
                MainActivity.getHandle().sendEmptyMessage(IHomeService.HOST_TAB_ID1);
            }
        };
    }

    /**
     * 初始化view
     */
    private void initView(){
        mChangePay = (TextView) mView.findViewById(R.id.person_text_changPay);
        mChangePsw = (TextView) mView.findViewById(R.id.person_text_changPsw);
        mChangePay.setOnClickListener(this);
        mChangePsw.setOnClickListener(this);
        mMenuAddress = (TextView) mView.findViewById(R.id.person_address_text);
        mMenuOrder = (TextView) mView.findViewById(R.id.person_order_text);
        mBalance = (TextView) mView.findViewById(R.id.person_balance);
        mMenuAddress.setOnClickListener(this);
        mMenuOrder.setOnClickListener(this);

        mBalance.setText(""+0);
    }

    @Override
    public void onClick(View view) {
        if(!BaseUserService.getInstatnce().checkUser(mView.getContext(), mButtonBackCall)) {
            //检测是否登录
            return;
        }
        switch (view.getId()){
            case R.id.person_address_text:{
                Intent intent = new Intent();
                intent.setClass(getContext(), AddressMgActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.person_order_text:{
                Intent intent = new Intent();
                intent.setClass(getContext(),OrderMgActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.person_text_changPay:{
                changePsw.setPayPassword(true);
                changePsw.show();
                break;
            }
            case R.id.person_text_changPsw:{
                changePsw.setPayPassword(false);
                changePsw.show();
            }
        }
    }
    public void reinit(){
        BaseUserService.getInstatnce().update(getContext());
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                mBalance.setText(""+BaseUserService.getGsonLogin().getUserEntity().getBalance());
            }
        });
    }
    public void showMsg(final String msg){
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
