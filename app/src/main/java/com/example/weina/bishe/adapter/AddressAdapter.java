package com.example.weina.bishe.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.entity.AddressEntity;

import java.util.ArrayList;

/**
 * Created by weina on 2017/3/17.
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{
    private ArrayList<AddressEntity> data;
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mReciver.setText("  收 货 人 : "+data.get(position).getName());
        holder.mAddress.setText(" 收货地址 : "+data.get(position).getAddress());
        holder.mPhone.setText(" 收货电话 : "+data.get(position).getPhone());
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
        public ViewHolder(View itemView) {
            super(itemView);
            mAddress = (TextView) itemView.findViewById(R.id.address_item_address_text);
            mPhone = (TextView) itemView.findViewById(R.id.address_item_phone_text);
            mReciver = (TextView)itemView.findViewById(R.id.address_item_reciver_text);
            mChooseBtn = (Button) itemView.findViewById(R.id.address_item_choose_btn);
        }
    }
}
