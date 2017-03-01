package com.example.weina.bishe.service.serviceImpl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;

import com.example.weina.bishe.bean.GoodsRoughBean;
import com.example.weina.bishe.bean.GsonLogin;
import com.example.weina.bishe.controller.MainActivity;
import com.example.weina.bishe.entity.UserEntity;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.util.FlushedInputStream;
import com.example.weina.bishe.util.JsonUtil;
import com.example.weina.bishe.util.OkHttpUtil;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by weina on 2017/2/11.
 */
public class HomeService implements IHomeService{
    private static OkHttpUtil mOkHttpUtil = new OkHttpUtil();

    public static OkHttpUtil getmOkHttpUtil() {
        return mOkHttpUtil;
    }

    //192.168.31.249
    private static final String url = "http://121.42.173.155/1.0.0/gson";
    HomeService(){
    }

    public static void getContent(final ArrayList<GoodsRoughBean.GoodsBean> datas){
        mOkHttpUtil.get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                if(null != data){
                    Gson gson = new Gson();
                    GoodsRoughBean testBean = gson.fromJson(data,GoodsRoughBean.class);
                    datas.clear();
                    datas.addAll(testBean.getGoods());
                }else{
                    System.out.println("no messsage !!!!!!!");
                }


                MainActivity.getHandle().sendEmptyMessage(UPDATE_OVER);
            }

            @Override
            public void onSuccess(InputStream data) {

            }
        });
    }
    public static void getPicture(final GoodsRoughBean.GoodsBean goodsBean, final int position){
        if(null != goodsBean.getPicture()) {
            mOkHttpUtil.getStream(goodsBean.getPicture(), new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data) {

                }

                @Override
                public void onSuccess(InputStream data)  {
                    //获取 图片 然后通知 更新
                    Bitmap bm = BitmapFactory.decodeStream(new FlushedInputStream(data));
                    goodsBean.setBitmap(bm);
                    try {
                        data.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt(POSITION_FLAG, position);
                    msg.what = POSITION_MSG;
                    MainActivity.getHandle().sendMessage(msg);
                }
            });
        }
    }

    public static void login(){
        UserEntity userEntity = new UserEntity();
        userEntity.setUserName("first");
        userEntity.setPassword("asd");
        GsonLogin gsonLogin = new GsonLogin();
        gsonLogin.setUserEntity(userEntity);
        mOkHttpUtil.postJson("http://192.168.137.1:8080/user/login", JsonUtil.toJson(gsonLogin), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                System.out.print(data);
            }

            @Override
            public void onSuccess(InputStream data) {

            }
        });
    }
}
