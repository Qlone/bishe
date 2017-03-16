package com.example.weina.bishe.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.entity.OrderEntity;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.view.ChooseNumberView;
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
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{

    private ArrayList<OrderEntity> data = null;

    public OrderAdapter(ArrayList<OrderEntity> data) {
        this.data = data;
    }

    private ChangeOrderCallBack mChangeOrderCallBack;
    public interface ChangeOrderCallBack{
        void saveAmount(int orderId,int amount);
        void deleteOrder(int orderId,int position);
        void changeChoose();
    }
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final OrderAdapter.ViewHolder holder, final int position) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm ss");
        holder.mCreateTime.setText("创建时间: "+sdf.format(data.get(position).getCreateTime()));
        holder.mAmount.setText("数量: "+data.get(position).getAmount());
        holder.mPrice.setText("总价 ￥"+data.get(position).getPrice()*data.get(position).getAmount());
        holder.mTitle.setText(data.get(position).getTitle());

        holder.mChooseNumberView.setMaxNumber(100);
        holder.mChooseNumberView.setNumber(data.get(position).getAmount());
        holder.mSaveLayout.setVisibility(View.GONE);

        /**
         * 选择按钮
         */
        if(data.get(position).isChoose()){
            holder.isChoose.setBackgroundResource(R.drawable.ischoose_bg);
        }else {
            holder.isChoose.setBackgroundResource(R.drawable.choose_button);
        }
        //添加监听
        holder.isChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.get(position).setChoose(!data.get(position).isChoose());
                if(data.get(position).isChoose()){
                    holder.isChoose.setBackgroundResource(R.drawable.ischoose_bg);
                }else {
                    holder.isChoose.setBackgroundResource(R.drawable.choose_button);
                }
                if(null != mChangeOrderCallBack){
                    mChangeOrderCallBack.changeChoose();
                }
            }
        });
        holder.mEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.get(position).setEdit(true);
                holder.mSaveLayout.setVisibility(View.VISIBLE);
                holder.mChooseNumberView.setNumber(data.get(position).getAmount());
            }
        });
        holder.mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.get(position).setEdit(false);
                holder.mSaveLayout.setVisibility(View.GONE);
                if(null != mChangeOrderCallBack){
                    mChangeOrderCallBack.saveAmount(data.get(position).getOrdersId(),holder.mChooseNumberView.getNumber());
                }
            }
        });
        holder.mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != mChangeOrderCallBack) {
                    mChangeOrderCallBack.deleteOrder(data.get(position).getOrdersId(),position);
                }
            }
        });

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
        public SimpleDraweeView mSimpleDraweeView;
        public Button isChoose;

        //编辑按钮
        public Button mEditBtn;
        //编辑页面
        public LinearLayout mSaveLayout;
        public ChooseNumberView mChooseNumberView;
        public Button mSaveBtn;
        public Button mDeleteBtn;

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

            isChoose = (Button) itemView.findViewById(R.id.order_item_btn_choose);


            mEditBtn = (Button) itemView.findViewById(R.id.order_item_edit_btn);
            mSaveBtn = (Button) itemView.findViewById(R.id.order_item_save_btn);
            mSaveLayout = (LinearLayout)itemView.findViewById(R.id.order_item_save_layout);
            mChooseNumberView = (ChooseNumberView)itemView.findViewById(R.id.order_item_choose_number);
            mDeleteBtn = (Button) itemView.findViewById(R.id.order_item_delete_btn);
        }
    }

    public ChangeOrderCallBack getChangeOrderCallBack() {
        return mChangeOrderCallBack;
    }

    public void setChangeOrderCallBack(ChangeOrderCallBack changeOrderCallBack) {
        mChangeOrderCallBack = changeOrderCallBack;
    }
}
