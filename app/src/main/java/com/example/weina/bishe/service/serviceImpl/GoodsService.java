package com.example.weina.bishe.service.serviceImpl;

import android.util.Log;

import com.example.weina.bishe.bean.GsonSort;
import com.example.weina.bishe.bean.GsonSortApply;
import com.example.weina.bishe.controller.GoodDetailActivity;
import com.example.weina.bishe.controller.SearchActivity;
import com.example.weina.bishe.entity.GoodsEntity;
import com.example.weina.bishe.service.IGoodsService;
import com.example.weina.bishe.service.ISearchService;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.JsonUtil;
import com.example.weina.bishe.util.OkHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weina on 2017/3/21.
 */
public class GoodsService implements IGoodsService {

    public interface GoodsCallBack{
        void onSuccess(String data);
        void onError();
    }

    public static void getGoodsDetail(int goodsId,final List<GoodsEntity> datas){
        String url = StaticString.URL +"/good/getItem?goodsId="+goodsId;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                if(null!=data){
                    Gson gson = new Gson();
                    List<GoodsEntity> testBean = gson.fromJson(data, new TypeToken<List<GoodsEntity>>() {}.getType());
                    if(null!=testBean){
                        datas.clear();
                        datas.addAll(testBean);
                        GoodDetailActivity.getmHandler().sendEmptyMessage(GoodDetailActivity.UPDATA_OVER);
                    }
                }
            }
            @Override
            public void onError(String msg) {
                GoodDetailActivity.getmHandler().sendEmptyMessage(GoodDetailActivity.UPDATA_OVER);
            }
        });
    }
    public static void getSort(final List<String> typeData, final List<String> sortData, final GoodsCallBack callBack){
        String url= StaticString.URL+"/good/getSort";

        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                if(null!=data){
                    Gson gson = new Gson();
                    GsonSort gsonSort = gson.fromJson(data,GsonSort.class);
                    if(null!=gsonSort){
                        typeData.addAll(gsonSort.getDataType());
                        sortData.addAll(gsonSort.getDataSort());
                        callBack.onSuccess(data);
                    }else {
                        callBack.onError();
                    }
                }else {
                    callBack.onError();
                }
            }

            @Override
            public void onError(String msg) {
                callBack.onError();
                Log.d("sortUpdata","fail connect"+msg);
            }
        });

    }


    public static void highSearch(final ArrayList<GoodsEntity> datas, GsonSortApply gsonSortApply){
        String url = StaticString.URL +"/good/highSearch";
        gsonSortApply.setPage(SearchActivity.getPage());
        gsonSortApply.setLines(8);
        HomeService.getmOkHttpUtil().postJson(url, JsonUtil.toJson(gsonSortApply), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                if(null != data){
                    Gson gson = new Gson();
                    List<GoodsEntity> testBean = gson.fromJson(data,new TypeToken<List<GoodsEntity>>(){}.getType());
                    if(null != testBean) {
                        if(SearchActivity.getPage() == 1){
                            datas.clear();
                            datas.addAll(testBean);
                            SearchActivity.getHandler().sendEmptyMessage(ISearchService.UPDATE_OVER);
                        }else {
                            datas.addAll(testBean);
                            SearchActivity.getHandler().sendEmptyMessage(ISearchService.LOAD_OVER);
                        }
                        SearchActivity.setPage(SearchActivity.getPage()+1);
                    }else {
                        if(SearchActivity.getPage() == 1){
                            SearchActivity.getHandler().sendEmptyMessage(ISearchService.UPDATE_OVER);
                        }else {
                            SearchActivity.getHandler().sendEmptyMessage(ISearchService.LOAD_OVER);
                        }
                    }
                }else{
                    System.out.println("no messsage !!!!!!!");
                }

            }

            @Override
            public void onError(String msg) {
                SearchActivity.getHandler().sendEmptyMessage(ISearchService.UPDATE_OVER);
                SearchActivity.getHandler().sendEmptyMessage(ISearchService.UPDATE_OVER);
            }
        });
    }
}
