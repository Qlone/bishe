package com.example.weina.bishe.util.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weina.bishe.R;

/**
 * Created by weina on 2017/3/10.
 */
public class ChooseNumberView extends LinearLayout {
    private Context mContext;
    private ImageButton mBtnMinus;
    private ImageButton mBtnPlus;
    private EditText mEditText;
    private TextView mTextView;

    private InputMethodManager mInputMethodManager ;
    private int number;
    private int maxNumber;
    private final static int MIN_NUMBER =1;
    public ChooseNumberView(Context context,AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.view_choose_number, this);
        this.mContext =context;
        initData();
        initView();
    }
    private void initData(){
        number=MIN_NUMBER;
        maxNumber=MIN_NUMBER;
        mInputMethodManager =(InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    private void initView(){
        mBtnMinus = (ImageButton) findViewById(R.id.view_choose_btn_minus);
        mBtnMinus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(number-1>=MIN_NUMBER) {
                    number--;
                    mEditText.setText(""+number);
                }else {
                    number =MIN_NUMBER;
                    mEditText.setText(""+MIN_NUMBER);
                }
                mEditText.setFocusable(false);
                mInputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);//
            }
        });

        mBtnPlus = (ImageButton)findViewById(R.id.view_choose_btn_plus);
        mBtnPlus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(number+1 <= maxNumber){
                    number++;
                    mEditText.setText(""+number);
                }else{
                    number=maxNumber;
                    mEditText.setText(""+maxNumber);
                }
                mEditText.setFocusable(false);
                mInputMethodManager.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);//
            }
        });

        mEditText = (EditText) findViewById(R.id.view_choose_text);
        mEditText.addTextChangedListener(new EditChangedListener());
        mEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("点击了 view","");
                mEditText.setFocusable(true);//设置输入框可聚集
                mEditText.setFocusableInTouchMode(true);//设置触摸聚焦
                mEditText.requestFocus();//请求焦点
                mEditText.findFocus();//获取焦点
                mInputMethodManager.showSoftInput(mEditText, InputMethodManager.SHOW_FORCED);// 显示输入法
            }
        });

        mTextView = (TextView) findViewById(R.id.view_choose_text_stock);
        setMaxNumber(maxNumber);
        setNumber(MIN_NUMBER);
        mEditText.setFocusable(false);

    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
        mTextView.setText("库存数量  "+ maxNumber);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        mEditText.setText(""+number);
    }
    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            try {
                if (null != charSequence && !"".equals(charSequence.toString())) {
                    number = Integer.parseInt(charSequence.toString());
                    if (number > maxNumber) {
                        number = maxNumber;
                        mEditText.setText("" + number);
                    } else if (number < MIN_NUMBER) {
                        number = MIN_NUMBER;
                        mEditText.setText("" + number);
                    }
                } else {
                    number = MIN_NUMBER;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
