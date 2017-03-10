package com.example.weina.bishe.service.serviceImpl;

import com.example.weina.bishe.controller.MainActivity;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.OkHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
                            datas.clear();
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

}
