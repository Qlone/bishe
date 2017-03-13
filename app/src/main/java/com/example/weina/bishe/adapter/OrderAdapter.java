package com.example.weina.bishe.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.entity.OrderEntity;
import com.example.weina.bishe.service.StaticString;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by weina on 2017/3/13.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private ArrayList<OrderEntity> data = null;

    public OrderAdapter(ArrayList<OrderEntity> data) {
        this.data = data;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm ss");
        holder.mCreateTime.setText("创建时间: "+sdf.format(data.get(position).getCreateTime()));
        holder.mAmount.setText("数量: "+data.get(position).getAmount());
        holder.mPrice.setText("总价 ￥"+data.get(position).getPrice()*data.get(position).getAmount());
        holder.mTitle.setText(data.get(position).getTitle());
        holder.mAddress.setText("收货地址: "+data.get(position).getAddress());
        holder.mPhone.setText("收货电话: "+data.get(position).getPhone());
        /**
         * 图片加载
         */
        int width = StaticString.GOODS_IMG_ITEM_SIZE, height = StaticString.GOODS_IMG_ITEM_SIZE;
        //Log.d("图片的宽和高",""+width+":"+height);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(data.get(position).getPicture()))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(holder.mSimpleDraweeView.getController())
                .setImageRequest(request)
                .build();
        holder.mSimpleDraweeView.setController(controller);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mCreateTime;//创建时间
        public TextView mAmount;//数量
        public TextView mPrice;//价格
        public TextView mTitle;//标题
        public TextView mAddress;//地址
        public TextView mPhone;//手机号码
        public SimpleDraweeView mSimpleDraweeView;
        public ViewHolder(View itemView) {
            super(itemView);
            mSimpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.order_item_img);
            //设置宽高比例
            mSimpleDraweeView.setAspectRatio(1f);
            //设置对其方式

            mSimpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
            mCreateTime = (TextView) itemView.findViewById(R.id.order_item_createTime);
            mAmount = (TextView) itemView.findViewById(R.id.order_item_amount);
            mPrice =(TextView) itemView.findViewById(R.id.order_item_totalPrice);
            mTitle = (TextView)itemView.findViewById(R.id.order_item_title);
            mAddress=(TextView)itemView.findViewById(R.id.order_item_address);
            mPhone =(TextView) itemView.findViewById(R.id.order_item_phone);
        }
    }

}
