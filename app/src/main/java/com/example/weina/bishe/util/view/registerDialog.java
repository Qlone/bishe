package com.example.weina.bishe.util.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weina.bishe.R;
import com.example.weina.bishe.controller.MainActivity;
import com.example.weina.bishe.entity.UserEntity;
import com.example.weina.bishe.service.serviceImpl.BaseUserService;

/**
 * Created by weina on 2017/4/26.
 */
public class RegisterDialog extends Dialog implements View.OnClickListener{
    private Context mContext;

    private TextView userNameView;
    private TextView pswView1;
    private TextView pswVIew2;

    private TextView tips;

    //按钮
    private Button mCancle;
    private Button mConfirm;
    //其他
    private GifWaitBg mGifWaitBg;
    public  RegisterDialog(Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);//不要标题
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBase();
        initView();
    }

    //初始化页面
    private void initView(){
        userNameView = (TextView) findViewById(R.id.register_name);
        pswView1 = (TextView) findViewById(R.id.register_psw1);
        pswVIew2 = (TextView) findViewById(R.id.register_psw2);
        tips = (TextView) findViewById(R.id.register_tips);
        //按钮
        mCancle = (Button) findViewById(R.id.register_button_cancle);
        mConfirm =(Button) findViewById(R.id.register_button_confirm);
        mCancle.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mGifWaitBg = (GifWaitBg) findViewById(R.id.register_wait);
        mGifWaitBg.setGifGone();
    }


    private void initBase(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_register, null);
        setContentView(view);
//        this.setCanceledOnTouchOutside(false);//点击空白不消失
//        OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode== KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
//                {
//                    return true;
//                }
//                else
//                {
//                    return false;
//                }
//            }
//        };
//        setOnKeyListener(keylistener);
//        setCancelable(false);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.height=(int)(d.heightPixels*0.5);
        lp.width = (int) (d.widthPixels * 0.9); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.register_button_cancle:{
                onBackPressed();
                tips.setText("");
                break;
            }
            case R.id.register_button_confirm:{
                String userName = userNameView.getText().toString();
                String psw1 = pswView1.getText().toString();
                String psw2 = pswVIew2.getText().toString();
                if(null== userName||"".equals(userName)){
                    tips.setText("用户名不能为空");
                }else if(null ==psw1||"".equals(psw1)){
                    tips.setText("密码不能为空");
                }else if(null==psw2||"".equals(psw2)){
                    tips.setText("请确认密码");
                }else if(!psw1.equals(psw2)){
                    tips.setText("两次密码不同");
                }else {
                    UserEntity userEntity = new UserEntity();
                    userEntity.setUserName(userName);
                    userEntity.setPassword(psw1);
                    userEntity.setPayPassword(000000);
                    mGifWaitBg.setGifShow();
                    BaseUserService.getInstatnce().rgisterUser(userEntity, new BaseUserService.RegisterCallBack() {
                        @Override
                        public void onSuccess(String data) {
                            if("exits".equals(data)){
                                tipUI("用户已经存在");
                            }else if("fail".equals(data)){
                                tipUI("创建失败");
                            }else if("success".equals(data)){
                                tipUI("创建成功，支付密码默认000000");
                            }else {
                                tipUI("未知错误");
                            }
                        }

                        @Override
                        public void onFail() {
                            tipUI("网络连接失败");
                        }
                    });
                }
            }
        }
    }

    private void setMsg(String msg){
        Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
    }
    private void tipUI(final String msg){
        MainActivity.getHandle().post(new Runnable() {
            @Override
            public void run() {
                tips.setText(msg);
                mGifWaitBg.setGifGone();
            }
        });
    }
}
