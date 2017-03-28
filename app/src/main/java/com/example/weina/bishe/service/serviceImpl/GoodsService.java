package com.example.weina.bishe.service.serviceImpl;

import com.example.weina.bishe.controller.GoodDetailActivity;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.service.IGoodsService;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.OkHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by weina on 2017/3/21.
 */
public class GoodsService implements IGoodsService {


    public static void getGoodsDetail(int goodsId,final List<GoodsEntity> datas){
        String url = StaticString.URL +"/good/getItem?goodsId="+goodsId;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                if(null!=data){
                    Gson gson = new Gson();
                    List<GoodsEntity> testBean = gson.fromJson(data, new TypeToken<List<GoodsEntity>>() {}.getType());
                    if(null!=testBean){
                        datas.clear();
                        datas.addAll(testBean);
                        GoodDetailActivity.getmHandler().sendEmptyMessage(GoodDetailActivity.UPDATA_OVER);
                    }
                }
            }
            @Override
            public void onError(String msg) {
                GoodDetailActivity.getmHandler().sendEmptyMessage(GoodDetailActivity.UPDATA_OVER);
            }
        });
    }
}
