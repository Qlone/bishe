package com.example.weina.bishe.service;

import com.example.weina.bishe.bean.GsonTestBean;
import com.example.weina.bishe.util.OkHttpUtil;
import com.google.gson.Gson;

/**
 * Created by weina on 2017/2/11.
 */
public class HomeService {
    private static OkHttpUtil mOkHttpUtil = new OkHttpUtil();;
    private static final String url = "http://192.168.31.249:8080/gson";
    HomeService(){
    }
    public static void getContent(){
        mOkHttpUtil.get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                if(null != data){
                    Gson gson = new Gson();
                    GsonTestBean testBean = gson.fromJson(data,GsonTestBean.class);
                    System.out.println(testBean.getList().get(0).getText()+"!!!!!!!!");
                }else{
                    System.out.println("no messsage !!!!!!!");
                }
            }
        });
    }
}
