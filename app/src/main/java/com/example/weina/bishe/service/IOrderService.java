package com.example.weina.bishe.service;

/**
 * Created by weina on 2017/3/13.
 */
public interface IOrderService {
    /**
     * 购物车
     */
    String ORDER_STATUS_CART="cart";
    /**
     * 删除 订单
     */
    String ORDER_STATUS_DEDLETE = "delete";

    /**
     * 订单 尚未付款 ，也就是 在购物车里面了吧
     */
    String ORDER_STATUS_NOPAY = "not_pay";
    /**
     * 订单 已经付款 商家没有发货
     */
    String ORDER_STATUS_PAID = "pay_no_send";

    /**
     * 订单 已经发货 正在路上
     */
    String ORDER_STATUS_ONWAY = "pay_on_way";
    /**
     * 订单 已经收到
     */
    String ORDER_STATUS_GET = "pay_and_get";

    /**
     * 订单 评论
     */
    String ORDER_STATUS_COMMENT="pay_and_commend";
}
