package com.example.weina.bishe.service.serviceImpl;

import com.example.weina.bishe.controller.AddressMgActivity;
import com.example.weina.bishe.entity.AddressEntity;
import com.example.weina.bishe.service.IAddressService;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.JsonUtil;
import com.example.weina.bishe.util.OkHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weina on 2017/3/17.
 */
public class AddressService implements IAddressService {

    public interface AddressCallBack{
        void success(String data);
        void error();
    }
    //获取地址
    public static void getAddressList(final ArrayList<AddressEntity> list){
        if(BaseUserService.getGsonLogin().isBoolean()){//若合法
            String url = StaticString.URL+"/address/list?userId="
                    +BaseUserService.getGsonLogin().getUserEntity().getUserId();
            HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data){
                    if(null!= data){
                        Gson gson = new Gson();
                        List<AddressEntity> testBean = gson.fromJson(data, new TypeToken<List<AddressEntity>>() {}.getType());
                        if(null != testBean){
                            list.clear();
                            list.addAll(testBean);
                        }
                    }
                    AddressMgActivity.getHandler().sendEmptyMessage(IAddressService.ADDRESS_UPDATA_OVER);
                }

                @Override
                public void onError(String msg) {
                    AddressMgActivity.getHandler().sendEmptyMessage(IAddressService.ADDRESS_UPDATA_OVER);
                }
            });
        }
    }

    //添加地址
    public static void addAddress(String address,String phone,String reciver){
        if(BaseUserService.getGsonLogin().isBoolean()){//若合法
            String url = StaticString.URL+"/address/add";
            AddressEntity addressEntity = new AddressEntity();
            addressEntity.setAddress(address);
            addressEntity.setPhone(phone);
            addressEntity.setName(reciver);
            addressEntity.setUserId(BaseUserService.getGsonLogin().getUserEntity().getUserId());
            HomeService.getmOkHttpUtil().postJson(url, JsonUtil.toJson(addressEntity), new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data){
                    if(null!=data){
                        if("true".equals(data)){
                            AddressMgActivity.getHandler().sendEmptyMessage(IAddressService.ADDRESS_ADD_SUCCESS);
                        }else{
                            AddressMgActivity.getHandler().sendEmptyMessage(IAddressService.ADDRESS_ADD_FAIL);
                        }
                    }else{
                        AddressMgActivity.getHandler().sendEmptyMessage(IAddressService.ADDRESS_ADD_ELSE);
                    }
                }

                @Override
                public void onError(String msg) {
                    AddressMgActivity.getHandler().sendEmptyMessage(IAddressService.ADDRESS_ADD_ELSE);
                }
            });
        }
    }
    //删除地址
    public static void deleteAddress(int addressId , final AddressCallBack addressCallBack){
        if(BaseUserService.getGsonLogin().isBoolean()){//合法
            String url = StaticString.URL+"/address/delete?userId="
                    +BaseUserService.getGsonLogin().getUserEntity().getUserId()
                    +"&addressId="+addressId;
            HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
                @Override
                public void onSuccess(String data){
                    addressCallBack.success(data);
                }
                @Override
                public void onError(String msg) {
                    addressCallBack.error();
                }
            });
        }
    }
}
