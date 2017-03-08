package com.example.weina.bishe.service.serviceImpl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.weina.bishe.controller.MainActivity;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.FlushedInputStream;
import com.example.weina.bishe.util.GoodsManager;
import com.example.weina.bishe.util.OkHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by weina on 2017/2/11.
 */
public class HomeService implements IHomeService{
    private static OkHttpUtil mOkHttpUtil = new OkHttpUtil();

    public static OkHttpUtil getmOkHttpUtil() {
        return mOkHttpUtil;
    }

    //192.168.31.249
//    private static final String url = "http://121.42.173.155/1.0.0/gson";
    HomeService(){
    }

    public static void getContent(final ArrayList<GoodsEntity> datas){
        String url = StaticString.URL+"/good/list?page="+MainActivity.getPage()+"&lines=8";
        mOkHttpUtil.get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                if(null != data){
                    Gson gson = new Gson();
                    List<GoodsEntity> testBean = gson.fromJson(data,new TypeToken<List<GoodsEntity>>(){}.getType());
                    if(null != testBean) {
                        if(MainActivity.getPage() == 1){
                            GoodsManager.clear(datas);
                            datas.addAll(testBean);
                            MainActivity.getHandle().sendEmptyMessage(UPDATE_OVER);
                        }else {
                            datas.addAll(testBean);
                            MainActivity.getHandle().sendEmptyMessage(LOAD_OVER);
                        }
                        MainActivity.setPage(MainActivity.getPage()+1);
                    }else {
                        if(MainActivity.getPage() == 1){
                            MainActivity.getHandle().sendEmptyMessage(UPDATE_OVER);
                        }else {
                            MainActivity.getHandle().sendEmptyMessage(LOAD_OVER);
                        }
                    }
                }else{
                    System.out.println("no messsage !!!!!!!");
                }
            }

            @Override
            public void onError(String msg) {
                MainActivity.getHandle().sendEmptyMessage(UPDATE_OVER);
                MainActivity.getHandle().sendEmptyMessage(LOAD_OVER);
            }
        });
    }
    public static void getPicture(final GoodsEntity goodsEntity, final int position, final Handler handler){
        if(null != goodsEntity.getPicture()) {
            mOkHttpUtil.getStream(goodsEntity.getPicture(), new OkHttpUtil.HttpCallback() {

                @Override
                public void onSuccess(InputStream data)  {
                    //获取 图片 然后通知 更新
                    Bitmap bm = BitmapFactory.decodeStream(new FlushedInputStream(data));
                    goodsEntity.setBitmap(bm);
                    try {
                        data.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putInt(POSITION_FLAG, position);
                    msg.what = POSITION_MSG;
                    handler.sendMessage(msg);
                }

                @Override
                public void onError(String msg) {
                    Message mssg = new Message();
                    mssg.what = POSITION_MSG;
                    handler.sendMessage(mssg);
                }
            });
        }
    }

}
