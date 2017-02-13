package com.example.weina.bishe.bean;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by weina on 2017/2/12.
 */
public class GoodsRoughBean {
    private List<GoodsBean> goods;

    public List<GoodsBean> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodsBean> goods) {
        this.goods = goods;
    }

    public static class GoodsBean {
        /**
         * type : hot
         * picture : http://**
         * title : nick
         * price : 10
         * sales : 11
         */

        private String type;
        private String picture;
        private String title;
        private Bitmap bitmap;
        private double price;
        private int sales;

        public String getType() {
            return type;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getSales() {
            return sales;
        }

        public void setSales(int sales) {
            this.sales = sales;
        }
    }
}
