package com.example.weina.bishe.service.serviceImpl;

import android.util.Log;

import com.example.weina.bishe.controller.SearchActivity;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.entity.LableEntity;
import com.example.weina.bishe.service.ISearchService;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.JsonUtil;
import com.example.weina.bishe.util.OkHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weina on 2017/3/3.
 */
public class SearchService implements ISearchService{

    public interface HintDataCallback{
        void callback(List<LableEntity> lableList);
    }

    /**
     * 获取 推荐
     * @param text
     * @param call
     */
    public static void getHintLable(String text, final HintDataCallback call){
        String url = StaticString.URL+ "/lable/hint?text="+text+"&page=1&lines="+ SearchActivity.DEFAULT_HINT_SIZE;
        HomeService.getmOkHttpUtil().get(url,new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                Gson gson = new Gson();
                System.out.println(data);
                List<LableEntity> lableList = gson.fromJson(data,new TypeToken<List<LableEntity>>(){}.getType());
                call.callback(lableList);
            }

            @Override
            public void onError(String msg) {

            }

        });
    }
    /**
     * 获取 热搜
     * @param call
     */
    public static void getHotLable(final HintDataCallback call){
        String url = StaticString.URL+ "/lable/hot?page=1&lines="+ SearchActivity.DEFAULT_HINT_SIZE;
        HomeService.getmOkHttpUtil().get(url,new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                Gson gson = new Gson();
                System.out.println(data);
                List<LableEntity> lableList = gson.fromJson(data,new TypeToken<List<LableEntity>>(){}.getType());
                call.callback(lableList);
            }

            @Override
            public void onError(String msg) {

            }
        });
    }
    /**
     * 搜索成功 添加标签
     */
    public static void searchLable(final String text) {
        final LableEntity lableEntity = new LableEntity();
        lableEntity.setText(text);
        String url = StaticString.URL+"/lable/add";
        HomeService.getmOkHttpUtil().postJson(url, JsonUtil.toJson(lableEntity), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                Log.d("添加标签成功",lableEntity.getText());
            }

            @Override
            public void onError(String msg) {

            }

        });
    }

    public static void searchGoodByTitle(final ArrayList<GoodsEntity> datas,String title){
        String url = StaticString.URL +"/good/search?title="+title+"&page="+SearchActivity.getPage()+"&lines=8";
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                if(null != data){
                    Gson gson = new Gson();
                    List<GoodsEntity> testBean = gson.fromJson(data,new TypeToken<List<GoodsEntity>>(){}.getType());
                    if(null != testBean) {
                        if(SearchActivity.getPage() == 1){
                            datas.clear();
                            datas.addAll(testBean);
                            SearchActivity.getHandler().sendEmptyMessage(UPDATE_OVER);
                        }else {
                            datas.addAll(testBean);
                            SearchActivity.getHandler().sendEmptyMessage(LOAD_OVER);
                        }
                        SearchActivity.setPage(SearchActivity.getPage()+1);
                    }else {
                        if(SearchActivity.getPage() == 1){
                            SearchActivity.getHandler().sendEmptyMessage(UPDATE_OVER);
                        }else {
                            SearchActivity.getHandler().sendEmptyMessage(LOAD_OVER);
                        }
                    }
                }else{
                    System.out.println("no messsage !!!!!!!");
                }

            }

            @Override
            public void onError(String msg) {
                SearchActivity.getHandler().sendEmptyMessage(UPDATE_OVER);
                SearchActivity.getHandler().sendEmptyMessage(UPDATE_OVER);
            }
        });
    }

}
