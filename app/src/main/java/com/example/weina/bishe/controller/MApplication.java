package com.example.weina.bishe.controller;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by weina on 2017/3/9.
 */
public class MApplication extends Application {

    @Override
    public  void onCreate(){
        super.onCreate();
//        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(getApplicationContext())
//                .setMaxCacheSize(1024*1024*20)
//                .setMaxCacheSizeOnLowDiskSpace(1024*1024*10)
//                .setMaxCacheSizeOnVeryLowDiskSpace(1024*1024*10)
//                .build();
//
//        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplicationContext())
//                .setMainDiskCacheConfig(diskCacheConfig)
//                .build();
        Fresco.initialize(this);
    }
}
