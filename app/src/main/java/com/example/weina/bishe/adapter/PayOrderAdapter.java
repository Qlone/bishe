package com.example.weina.bishe.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.controller.GoodDetailActivity;
import com.example.weina.bishe.entity.OrderEntity;
import com.example.weina.bishe.service.IOrderService;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.Arith;
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
 * Created by weina on 2017/3/21.
 */
public class PayOrderAdapter extends RecyclerView.Adapter<PayOrderAdapter.ViewHolder>{
    private ArrayList<OrderEntity> data = null;
    private OrderButtonCallBack mOrderButtonCallBack;
    private Context mContext;
    public PayOrderAdapter(ArrayList<OrderEntity> data,Context context) {
        this.data = data;
        this.mContext = context;
    }

    /**
     * 按钮事件
     */
    public interface OrderButtonCallBack{
        void buttonLeft(Button button,String status,int position);
        void buttonRight(Button button,String stauts,int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_has_pay_item_, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm ss");
        holder.mCreateTime.setText("创建时间: "+sdf.format(data.get(position).getCreateTime()));
        if(null!=data.get(position).getPaidTime()) {
            holder.mPaidTime.setText("支付时间: " + sdf.format(data.get(position).getPaidTime()));
        }else {
            holder.mPaidTime.setText("");
        }
        holder.mAmount.setText("数量: "+data.get(position).getAmount());
        holder.mPrice.setText("总价 ￥"+ Arith.mul(data.get(position).getPrice(),data.get(position).getAmount()));
        // 标题，收货地址，收货手机，收货人，状态
        holder.mTitle.setText(data.get(position).getTitle());
        holder.mAddress.setText("地址    :"+data.get(position).getAddress());
        holder.mPhone.setText("手机    :"+data.get(position).getPhone());
        holder.mReciver.setText("收货人:"+data.get(position).getReciver());

        switch (data.get(position).getStatus()) {
            case IOrderService.ORDER_STATUS_COMMENT:{
                holder.mStatus.setText("已完成");
                break;
            }
            case IOrderService.ORDER_STATUS_NOPAY:{
                holder.mStatus.setText("未付款");
                break;
            }
            case IOrderService.ORDER_STATUS_ONWAY:{
                holder.mStatus.setText("正在发货");
                break;
            }
            case IOrderService.ORDER_STATUS_PAID:{
                holder.mStatus.setText("等待发货");
                break;
            }
            case IOrderService.ORDER_STATUS_GET:{
                holder.mStatus.setText("未评论");
                break;
            }
            default:
                holder.mStatus.setText("");
                break;
        }
        /**
         * 设置按钮
         */
        if(null != mOrderButtonCallBack){
            mOrderButtonCallBack.buttonLeft(holder.mButtonLeft,data.get(position).getStatus(),position);
            mOrderButtonCallBack.buttonRight(holder.mButtonRight,data.get(position).getStatus(),position);
        }


        /**
         * 图片加载
         */
        int width = StaticString.ORDER_IMG_SIZ, height = StaticString.ORDER_IMG_SIZ;
        //Log.d("图片的宽和高",""+width+":"+height);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(data.get(position).getPicture()))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(holder.mSimpleDraweeView.getController())
                .setImageRequest(request)
                .build();
        holder.mSimpleDraweeView.setController(controller);
        holder.mSimpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoodDetailActivity.openGoodsDeatail(mContext,data.get(position).getGoodsId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        /**
         *下面显示 创建时间 支付时间， 数量， 总价
         */
        public TextView mCreateTime;
        public TextView mPaidTime;
        public TextView mAmount;
        public TextView mPrice;

        /**
         * 标题， 订单状态， 收件地址，收件手机，收件人
         */
        public TextView mTitle;
        public TextView mStatus;
        public TextView mAddress;
        public TextView mPhone;
        public TextView mReciver;

        /**
         *两个按钮
         */
        public Button mButtonLeft;
        public Button mButtonRight;

        /**
         * 图片
         */
        public SimpleDraweeView mSimpleDraweeView;


        public ViewHolder(View itemView) {
            super(itemView);
            mCreateTime = (TextView) itemView.findViewById(R.id.pay_order_item_createTime);
            mPaidTime = (TextView) itemView.findViewById(R.id.pay_order_item_paidTime);
            mAmount = (TextView) itemView.findViewById(R.id.pay_order_item_amount);
            mPrice = (TextView) itemView.findViewById(R.id.pay_order_item_totalPrice);
            //
            mTitle = (TextView) itemView.findViewById(R.id.pay_order_item_title);
            mStatus = (TextView) itemView.findViewById(R.id.pay_order_item_status);
            mAddress = (TextView) itemView.findViewById(R.id.pay_order_item_address);
            mPhone = (TextView) itemView.findViewById(R.id.pay_order_item_phone);
            mReciver = (TextView) itemView.findViewById(R.id.pay_order_item_reciver);
            //
            mButtonLeft = (Button) itemView.findViewById(R.id.pay_order_item_btn_left);
            mButtonRight = (Button) itemView.findViewById(R.id.pay_order_item_btn_right);
            //
            mSimpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.pay_order_item_img);
            //设置宽高比例
            mSimpleDraweeView.setAspectRatio(1f);
            //设置对其方式

            mSimpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        }
    }

    public void setOrderButtonCallBack(OrderButtonCallBack orderButtonCallBack) {
        mOrderButtonCallBack = orderButtonCallBack;
    }
}
