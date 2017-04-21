package com.example.weina.bishe.controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weina.bishe.R;
import com.example.weina.bishe.entity.CommentEntity;
import com.example.weina.bishe.service.serviceImpl.BaseUserService;
import com.example.weina.bishe.service.serviceImpl.CommentService;
import com.example.weina.bishe.util.view.ScoreView;

import java.util.Date;

/**
 * Created by weina on 2017/3/28.
 */
public class CommentActivity extends AppCompatActivity implements View.OnClickListener{
    public final static String COMMENT_ORDER_BUNDLE = "comment_bundle_order";
    public final static String COMMENT_GOOD_BUNDLE = "comment_goods_order";

    private EditText mEditText;//编辑框内容
    private ScoreView mScoreView;//滑稽币
    private TextView mScoreText;//评分显示
    private Button mButton;//提交按钮
    private TextView mWordCount;
    private static Handler sHandler;
    //回调
    private ScoreView.ChangeCallBack mChangeCallBack;
    //
    private CommentService.CommentServiceCallBack mCommentServiceCallBack;
    //获取数据
    private int orderId;
    private int goodsId;
    private int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.comment_toolbar);
        setSupportActionBar(toolbar);
        SysApplication.getInstance().addActivity(this);
        initData();
        initView();
    }
    private void initData(){
        Intent intent = getIntent();
        orderId = intent.getIntExtra(COMMENT_ORDER_BUNDLE,0);
        goodsId = intent.getIntExtra(COMMENT_GOOD_BUNDLE,0);


        mChangeCallBack = new ScoreView.ChangeCallBack() {
            @Override
            public void callBack() {
                mScoreText.setText(""+mScoreView.getScore()+" 分");
            }
        };
        mCommentServiceCallBack = new CommentService.CommentServiceCallBack() {
            @Override
            public void onCallBack(String data) {
                if("true".equals(data)){
                    showMsg("评论成功");
                    finish();
                }else {
                    showMsg("评论失败");
                }
            }

            @Override
            public void onError() {
                showMsg("网络错误");
            }
        };
        sHandler = new Handler();
    }


    private void initView(){
        mEditText = (EditText) findViewById(R.id.editText);
        mScoreView = (ScoreView) findViewById(R.id.comment_scoreView);
        mScoreText = (TextView) findViewById(R.id.comment_scoreText);
        mButton = (Button) findViewById(R.id.comment_confrim_btn);
        mWordCount = (TextView) findViewById(R.id.comment_wordCount);
        //绑定监听
        mScoreView.setChangeCallBack(mChangeCallBack);
        mEditText.addTextChangedListener(new EditChangedListener());
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.comment_confrim_btn:{
                if(BaseUserService.getGsonLogin().isBoolean()&&number<=200&&number>=10){
                    CommentEntity commentEntity = new CommentEntity();
                    commentEntity.setUserId(BaseUserService.getGsonLogin().getUserEntity().getUserId());
                    commentEntity.setOrderId(orderId);
                    commentEntity.setGoodsId(goodsId);
                    commentEntity.setCommentData(new Date());
                    commentEntity.setContext(mEditText.getText().toString());
                    commentEntity.setStart(mScoreView.getScore());
                    //发送请求
                    CommentService.addComment(commentEntity,mCommentServiceCallBack);

                }else if(number<10){
                    showMsg("最少10个字");
                }else if(number>200){
                    showMsg("超过字数");
                }
            }
        }
    }


    /**
     * 监听内容变化
     */
    private class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            number = charSequence.length();
            mWordCount.setText(""+number);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    private void showMsg(final String msg){
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CommentActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
