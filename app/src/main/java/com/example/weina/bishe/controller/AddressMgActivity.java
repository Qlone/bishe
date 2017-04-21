package com.example.weina.bishe.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weina.bishe.R;
import com.example.weina.bishe.adapter.AddressAdapter;
import com.example.weina.bishe.entity.AddressEntity;
import com.example.weina.bishe.service.IAddressService;
import com.example.weina.bishe.service.serviceImpl.AddressService;
import com.example.weina.bishe.util.SpacesItemDecoration;
import com.example.weina.bishe.util.view.AddAddressDialogView;
import com.example.weina.bishe.util.view.AddressConfirmView;
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
    private AddressAdapter.onClickLisener mOnClickLisener;//删除回调
    private AddAddressDialogView addAddressDialogView;

    /**
     * 默认地址
     */
    private TextView mHeaderAddress;
    private TextView mHeaderPhone;
    private TextView mHeaderRecvicer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_manage_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.address_toolbar);
        setSupportActionBar(toolbar);
        SysApplication.getInstance().addActivity(this);
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
        mOnClickLisener = new AddressAdapter.onClickLisener() {
            @Override
            public void delete(final int position) {
                AddressService.deleteAddress(data.get(position).getAddressId(), new AddressService.AddressCallBack() {
                    @Override
                    public void success(String datas) {
                        if("true".equals(datas)){
                            AddressService.getAddressList(data);
                            AddressMgActivity.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    clearAddressStatus(data.get(position).getAddressId());
                                }
                            });
                        }else {
                            AddressMgActivity.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddressMgActivity.this," 删除失败 ",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void error() {
                        AddressMgActivity.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AddressMgActivity.this," 请稍后重试 ",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
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
        mAddressAdapter.setOnClickLisener(mOnClickLisener);
        mXRecyclerView.setAdapter(mAddressAdapter);
        //第一次进入刷新
        AddressService.getAddressList(data);

        mAddAddress = (Button) findViewById(R.id.view_address_addAddress);
        mAddAddress.setOnClickListener(new clickListener());
        //添加头
        View header =   LayoutInflater.from(this).inflate(R.layout.address_recycle_header, (ViewGroup)findViewById(android.R.id.content),false);
        mXRecyclerView.addHeaderView(header);
        /**
         * 绑定头 view
         */
        mHeaderPhone  = (TextView) header.findViewById(R.id.address_header_phone);
        mHeaderAddress = (TextView) header.findViewById(R.id.address_header_address);
        mHeaderRecvicer = (TextView)header.findViewById(R.id.address_header_recvicer);
        /**
         * 绑定view
         */
        mConfirmAddress = (Button)findViewById(R.id.view_adddress_setAddress);
        mConfirmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(AddressEntity addressEntity:data){
                    if(addressEntity.isChoose()){
                        AddressConfirmView.setAddressAndPhone(addressEntity.getAddressId(),addressEntity.getAddress(),addressEntity.getName(),addressEntity.getPhone(),AddressMgActivity.this);
                        Toast.makeText(AddressMgActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
                        getAddressStatus();//更改显示
                    }
                }
            }
        });
        getAddressStatus();//更改显示
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

    //获取 默认地址信息
    private void getAddressStatus(){
        AddressEntity addressEntity =  AddressConfirmView.getAddressAndPhone(AddressMgActivity.this);
        mHeaderAddress.setText( " 默认地址  :"+addressEntity.getAddress());
        mHeaderPhone.setText(   " 默认手机    :"+addressEntity.getPhone());
        mHeaderRecvicer.setText(" 默认收件人:"+addressEntity.getName());
    }
    //清除 默认地址
    private void clearAddressStatus(int addressId){
        AddressEntity addressEntity =  AddressConfirmView.getAddressAndPhone(AddressMgActivity.this);
        if(addressId == addressEntity.getAddressId()){
            AddressConfirmView.setAddressAndPhone(-1,"","","",AddressMgActivity.this);
        }
        getAddressStatus();
    }
}
