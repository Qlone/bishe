package com.example.weina.bishe.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by weina on 2017/3/13.
 */
public class OrderEntity implements Serializable{
    private Integer mOrdersId;
    private Integer mGoodsId;
    private String mAddress;
    private String mPhone;
    private String mPicture;
    private String mTitle;
    private Double mPrice;
    private Date mCreateTime;
    private Date mPaidTime;
    private String mStatus;
    private Integer mAmount;
    private Integer mUserId;
    private String mReciver;
    private boolean isChoose =false;
    private boolean isEdit = false;

    public String getReciver() {
        return mReciver;
    }

    public void setReciver(String reciver) {
        mReciver = reciver;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public Integer getOrdersId() {
        return mOrdersId;
    }

    public void setOrdersId(Integer ordersId) {
        mOrdersId = ordersId;
    }

    public Integer getGoodsId() {
        return mGoodsId;
    }

    public void setGoodsId(Integer goodsId) {
        mGoodsId = goodsId;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Double getPrice() {
        return mPrice;
    }

    public void setPrice(Double price) {
        mPrice = price;
    }

    public Date getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(Date createTime) {
        mCreateTime = createTime;
    }

    public Date getPaidTime() {
        return mPaidTime;
    }

    public void setPaidTime(Date paidTime) {
        mPaidTime = paidTime;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public Integer getAmount() {
        return mAmount;
    }

    public void setAmount(Integer amount) {
        mAmount = amount;
    }

    public Integer getUserId() {
        return mUserId;
    }

    public void setUserId(Integer userId) {
        mUserId = userId;
    }
}
