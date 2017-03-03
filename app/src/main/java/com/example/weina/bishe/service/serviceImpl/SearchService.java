package com.example.weina.bishe.service.serviceImpl;

import com.example.weina.bishe.controller.SearchActivity;
import com.example.weina.bishe.entity.LableEntity;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.OkHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.util.List;

/**
 * Created by weina on 2017/3/3.
 */
public class SearchService {

    public interface HintDataCallback{
        void callback(List<LableEntity> lableList);
    }

    public static void getHintLable(String text, final HintDataCallback call){
        String url = StaticString.URL+ "/lable/hint?text="+text+"&page=1&lines="+ SearchActivity.DEFAULT_HINT_SIZE;
        HomeService.getmOkHttpUtil().get(url,new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                Gson gson = new Gson();
                System.out.println(data);
                List<LableEntity> lableList = gson.fromJson(data,new TypeToken<List<LableEntity>>(){}.getType());
                call.callback(lableList);
            }
            @Override
            public void onSuccess(InputStream data) {

            }
        });
    }

}
