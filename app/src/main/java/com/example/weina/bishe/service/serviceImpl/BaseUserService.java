package com.example.weina.bishe.service.serviceImpl;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.weina.bishe.bean.GsonLogin;
import com.example.weina.bishe.controller.MainActivity;
import com.example.weina.bishe.entity.UserEntity;
import com.example.weina.bishe.service.IBaseUserService;
import com.example.weina.bishe.service.IHomeService;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.JsonUtil;
import com.example.weina.bishe.util.OkHttpUtil;
import com.example.weina.bishe.util.view.LoginDialog;
import com.google.gson.Gson;

/**
 * Created by weina on 2017/3/2.
 */
public class BaseUserService implements IBaseUserService{
    private static GsonLogin sGsonLogin;
    private static BaseUserService instance = getInstatnce();
    private LoginDialog loginDialog;
    private Handler handler = new Handler(Looper.getMainLooper());
    private static String SP_NAME = "user";
    private static String USER_KEY = "username";
    private ButtonBackCall mButtonBackCall;
    private BaseUserService(){
    }
    public synchronized static BaseUserService getInstatnce(){
        if(null == instance){
            instance = new BaseUserService();
        }
        return instance;
    }

    public interface ButtonBackCall{
        public void doConfirm();
        public void doCancel();
    }

    public interface ChangPswCallBack{
        void onSuccess();
        void onFail();
    }
    public interface RegisterCallBack{
        void onSuccess(String data);
        void onFail();
    }
    public interface UpdataCallBack{
        void onSuccess();
    }

    @Override
    public boolean checkUser(final Context context, final ButtonBackCall buttonBackCall) {
        //检测是否 登录且合法
        this.mButtonBackCall =buttonBackCall;
        if(null == sGsonLogin || !sGsonLogin.isBoolean()){
            //获取上一次的 用户名字
            //步骤1：创建一个SharedPreferences接口对象
            SharedPreferences read = context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
            //步骤2：获取文件中的值
            String value = read.getString(USER_KEY, "");

            loginDialog = new LoginDialog(context,value);
            loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loginDialog.setClickListenerInterface(new LoginDialog.ClickListenerInterface() {
                @Override
                public void doConfirm(String username, String password) {
                    UserEntity userEntity = new UserEntity(username,password);
                    login(userEntity,context);
                }

                @Override
                public void doCancel() {
                    if(null!= mButtonBackCall) {
                        mButtonBackCall.doCancel();
                    }
                }
            });
            loginDialog.show();
            return false;
        }else {
            return true;
        }
    }
    public void update(final UpdataCallBack updataCallBack){
        String url = StaticString.URL+"/user/login";
        HomeService.getmOkHttpUtil().postJson(url, JsonUtil.toJson(sGsonLogin), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                if(null!=data){
                    Gson gson = new Gson();
                    BaseUserService.sGsonLogin = gson.fromJson(data,GsonLogin.class);
                    if(null !=updataCallBack) {
                        updataCallBack.onSuccess();
                    }
                }
            }
            @Override
            public void onError(String msg) {

            }
        });
    }
    private boolean login(final UserEntity userEntity, final Context context) {
        String url = StaticString.URL+"/user/login";
        final GsonLogin gsonLogin = new GsonLogin();
        gsonLogin.setUserEntity(userEntity);
        //发送 登录请求
        HomeService.getmOkHttpUtil().postJson(url, JsonUtil.toJson(gsonLogin), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                if(null!=data){
                    Gson gson = new Gson();
                    BaseUserService.sGsonLogin = gson.fromJson(data,GsonLogin.class);
                    if(BaseUserService.sGsonLogin.isBoolean()) {
                        //步骤2-1：创建一个SharedPreferences.Editor接口对象，lock表示要写入的XML文件名，MODE_WORLD_WRITEABLE写操作
                        SharedPreferences.Editor editor = context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE).edit();
                        //步骤2-2：将获取过来的值放入文件
                        editor.putString(USER_KEY, BaseUserService.sGsonLogin.getUserEntity().getUserName());
                        //步骤3：提交
                        editor.commit();

                        handler.post(new Runnable() {
                            //登录成功后提示
                            @Override
                            public void run() {
                                Toast.makeText(context,BaseUserService.sGsonLogin.getUserEntity().getUserName()+", 欢迎回来",Toast.LENGTH_SHORT).show();
                            }
                        });
                        MainActivity.getHandle().sendEmptyMessage(IHomeService.CHANG_USER_NAME);
                        loginDialog.onBackPressed();
                        if(null != mButtonBackCall) {
                            mButtonBackCall.doConfirm();
                        }
                    }else {
                        handler.post(new Runnable() {
                            //登录成功后提示
                            @Override
                            public void run() {
                                loginDialog.setmMessage(" 登录失败请检查用户名和密码 ");
                            }
                        });
                    }

                }
            }

            @Override
            public void onError(String msg) {
                handler.post(new Runnable() {
                    //登录成功后提示
                    @Override
                    public void run() {
                        loginDialog.setmMessage(" 连接网络失败 ");
                    }
                });
            }
        });

        return false;
    }
    public static void logout(){
        sGsonLogin.setBoolean(false);
        sGsonLogin.setUserEntity(null);
        MainActivity.getHandle().sendEmptyMessage(IHomeService.CHANG_USER_NAME);
    }
    public static void changPayPassword(int oldPsw, int newPsw, final ChangPswCallBack changPswCallBack){
        String url = StaticString.URL + "/user/changePay?userId="+sGsonLogin.getUserEntity().getUserId()
                +"&old="+oldPsw
                +"&new="+newPsw;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                if(null != data && !"null".equals(data)){
                    Gson gson = new Gson();
                    UserEntity userEntity = gson.fromJson(data,UserEntity.class);
                    sGsonLogin.setUserEntity(userEntity);
                    changPswCallBack.onSuccess();
                    Log.d("psw","真的进来了");
                }else {
                    changPswCallBack.onFail();
                }
            }
            @Override
            public void onError(String msg) {
                changPswCallBack.onFail();
            }
        });
    }

    public static void changPassword(String oldPsw, String newPsw, final ChangPswCallBack changPswCallBack){
        String url = StaticString.URL + "/user/changePsw?userId="+sGsonLogin.getUserEntity().getUserId()
                +"&old="+oldPsw
                +"&new="+newPsw;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                if(null != data||!"null".equals(data)){
                    Gson gson = new Gson();
                    UserEntity userEntity = gson.fromJson(data,UserEntity.class);
                    sGsonLogin.setUserEntity(userEntity);
                    changPswCallBack.onSuccess();
                }else {
                    changPswCallBack.onFail();
                }
            }
            @Override
            public void onError(String msg) {
                changPswCallBack.onFail();
            }
        });
    }
    public void rgisterUser(UserEntity userEntity, final RegisterCallBack registerCallBack){
        String url = StaticString.URL+"/user/register";
        HomeService.getmOkHttpUtil().postJson(url, JsonUtil.toJson(userEntity), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                registerCallBack.onSuccess(data);
            }
            @Override
            public void onError(String msg) {
                registerCallBack.onFail();
            }
        });
    }


    public static GsonLogin getGsonLogin() {
        return sGsonLogin;
    }

    public static void setGsonLogin(GsonLogin gsonLogin) {
        sGsonLogin = gsonLogin;
    }
}
