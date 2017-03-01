package com.example.weina.bishe.util;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by weina on 2017/2/11.
 */
public class OkHttpUtil {
    public static String TAG="debug-okhttp";
    public static boolean isDebug=true;

    private OkHttpClient client;
    // 超时时间
    public static final int TIMEOUT = 1000 * 60;

    //json请求
    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");

    private Handler handler = new Handler(Looper.getMainLooper());

    public OkHttpUtil() {
        // TODO Auto-generated constructor stub
        this.init();
    }

    private void init() {

        client = new OkHttpClient();

        // 设置超时时间
        client.newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS).build();

    }

    /**
     * post请求，json数据为body
     *
     * @param url
     * @param json
     * @param callback
     */
    public void postJson(String url, String json, final HttpCallback callback) {

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();

        onStart(callback);

        client.newCall(request).enqueue(new Callback() {



            @Override
            public void onResponse(Call call,Response response) throws IOException {
                // TODO Auto-generated method stub
                if (response.isSuccessful()) {
                    onSuccess(callback, response.body().string());
                } else {
                    onError(callback, response.message());
                }
            }

            @Override
            public void onFailure(Call call, IOException arg1) {
                // TODO Auto-generated method stub

                onError(callback, arg1.getMessage());
                arg1.printStackTrace();

            }
        });

    }

    /**
     * post请求 map为body
     *
     * @param url
     * @param map
     * @param callback
     */
    public void post(String url, Map<String, Object> map,final HttpCallback callback) {

        // FormBody.Builder builder = new FormBody.Builder();
        // FormBody body=new FormBody.Builder().add("key", "value").build();

        /**
         * 创建请求的参数body
         */
        FormBody.Builder builder = new FormBody.Builder();

        /**
         * 遍历key
         */
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {

                System.out.println("Key = " + entry.getKey() + ", Value = "
                        + entry.getValue());
                builder.add(entry.getKey(), entry.getValue().toString());

            }
        }

        RequestBody body = builder.build();

        Request request = new Request.Builder().url(url).post(body).build();

        onStart(callback);

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // TODO Auto-generated method stub

                if (response.isSuccessful()) {

                    onSuccess(callback, response.body().string());

                } else {
                    onError(callback, response.message());
                }
            }

            @Override
            public void onFailure(Call call, IOException arg1) {
                // TODO Auto-generated method stub
                arg1.printStackTrace();
                onError(callback, arg1.getMessage());
            }
        });

    }

    /**
     * get请求
     * @param url
     * @param callback
     */
    public void getStream(String url, final HttpCallback callback) {

        Request request = new Request.Builder().url(url).build();

        onStart(callback);

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call,Response response) throws IOException {
                // TODO Auto-generated method stub
                if (response.isSuccessful()) {
                    onSuccess(callback, response.body().byteStream());
                } else {
                    onError(callback, response.message());
                }
            }

            @Override
            public void onFailure(Call call, IOException arg1) {
                // TODO Auto-generated method stub

                onError(callback, arg1.getMessage());
                arg1.printStackTrace();

            }
        });

    }

    public void get(String url, final HttpCallback callback) {

        Request request = new Request.Builder().url(url).build();

        onStart(callback);

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO Auto-generated method stub
                if (response.isSuccessful()) {
                    onSuccess(callback, response.body().string());
                } else {
                    onError(callback, response.message());
                }
            }

            @Override
            public void onFailure(Call call, IOException arg1) {
                // TODO Auto-generated method stub

                onError(callback, arg1.getMessage());
                arg1.printStackTrace();

            }
        });
    }
    /**
     * log信息打印
     * @param params
     */
    public void debug(String params){
        if(false == isDebug){
            return;
        }

        if(null == params){
            Log.d(TAG, "params is null");
        }else{
            Log.d(TAG, params);
        }
    }

    private void onStart(HttpCallback callback) {
        if (null != callback) {
            callback.onStart();
        }
    }

    private void onSuccess(final HttpCallback callback, final String data) {

        debug(data);

//        if (null != callback) {
//            handler.post(new Runnable() {
//                public void run() {
//                    // 需要在主线程的操作。
//
//                }
//            });
//        }
        if(null !=callback){
            callback.onSuccess(data);
        }

    }
    private void onSuccess(final HttpCallback callback, final InputStream data) {


//        if (null != callback) {
//            handler.post(new Runnable() {
//                public void run() {
//                    // 需要在主线程的操作。
//                    callback.onSuccess(data);
//                }
//            });
//        }
        if(null != callback){
            callback.onSuccess(data);
        }
    }

    private void onError(final HttpCallback callback,final String msg) {
        if (null != callback) {
            handler.post(new Runnable() {
                public void run() {
                    // 需要在主线程的操作。
                    callback.onError(msg);
                }
            });
        }
    }


    /**
     * http请求回调
     *
     * @author Flyjun
     *
     */
    public static abstract class HttpCallback {
        // 开始
        public void onStart() {};

        // 成功回调
        public abstract void onSuccess(String data);

        public abstract void onSuccess(InputStream data);

        // 失败回调
        public void onError(String msg) {};
    }

}
