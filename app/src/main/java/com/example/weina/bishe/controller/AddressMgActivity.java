package com.example.weina.bishe.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.example.weina.bishe.R;
import com.example.weina.bishe.adapter.AddressAdapter;
import com.example.weina.bishe.entity.AddressEntity;
import com.example.weina.bishe.service.IAddressService;
import com.example.weina.bishe.service.serviceImpl.AddressService;
import com.example.weina.bishe.util.SpacesItemDecoration;
import com.example.weina.bishe.util.view.AddAddressDialogView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;

/**
 * Created by weina on 2017/3/17.
 */
public class AddressMgActivity extends AppCompatActivity {
    private ArrayList<AddressEntity> data;
    private AddressAdapter mAddressAdapter;
    private XRecyclerView mXRecyclerView;
    private static Handler sHandler;

    /**
     * 确认  添加按钮
     */
    private Button mAddAddress;
    private Button mConfirmAddress;
    private AddAddressDialogView addAddressDialogView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_manage_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        initdata();
        initView();
    }
    private void initdata(){//初始化数据

        addAddressDialogView = new AddAddressDialogView(AddressMgActivity.this);
        data = new ArrayList<>();
        mAddressAdapter = new AddressAdapter(data);
        sHandler = new Handler(){
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case IAddressService.ADDRESS_UPDATA_OVER:{
                        updataOver();
                        break;
                    }
                    case IAddressService.ADDRESS_ADD_SUCCESS:{
                        addAddressDialogView.onBackPressed();
                        AddressService.getAddressList(data);//刷新数据
                        break;
                    }
                    case IAddressService.ADDRESS_ADD_FAIL:{
                        addAddressDialogView.setMsg(" 添加地址失败  ");
                        break;
                    }
                    case IAddressService.ADDRESS_ADD_ELSE:{
                        addAddressDialogView.setMsg(" 服务器错误 ");
                        break;
                    }
                }
            }
        };
    }

    private void initView(){//初始化界面
        mXRecyclerView = (XRecyclerView) findViewById(R.id.address_recycler);
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
                AddressService.getAddressList(data);
            }

            @Override
            public void onLoadMore() {
                mXRecyclerView.loadMoreComplete();
            }
        });
        mXRecyclerView.setAdapter(mAddressAdapter);
        //第一次进入刷新
        AddressService.getAddressList(data);

        mAddAddress = (Button) findViewById(R.id.view_address_addAddress);
        mAddAddress.setOnClickListener(new clickListener());
    }

    private class clickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.view_address_addAddress:{
                    addAddressDialogView.show();
                    addAddressDialogView.setMsg("");
                    break;
                }
            }
        }
    }

    public static Handler getHandler() {
        return sHandler;
    }
    private void updataOver(){
        mAddressAdapter.notifyDataSetChanged();
        mXRecyclerView.refreshComplete();
    }
}
