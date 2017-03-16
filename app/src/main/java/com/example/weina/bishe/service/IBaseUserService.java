package com.example.weina.bishe.service;

import android.content.Context;

import com.example.weina.bishe.service.serviceImpl.BaseUserService;

/**
 * Created by weina on 2017/3/2.
 */
public interface IBaseUserService {
    /**
     * 检测是否登录
     * @return
     */

    boolean checkUser(Context context, BaseUserService.ButtonBackCall buttonBackCall);
}
