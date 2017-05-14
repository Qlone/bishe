package com.example.weina.bishe.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weina.bishe.R;
import com.example.weina.bishe.service.StaticString;

/**
 * Created by weina on 2017/5/12.
 */
public class SetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mText;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        SysApplication.getInstance().addActivity(this);
        initView();
        initData();
    }

    private void initData(){
        mText.setText(StaticString.URL);
    }
    private void initView(){
        mText = (EditText) findViewById(R.id.setIp);
        mButton = (Button) findViewById(R.id.setComfirm);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.setComfirm:{
                onConfirm();
                break;
            }
        }
    }
    private void onConfirm(){
        String msg = mText.getText().toString();
        if(!msg.equals("")){
            StaticString.URL = msg;
            Toast.makeText(SetActivity.this,"设置成功",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(SetActivity.this,"ip字段不能为空",Toast.LENGTH_SHORT).show();
        }
    }
}
