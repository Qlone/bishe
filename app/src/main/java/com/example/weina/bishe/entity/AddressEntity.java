package com.example.weina.bishe.entity;

/**
 * Created by weina on 2017/3/17.
 */
public class AddressEntity {
    private Integer mAddressId;
    private String mAddress;
    private String mPhone;
    private Integer mUserId;
    private String mName;

    public Integer getAddressId() {
        return mAddressId;
    }

    public void setAddressId(Integer addressId) {
        mAddressId = addressId;
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

    public Integer getUserId() {
        return mUserId;
    }

    public void setUserId(Integer userId) {
        mUserId = userId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
