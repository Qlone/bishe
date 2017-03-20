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
public class GsonAddOrder {
    //添加 订单id
    List<Integer>  orderIdList;
    // 地址 id
    int addressId;

    public GsonAddOrder() {
    }

    public GsonAddOrder(List<Integer> orderIdList, int addressId) {
        this.orderIdList = orderIdList;
        this.addressId = addressId;
    }

    public List<Integer> getOrderIdList() {
        return orderIdList;
    }

    public void setOrderIdList(List<Integer> orderIdList) {
        this.orderIdList = orderIdList;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }
}
