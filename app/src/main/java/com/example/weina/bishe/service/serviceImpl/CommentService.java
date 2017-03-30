package com.example.weina.bishe.service.serviceImpl;

import com.example.weina.bishe.controller.CommentListActivity;
import com.example.weina.bishe.entity.CommentEntity;
import com.example.weina.bishe.service.ICommentService;
import com.example.weina.bishe.service.StaticString;
import com.example.weina.bishe.util.JsonUtil;
import com.example.weina.bishe.util.OkHttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weina on 2017/3/29.
 */
public class CommentService implements ICommentService {

    public interface CommentServiceCallBack{
        void onCallBack(String data);
        void onError();
    }

    public static void addComment(CommentEntity commentEntity, final CommentServiceCallBack commentServiceCallBack){
        String url = StaticString.URL+"/comment/add";
        HomeService.getmOkHttpUtil().postJson(url, JsonUtil.toJson(commentEntity), new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                commentServiceCallBack.onCallBack(data);
            }

            @Override
            public void onError(String msg) {
                commentServiceCallBack.onError();
            }
        });
    }

    /**
     * 获取分数
     */
    public static void getScore(int goodsId,final CommentServiceCallBack commentServiceCallBack){
        String url = StaticString.URL+"/comment/getScore?goodsId="+goodsId;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                commentServiceCallBack.onCallBack(data);
            }
            @Override
            public void onError(String msg) {
                commentServiceCallBack.onError();
            }
        });
    }
    public static void getCommentCount(int goodsId,final CommentServiceCallBack commentServiceCallBack){
        String url = StaticString.URL+"/comment/commentCount?goodsId="+goodsId;
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                commentServiceCallBack.onCallBack(data);
            }
            @Override
            public void onError(String msg) {
                commentServiceCallBack.onError();
            }
        });
    }

    public static void getCommentList(int goodsId, final ArrayList<CommentEntity> datas){
        String url = StaticString.URL+"/comment/list?goodsId="+goodsId
                +"&page="+ CommentListActivity.getPage()
                +"&lines="+CommentListActivity.getLines();
        HomeService.getmOkHttpUtil().get(url, new OkHttpUtil.HttpCallback() {
            @Override
            public void onSuccess(String data){
                if(null != data) {
                    Gson gson = new Gson();
                    List<CommentEntity> testBean = gson.fromJson(data, new TypeToken<List<CommentEntity>>() {}.getType());
                    if(null != testBean) {
                        if(CommentListActivity.getPage() == 1){
                            datas.clear();
                            datas.addAll(testBean);
                            CommentListActivity.getHandler().sendEmptyMessage(CommentListActivity.HANLE_UPDATA_OVER);
                        }else {
                            datas.addAll(testBean);
                            CommentListActivity.getHandler().sendEmptyMessage(CommentListActivity.HANLE_LOAD_OVER);
                        }
                        CommentListActivity.setPage(CommentListActivity.getPage()+1);
                    }else {
                        if(CommentListActivity.getPage() == 1){
                            CommentListActivity.getHandler().sendEmptyMessage(CommentListActivity.HANLE_UPDATA_OVER);
                        }else {
                            CommentListActivity.getHandler().sendEmptyMessage(CommentListActivity.HANLE_LOAD_OVER);
                        }
                    }
                }
            }
            @Override
            public void onError(String msg) {
                CommentListActivity.getHandler().sendEmptyMessage(CommentListActivity.HANLE_UPDATA_OVER);
                CommentListActivity.getHandler().sendEmptyMessage(CommentListActivity.HANLE_LOAD_OVER);
            }
        });
    }
}
