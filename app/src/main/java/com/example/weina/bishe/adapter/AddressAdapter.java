package com.example.weina.bishe.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.controller.AddressMgActivity;
import com.example.weina.bishe.entity.AddressEntity;
import com.example.weina.bishe.service.IAddressService;

import java.util.ArrayList;

/**
 * Created by weina on 2017/3/17.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{
    private ArrayList<AddressEntity> data;
    private onClickLisener mOnClickLisener;
    public AddressAdapter(ArrayList<AddressEntity> datas) {
        this.data = datas;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item,parent,false);
        ViewHolder vh = new ViewHolder(view);
        return vh ;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mReciver.setText("  收 货 人 : "+data.get(position).getName());
        holder.mAddress.setText(" 收货地址 : "+data.get(position).getAddress());
        holder.mPhone.setText(" 收货电话 : "+data.get(position).getPhone());
        if(null != mOnClickLisener){
            holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickLisener.delete(position);
                }
            });
        }
        //选择按钮
        if(data.get(position).isChoose()){
            holder.mChooseBtn.setBackgroundResource(R.drawable.ischoose_bg);
        }else {
            holder.mChooseBtn.setBackgroundResource(R.drawable.choose_button);
        }
        holder.mChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearChoose(holder);//清空
                holder.mChooseBtn.setBackgroundResource(R.drawable.ischoose_bg);
                data.get(position).setChoose(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mAddress;
        public TextView mPhone;
        public TextView mReciver;
        public Button mChooseBtn;
        public Button mDeleteBtn;
        public ViewHolder(View itemView) {
            super(itemView);
            mAddress = (TextView) itemView.findViewById(R.id.address_item_address_text);
            mPhone = (TextView) itemView.findViewById(R.id.address_item_phone_text);
            mReciver = (TextView)itemView.findViewById(R.id.address_item_reciver_text);
            mChooseBtn = (Button) itemView.findViewById(R.id.address_item_choose_btn);
            mDeleteBtn = (Button) itemView.findViewById(R.id.address_item_delete_btn);
        }
    }
    public interface onClickLisener{
        void delete(int position);
    }

    public void setOnClickLisener(onClickLisener onClickLisener) {
        mOnClickLisener = onClickLisener;
    }

    private void clearChoose(ViewHolder holder){
        for(AddressEntity addressEntity:data){
            addressEntity.setChoose(false);
            AddressMgActivity.getHandler().sendEmptyMessage(IAddressService.ADDRESS_UPDATA_OVER);
        }
    }
}
