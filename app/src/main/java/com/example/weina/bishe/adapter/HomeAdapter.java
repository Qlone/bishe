package com.example.weina.bishe.adapter;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.Arith;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

/**
 * Created by weina on 2017/2/11.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{
    public ArrayList<GoodsEntity> datas = null;
    public OnItemClickListener mLicense= null;
    public static Handler mHandler =null;
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

        /**
         * 图片加载
         */
        int width = StaticString.GOODS_IMG_ITEM_SIZE, height = StaticString.GOODS_IMG_ITEM_SIZE;
        //Log.d("图片的宽和高",""+width+":"+height);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(datas.get(position).getPicture()))
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        PipelineDraweeController controller = (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
                .setOldController(viewHolder.mSimpleDraweeView.getController())
                .setImageRequest(request)
                .build();
        viewHolder.mSimpleDraweeView.setController(controller);

        viewHolder.mItemSales.setText(datas.get(position).getViews()+"人付款");
        viewHolder.mItemPrice.setText("￥ "+ Arith.round(datas.get(position).getPrice(),2));
        viewHolder.mItemGoodSales.setText(datas.get(position).getSales()+"件卖出");
        if(null !=datas.get(position).getStatus()) {
            switch (datas.get(position).getStatus()) {
                case "hot":
                    viewHolder.mItemTypes.setBackgroundResource(R.drawable.hot);
                    break;
                case "recommend":
                    viewHolder.mItemTypes.setBackgroundResource(R.drawable.recommand);
                    break;
                default:
                    viewHolder.mItemTypes.setBackgroundResource(0);
            }
        } else {
            viewHolder.mItemTypes.setBackgroundResource(0);
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
        public TextView mItemGoodSales;
        public ImageView mItemPicture;
        public SimpleDraweeView mSimpleDraweeView;
        public ImageView mItemTypes;
        public ViewHolder(View view){
            super(view);
            mItemTitle = (TextView) view.findViewById(R.id.item_title);
            mItemPrice = (TextView) view.findViewById(R.id.item_price);
            mItemSales = (TextView) view.findViewById(R.id.item_sales);
//            mItemPicture = (ImageView)view.findViewById(R.id.item_img);
            mSimpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.item_img);
            mItemTypes = (ImageView)view.findViewById(R.id.item_type);
            mItemGoodSales = (TextView) view.findViewById(R.id.item_goodsales);
            //设置宽高比例
            mSimpleDraweeView.setAspectRatio(1f);
            //设置对其方式

            mSimpleDraweeView.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP);
        }

    }

    public Handler getHandler() {
        return mHandler;
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
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
