package com.example.weina.bishe.service.serviceImpl;

import android.os.Bundle;
import android.os.Message;

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

    public interface OrderCallBack{
        void callBack(String data);
        void error(String msg);
    }

    public static void addOrder(int userId, int addressId, int goodsId, int amount, final OrderCallBack orderCallBack){
        String url = StaticString.URL +"/order/add?userId="+userId
                +"&addressId="+addressId
                +"&goodsId="+goodsId
                +"&amount="+amount;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {

            @Override
            public void onSuccess(String data){
                orderCallBack.callBack(data);
            }

            @Override
            public void onError(String msg) {
                orderCallBack.callBack(msg);
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
                    if(null != testBean) {
                        datas.clear();
                        datas.addAll(testBean);
                    }
                    MainActivity.getHandle().sendEmptyMessage(IHomeService.ORDER_UPDATA_OVER);
                }
            }

            @Override
            public void onError(String msg) {
                MainActivity.getHandle().sendEmptyMessage(IHomeService.ORDER_UPDATA_OVER);
            }
        });
    }

    public static void updataOrderNumber(int userId, int orderId, int amount, final OrderCallBack mOrderCallBack){
        String url = StaticString.URL +"/order/changeAmount?userId="+userId
                +"&orderId="+orderId
                +"&amount="+amount;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                mOrderCallBack.callBack(data);
                MainActivity.getHandle().sendEmptyMessage(IHomeService.ORDER_UPDATA_OVER);
            }
            @Override
            public void onError(String msg) {
                MainActivity.getHandle().sendEmptyMessage(IHomeService.ORDER_UPDATA_OVER);
            }
        });
    }
    public static void deleteOrder(int userId, int orderId, final int position){
        String url = StaticString.URL +"/order/deleteCart?userId="+userId
                +"&orderId="+orderId;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                if(null !=data && !"".equals(data)&& "true".equals(data)) {
                    Message message = new Message();
                    Bundle mbundle = new Bundle();
                    mbundle.putInt(IHomeService.POSITION_FLAG, position);
                    message.setData(mbundle);
                    message.what = IHomeService.ORDER_UPDATA;
                    MainActivity.getHandle().sendMessage(message);//删除后更新
                }
            }
            @Override
            public void onError(String msg) {

            }
        });
    }
}
