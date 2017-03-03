package com.example.weina.bishe.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.service.serviceImpl.HomeService;

import java.util.ArrayList;

/**
 * Created by weina on 2017/2/11.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    public ArrayList<GoodsEntity> datas = null;
    public OnItemClickListener mLicense= null;
    public HomeAdapter(ArrayList<GoodsEntity> datas) {
        this.datas = datas;
    }
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
        ViewHolder vh = new ViewHolder(view);
        return vh ;
    }

    @Override
    public void onBindViewHolder(final HomeAdapter.ViewHolder viewHolder, int position) {
        viewHolder.mItemTitle.setText( datas.get(position).getTitle());
        if(null !=datas.get(position).getBitmap()) {
            viewHolder.mItemPicture.setImageBitmap(datas.get(position).getBitmap());
        }
        viewHolder.mItemSales.setText(datas.get(position).getSales()+"人付款");
        viewHolder.mItemPrice.setText("￥ "+datas.get(position).getPrice());
        if(null ==datas.get(position).getBitmap() && true !=  datas.get(position).isDownloading()){
            Log.d("搜索图片 请求id ：",""+position);
            datas.get(position).setDownloading(true);
            HomeService.getPicture(datas.get(position),position);
        }
        if(null !=datas.get(position).getStatus()) {
            switch (datas.get(position).getStatus()) {
                case "hot":
                    viewHolder.mItemTypes.setBackgroundResource(R.drawable.hot);
                    break;
                case "recommend":
                    viewHolder.mItemTypes.setBackgroundResource(R.drawable.recommand);
                    break;
            }
        }

        //若回调，则设置点击事件
        if(mLicense != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mLicense.onItemClick(viewHolder.itemView,pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mItemTitle;
        public TextView mItemPrice;
        public TextView mItemSales;
        public ImageView mItemPicture;
        public ImageView mItemTypes;
        public ViewHolder(View view){
            super(view);
            mItemTitle = (TextView) view.findViewById(R.id.item_title);
            mItemPrice = (TextView) view.findViewById(R.id.item_price);
            mItemSales = (TextView) view.findViewById(R.id.item_sales);
            mItemPicture = (ImageView)view.findViewById(R.id.item_img);
            mItemTypes = (ImageView)view.findViewById(R.id.item_type);
        }

    }

    //设置接口
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    //监听器
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mLicense = mOnItemClickListener;
    }
}
