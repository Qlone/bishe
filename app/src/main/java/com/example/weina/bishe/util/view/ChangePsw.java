package com.example.weina.bishe.util.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.service.serviceImpl.BaseUserService;

/**
 * Created by weina on 2017/4/13.
 */
public class ChangePsw extends Dialog {
    private Context mContext;
    private TextView mOld;
    private TextView mNew;
    private TextView mNew2;
    private TextView mMsg;
    private Button mCancel;
    private Button mConfirm;
    private boolean isPayPassword;

    private ChangePswCallBack mChangePswCallBack;
    public ChangePsw(Context context) {
        super(context);
        this.mContext =context;
        isPayPassword=true;
        requestWindowFeature(Window.FEATURE_NO_TITLE);//不要标题
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBase();
        initView();
    }

    private void initBase(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.chang_password, null);
        setContentView(view);
        this.setCanceledOnTouchOutside(false);//点击空白不消失
        OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode== KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        };
        setOnKeyListener(keylistener);
//        setCancelable(false);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.height=(int)(d.heightPixels*0.5);
        lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }
    private void initView(){
        mCancel = (Button) findViewById(R.id.dialog_chang_button_cancle);
        mCancel.setOnClickListener(new clickListener());
        mConfirm = (Button) findViewById(R.id.dialog_chang_button_confirm);
        mConfirm.setOnClickListener(new clickListener());
        mOld = (TextView)findViewById(R.id.dialog_chang_chang);
        mNew =(TextView) findViewById(R.id.dialog_chang_phone);
        mNew2 = (TextView)findViewById(R.id.dialog_chang_reciver);
        mMsg =(TextView)findViewById(R.id.dialog_chang_message);
        if(isPayPassword) {
            setMsg("修改支付密码");
        }else{
            setMsg("修改登录密码");
        }
    }

    private class clickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.dialog_chang_button_cancle:{
                    onBackPressed();
                    break;
                }
                case R.id.dialog_chang_button_confirm:{
                    if(checkPassword()){
                        submitPsw();
                    }
                    break;
                }
            }
        }
    }
    private boolean checkPassword(){
        String old = mOld.getText().toString();
        String newPsw = mNew.getText().toString();
        String newPsw2 = mNew2.getText().toString();
        if(null == old || null == newPsw || null == newPsw2 || "".equals(old) || "".equals(newPsw) || "".equals(newPsw2)){
            setMsg("输入框不能为空");
            return false;
        }else if(!newPsw.equals(newPsw2)){
            setMsg("密码不相同");
            return false;
        }else if(isPayPassword){
            try {
                if(newPsw.length() != 6){
                    setMsg("密码长度为6位");
                    return false;
                }
                int psw = Integer.parseInt(newPsw);
                int iOld = Integer.parseInt(old);
                return true;
            }catch (Exception e){
                setMsg("密码只能是数字");
                return false;
            }
        }
        setMsg("");
        return true;
    }

    private void submitPsw(){
        BaseUserService.ChangPswCallBack callBack = new BaseUserService.ChangPswCallBack() {
            @Override
            public void onSuccess() {
                if(null!=mChangePswCallBack){
                    mChangePswCallBack.success();
                    onBackPressed();
                }
            }

            @Override
            public void onFail() {
                if(null!=mChangePswCallBack){
                    mChangePswCallBack.fail();
                    onBackPressed();
                }
            }
        };
        String sOld = mOld.getText().toString();
        String sNew= mNew.getText().toString();
        if(isPayPassword){
            BaseUserService.changPayPassword(Integer.parseInt(sOld),Integer.parseInt(sNew),callBack);
        }else{
            BaseUserService.changPassword(sOld,sNew,callBack);
        }

    }
    public interface ChangePswCallBack{
        void success();
        void fail();
    }

    public void setMsg(String msg){
        mMsg.setText(msg);
    }

    public boolean isPayPassword() {
        return isPayPassword;
    }
    @Override
    public void show(){
        super.show();
        mOld.setText("");
        mNew.setText("");
        mNew2.setText("");
        if(isPayPassword) {
            setMsg("修改支付密码");
        }else{
            setMsg("修改登录密码");
        }
    }

    public void setPayPassword(boolean payPassword) {
        isPayPassword = payPassword;
    }

    public void setChangePswCallBack(ChangePswCallBack changePswCallBack) {
        mChangePswCallBack = changePswCallBack;
    }
}
