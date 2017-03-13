package com.example.weina.bishe.service.serviceImpl;

import com.example.weina.bishe.controller.MainActivity;
import com.example.weina.bishe.entity.OrderEntity;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.service.IOrderService;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.OkHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weina on 2017/3/13.
 */
public class OrderService implements IOrderService {

    public static void addOrder(int userId,int addressId,int goodsId,int amount){
        String url = StaticString.URL +"/order/add?userId="+userId
                +"&addressId="+addressId
                +"&goodsId="+goodsId
                +"&amount="+amount;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {

            @Override
            public void onSuccess(String data){

            }

            @Override
            public void onError(String msg) {

            }
        });
    }
    public static void getOrder(int userId,String status,int page,int lines,final ArrayList<OrderEntity> datas){
        String url = StaticString.URL +"/order/cart?userId="+userId
                +"&status="+status
                +"&page="+page
                +"&lines="+lines;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {

            @Override
            public void onSuccess(String data){
                if(null != data) {
                    Gson gson = new Gson();
                    List<OrderEntity> testBean = gson.fromJson(data, new TypeToken<List<OrderEntity>>() {}.getType());
                    datas.clear();
                    datas.addAll(testBean);
                    MainActivity.getHandle().sendEmptyMessage(IHomeService.ORDER_UPDATA_OVER);
                }
            }

            @Override
            public void onError(String msg) {

            }
        });
    }


}
