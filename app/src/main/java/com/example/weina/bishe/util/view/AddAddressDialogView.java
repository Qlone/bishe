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
import com.example.weina.bishe.service.serviceImpl.AddressService;

/**
 * Created by weina on 2017/3/17.
 */
public class AddAddressDialogView extends Dialog {
    private Context mContext;
    private TextView mAddress;
    private TextView mPhone;
    private TextView mReciver;
    private TextView mMsg;
    private Button mCancel;
    private Button mConfirm;

    public AddAddressDialogView(Context context) {
        super(context);
        this.mContext =context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);//不要标题
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBase();
        initView();
    }

    private void initView() {
        mCancel = (Button) findViewById(R.id.dialog_address_button_cancle);
        mCancel.setOnClickListener(new clickListener());
        mConfirm = (Button) findViewById(R.id.dialog_address_button_confirm);
        mConfirm.setOnClickListener(new clickListener());
        mPhone = (TextView)findViewById(R.id.dialog_address_phone);
        mAddress =(TextView) findViewById(R.id.dialog_address_address);
        mReciver = (TextView)findViewById(R.id.dialog_address_reciver);
        mMsg =(TextView)findViewById(R.id.dialog_address_message);
    }

    private void initBase(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.address_addnew_view, null);
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

    private class clickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.dialog_address_button_cancle:{
                    onBackPressed();
                    break;
                }
                case R.id.dialog_address_button_confirm:{
                    String address= mAddress.getText().toString();
                    String phone= mPhone.getText().toString();
                    String reciver =mReciver.getText().toString();
                    AddressService.addAddress(address,phone,reciver);
                    break;
                }
            }
        }
    }
    public void setMsg(String msg){
        mMsg.setText(msg);
    }
}
