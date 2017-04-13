package com.example.weina.bishe.service.serviceImpl;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.example.weina.bishe.bean.GsonAddOrder;
import com.example.weina.bishe.controller.MainActivity;
import com.example.weina.bishe.controller.OrderMgActivity;
import com.example.weina.bishe.entity.OrderEntity;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.service.IOrderService;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.JsonUtil;
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

    /**
     *购物车
     */
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
                        MainActivity.getHandle().sendEmptyMessage(IHomeService.ORDER_UPDATA_OVER);
                        datas.addAll(testBean);
                    }
                }
                MainActivity.getHandle().sendEmptyMessage(IHomeService.ORDER_UPDATA_OVER);
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

    /**
     * 创建订单
     */
    public static void addOrderNotPay(List<Integer> orderId, int addressId, final OrderCallBack mOrderCallBack){
        String url = StaticString.URL +"/order/notPayOrder";
        GsonAddOrder gsonAddOrder = new GsonAddOrder(orderId,addressId);
        HomeService.getmOkHttpUtil().postJson(url, JsonUtil.toJson(gsonAddOrder), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                mOrderCallBack.callBack(data);
            }
            @Override
            public void onError(String msg) {
                mOrderCallBack.error(msg);
            }
        });
    }
    /**
     * 订单管理 获取 状态
     */
    public static synchronized void getOrderToMg(int userId,String status,int page,int lines,final ArrayList<OrderEntity> datas){
        String url = StaticString.URL +"/order/cart?userId="+userId
                +"&status="+status
                +"&page="+page
                +"&lines="+lines;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {

            @Override
            public void onSuccess(String data){
                if(null != data||!"null".equals(data)) {
                    Gson gson = new Gson();
                    List<OrderEntity> testBean = gson.fromJson(data, new TypeToken<List<OrderEntity>>() {}.getType());
                    Log.d("testBean","是否为空: "+ (null == testBean));
                    if(null != testBean) {
                        datas.clear();
                        datas.addAll(testBean);
                    }else{
                        datas.clear();
                    }
                }else {
                    datas.clear();
                }
                OrderMgActivity.getmHandler().sendEmptyMessage(OrderMgActivity.UPDATA_OVER);
            }

            @Override
            public void onError(String msg) {
                OrderMgActivity.getmHandler().sendEmptyMessage(OrderMgActivity.UPDATA_OVER);
            }
        });
    }
    //删除订单
    public static void delete(int userId, int orderId){
        String url = StaticString.URL +"/order/delete?userId="+userId
                +"&orderId="+orderId;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                OrderMgActivity.getmHandler().sendEmptyMessage(OrderMgActivity.DELETE_OVER);
            }
            @Override
            public void onError(String msg) {
                //TODO:
            }
        });
    }
    public static void pay(List<Integer> list, final OrderCallBack orderCallBack){
        GsonAddOrder gsonAddOrder = new GsonAddOrder();
        gsonAddOrder.setOrderIdList(list);
        String url = StaticString.URL +"/order/pay";
        HomeService.getmOkHttpUtil().postJson(url, JsonUtil.toJson(gsonAddOrder), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                orderCallBack.callBack(data);
            }

            @Override
            public void onError(String msg) {
                orderCallBack.error(msg);
            }
        });
    }

    //直接购买
    public static void addOrderNoCart(int userId, int addressId, int goodsId, int amount, final OrderCallBack orderCallBack){
        String url = StaticString.URL +"/order/addNoCart?userId="+userId
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
    public static void orderGet(int userId,int orderId){
        String url = StaticString.URL +"/order/accept?userId="+userId
                +"&orderId="+orderId;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){

                OrderMgActivity.getmHandler().sendEmptyMessage(OrderMgActivity.GET_OVER);
            }
            @Override
            public void onError(String msg) {

            }
        });
    }
}
