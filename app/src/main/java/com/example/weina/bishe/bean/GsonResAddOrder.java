/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.weina.bishe.bean;

import java.util.List;

/**
 * Created by weina on 2017/3/20.
 */
public class GsonResAddOrder {
    List<String> orderName;//订单名字
    List<String> msg;//订单结果 信息

    public List<String> getOrderName() {
        return orderName;
    }

    public void setOrderName(List<String> orderName) {
        this.orderName = orderName;
    }

    public List<String> getMsg() {
        return msg;
    }

    public void setMsg(List<String> msg) {
        this.msg = msg;
    }
}