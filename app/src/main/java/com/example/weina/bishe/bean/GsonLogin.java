/*
 * Copyright (c) 2017. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.weina.bishe.bean;

import com.example.weina.bishe.entity.UserEntity;

/**
 * Created by weina on 2017/3/1.
 */

public class GsonLogin {

    /**用户登录交互
     * {"UserEntity":"userEntity","Boolean":true}
     * UserEntity : userEntity
     * Boolean : true
     */

    private UserEntity userEntity;
    private boolean Boolean;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity UserEntity) {
        this.userEntity = UserEntity;
    }

    public boolean isBoolean() {
        return Boolean;
    }

    public void setBoolean(boolean Boolean) {
        this.Boolean = Boolean;
    }
}
