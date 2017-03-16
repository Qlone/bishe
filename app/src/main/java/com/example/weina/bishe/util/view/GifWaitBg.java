package com.example.weina.bishe.util.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.weina.bishe.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by weina on 2017/3/14.
 */
public class GifWaitBg extends RelativeLayout {
    private RelativeLayout mGifLayout;
    private SimpleDraweeView drawview;
    private Context mContext;


    public GifWaitBg(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.mContext =context;
        LayoutInflater.from(context).inflate(R.layout.gif_wait, this);
        initView();
    }


    private void initView(){
        mGifLayout = (RelativeLayout) findViewById(R.id.good_detail_waitGif_layout);
        drawview = (SimpleDraweeView) findViewById(R.id.good_detail_waitGif);
        DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                //加载drawable里的一张gif图
                .setUri(Uri.parse("res://"+mContext.getPackageName()+"/"+R.drawable.timg))//设置uri
                .build();
        //设置Controller
        drawview.setController(mDraweeController);
    }

    public void setGifShow(){
        mGifLayout.setVisibility(View.VISIBLE);

    }
    public void setGifGone(){
        mGifLayout.setVisibility(View.GONE);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev){
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(View.VISIBLE== mGifLayout.getVisibility()) {
            return true;//拦截
        }else {
            return false;
        }
    }

}
