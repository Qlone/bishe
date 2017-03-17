package com.example.weina.bishe.util.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weina.bishe.R;
import com.example.weina.bishe.service.serviceImpl.BaseUserService;

/**
 * Created by weina on 2017/3/17.
 */
public class AddressConfirmView extends Dialog {
    /**
     *  SharedPreference
     */
    private static final String ADDRESS_SP = "addresSp";
    private static final String ADDRESS_NAME = "address";
    private static final String PHONE_NAME = "phone";

    private Context mContext;
    /**
     * 地址和 电话
     */
    private String mAddress;
    private String mPhone;

    private ImageButton mCancel;
    private TextView mAddressView;
    private TextView mPhoneView;
    private TextView mAmountView;
    private TextView mPriceView;

    private Button mChangeAddress;
    private Button mConfirm;
    private AddressCallBack mAddressCallBack;

    public interface AddressCallBack{
        void confirm();
        void change();
    }

    public AddressConfirmView(Context context) {
        super(context);
        this.mContext =context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);//不要标题

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initBase();
        initView();
    }
    private void initData(){
        getAddressAndPhone();//获取缓存的地址和号码
    }
    private void initView(){
        mCancel = (ImageButton) findViewById(R.id.view_address_cancel);
        mConfirm = (Button) findViewById(R.id.view_adddress_confirm);
        mChangeAddress = (Button) findViewById(R.id.view_address_changeAddress);
        mAddressView = (TextView) findViewById(R.id.view_address_address);
        mPhoneView = (TextView) findViewById(R.id.view_address_phone);
        mAddressView.setText(" 购物地址 :"+mAddress);
        mPhoneView.setText(" 收货电话 :"+mPhone);
        mCancel.setOnClickListener(new clickListener());
        mConfirm.setOnClickListener(new clickListener());
        mChangeAddress.setOnClickListener(new clickListener());
    }

    private void initBase(){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.view_order_confirm_dialog, null);
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
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
    }

    private class clickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.view_address_cancel:{
                    onBackPressed();
                    break;
                }
                case R.id.view_adddress_confirm:{
                    mAddressCallBack.confirm();
                    setAddressAndPhone("西电海棠5号楼","22",mContext);
                    break;
                }
                case R.id.view_address_changeAddress:{
                    mAddressCallBack.change();
                    break;
                }
            }
        }
    }

    public void setAddressCallBack(AddressCallBack addressCallBack) {
        mAddressCallBack = addressCallBack;
    }
    /**
     * 获取 上一次的 地址 手机号， 保存上一次的地址和手机号
     */
    public void getAddressAndPhone(){
        String userId = userId = String.valueOf(BaseUserService.getGsonLogin().getUserEntity().getUserId());
        //步骤1：创建一个SharedPreferences接口对象
        SharedPreferences read = mContext.getSharedPreferences(ADDRESS_SP, mContext.MODE_PRIVATE);
        //步骤2：获取文件中的值
        mAddress = read.getString(ADDRESS_NAME+userId, "");
        mPhone = read.getString(PHONE_NAME+userId,"");
    }
    public static void  setAddressAndPhone(String address,String phone,Context context){
        String userId = String.valueOf(BaseUserService.getGsonLogin().getUserEntity().getUserId());
        //步骤2-1：创建一个SharedPreferences.Editor接口对象，lock表示要写入的XML文件名，MODE_WORLD_WRITEABLE写操作
        SharedPreferences.Editor editor = context.getSharedPreferences(ADDRESS_SP, context.MODE_PRIVATE).edit();
        //步骤2-2：将获取过来的值放入文件
        editor.putString(ADDRESS_NAME+userId, address);
        editor.putString(PHONE_NAME+userId,phone);
        //步骤3：提交
        editor.commit();
    }
}
