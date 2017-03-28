package com.example.weina.bishe.entity;

import java.util.Date;

/**
 * Created by weina on 2017/3/28.
 */
public class CommentEntity {
    private Integer mCommentId;
    private Integer mOrderId;
    private Integer mUserId;
    private Integer mGoodsId;
    private Date mCommentData;
    private String mContext;
    private Integer mStart;

    public Integer getCommentId() {
        return mCommentId;
    }

    public void setCommentId(Integer commentId) {
        mCommentId = commentId;
    }

    public Integer getOrderId() {
        return mOrderId;
    }

    public void setOrderId(Integer orderId) {
        mOrderId = orderId;
    }

    public Integer getUserId() {
        return mUserId;
    }

    public void setUserId(Integer userId) {
        mUserId = userId;
    }

    public Integer getGoodsId() {
        return mGoodsId;
    }

    public void setGoodsId(Integer goodsId) {
        mGoodsId = goodsId;
    }

    public Date getCommentData() {
        return mCommentData;
    }

    public void setCommentData(Date commentData) {
        mCommentData = commentData;
    }

    public String getContext() {
        return mContext;
    }

    public void setContext(String context) {
        mContext = context;
    }

    public Integer getStart() {
        return mStart;
    }

    public void setStart(Integer start) {
        mStart = start;
    }
}
