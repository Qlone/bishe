/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.weina.bishe.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by weina on 2017/3/2.
 */
public class GoodsEntity implements Parcelable {
    private Integer goodsId;
    private String type;
    private Double price;
    private String title;
    private String picture;
    private Integer sales;
    private Integer stock;
    private String status;
    private Integer views;
    private Integer goodsDelete;
    private String mPictureGroup;
    private Bitmap bitmap;
    private boolean downloading = false;

    public String getPictureGroup() {
        return mPictureGroup;
    }

    public void setPictureGroup(String pictureGroup) {
        mPictureGroup = pictureGroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getGoodsDelete() {
        return goodsDelete;
    }

    public void setGoodsDelete(Integer goodsDelete) {
        this.goodsDelete = goodsDelete;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isDownloading() {
        return downloading;
    }

    public void setDownloading(boolean downloading) {
        this.downloading = downloading;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }


    public String getTpye() {
        return type;
    }

    public void setTpye(String tpye) {
        this.type = tpye;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }


    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }



    public Integer getDelect() {
        return goodsDelete;
    }

    public void setDelect(Integer delect) {
        this.goodsDelete = delect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
//        private Integer goodsId;
//        private String type;
//        private Double price;
//        private String title;
//        private String picture;
//        private Integer sales;
//        private Integer stock;
//        private String status;
//        private Integer views;
//        private Integer delete;
//        private Bitmap bitmap;
//        private boolean downloading = false;
        parcel.writeInt(goodsId);
        parcel.writeString(type);
        parcel.writeDouble(price);
        parcel.writeString(title);
        parcel.writeString(picture);
        parcel.writeInt(sales);
        parcel.writeInt(stock);
        parcel.writeString(status);
        parcel.writeInt(views);
        parcel.writeInt(goodsDelete);
        parcel.writeString(mPictureGroup);
//        bitmap.writeToParcel(parcel,0);
        parcel.writeByte((byte)(downloading?1:0));
    }
    public GoodsEntity(Parcel parcel){
        goodsId = parcel.readInt();
        type = parcel.readString();
        price = parcel.readDouble();
        title = parcel.readString();
        picture = parcel.readString();
        sales = parcel.readInt();
        stock = parcel.readInt();
        status = parcel.readString();
        views = parcel.readInt();
        goodsDelete = parcel.readInt();
        mPictureGroup = parcel.readString();
//        bitmap = Bitmap.CREATOR.createFromParcel(parcel);
        downloading = parcel.readByte() != 0;
    }

    public static final Creator<GoodsEntity> CREATOR = new Creator<GoodsEntity>() {
        @Override
        public GoodsEntity createFromParcel(Parcel parcel) {
            return new GoodsEntity(parcel);
        }

        @Override
        public GoodsEntity[] newArray(int i) {
            return new GoodsEntity[0];
        }
    };
}
