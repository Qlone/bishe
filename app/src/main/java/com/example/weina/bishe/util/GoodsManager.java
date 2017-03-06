package com.example.weina.bishe.util;

import com.example.weina.bishe.entity.GoodsEntity;

import java.util.List;

/**
 * Created by weina on 2017/3/6.
 */
public class GoodsManager {
    public static void clear(final List<GoodsEntity> listData){
        for(GoodsEntity goodsEntity:listData){
            if(null != goodsEntity&& null != goodsEntity.getBitmap() && !goodsEntity.getBitmap().isRecycled()){
                goodsEntity.getBitmap().recycle();
                goodsEntity.setBitmap(null);
            }
        }
        listData.clear();
    }
}
