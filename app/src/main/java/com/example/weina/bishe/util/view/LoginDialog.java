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
import android.widget.EditText;
import android.widget.TextView;

import com.example.weina.bishe.R;

/**
 * Created by weina on 2017/3/1.
 */
public class LoginDialog extends Dialog {
    private EditText mUserName;
    private EditText mPassword;
    private TextView mMessage;
    private Button mConfirm;
    private Button mCancel;
    private Context context;
    private String username;
    private ClickListenerInterface clickListenerInterface;

     public interface ClickListenerInterface {
         public void doConfirm(String username,String password);

         public void doCancel();
     }


    public LoginDialog(Context context) {
        super(context);
        this.context= context;

    }
    public LoginDialog(Context context,String username) {
        super(context);
        this.context= context;
        this.username = username;
    }
    public LoginDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context= context;
    }

    protected LoginDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context= context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
     }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.login_layout, null);
        setContentView(view);

        //获取  view
        mUserName = (EditText)view.findViewById(R.id.login_text_username);
        mPassword = (EditText)view.findViewById(R.id.login_text_password);
        mMessage = (TextView) view.findViewById(R.id.login_text_message);
        if(null != username){
            mUserName.setText(username);
        }

        mConfirm = (Button) view.findViewById(R.id.login_button_confirm);
        mCancel = (Button)view.findViewById(R.id.login_button_cancle);
        mConfirm.setOnClickListener(new clickListener());
        mCancel.setOnClickListener(new clickListener());
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
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.height=(int)(d.heightPixels*0.5);
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
      }

    public void setClickListenerInterface(ClickListenerInterface clickListenerInterface) {
        this.clickListenerInterface = clickListenerInterface;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.login_button_confirm: {
                    mMessage.setText(" signing ......");
                    clickListenerInterface.doConfirm(mUserName.getText().toString(),mPassword.getText().toString());
                    break;
                }
                case R.id.login_button_cancle:
                    clickListenerInterface.doCancel();
                    onBackPressed();
                    break;
            }
        }

    };
    public void setmMessage(String message){
        mMessage.setText(message);
    }
}
