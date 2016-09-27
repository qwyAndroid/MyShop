package shop.qwy.com.myshop.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import shop.qwy.com.myshop.R;

/**
 * created by qwyAndroid on 2016/9/25
 */
public class AddAndSubView extends LinearLayout implements View.OnClickListener{

    private LayoutInflater mInflate;

    private Button btnAdd;
    private Button btnSub;
    private TextView textView;

    private int minValue;
    private int maxValue;
    public OnButtonClickListener mOnButtonClickListener;

    private int mTextNum = 1;
    public AddAndSubView(Context context) {
        this(context,null);
    }

    public AddAndSubView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AddAndSubView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();

        if (attrs != null){
            TintTypedArray a = TintTypedArray.obtainStyledAttributes
                    (context,attrs, R.styleable.NumberAddSubView,defStyleAttr,0);
            int val = a.getInt(R.styleable.NumberAddSubView_value,0);
            setmTextNum(val);

            int minVal =  a.getInt(R.styleable.NumberAddSubView_minValue,0);
            setMinValue(minVal);

            int maxVal =  a.getInt(R.styleable.NumberAddSubView_maxValue,0);
            setMaxValue(maxVal);

            Drawable drawableBtnAdd =a.getDrawable(R.styleable.NumberAddSubView_btnAddBackground);
            Drawable drawableBtnSub =a.getDrawable(R.styleable.NumberAddSubView_btnSubBackground);
            Drawable drawableTextView =a.getDrawable(R.styleable.NumberAddSubView_textViewBackground);

            setButtonAddBackgroud(drawableBtnAdd);
            setButtonSubBackgroud(drawableBtnSub);
            setTexViewtBackground(drawableTextView);

            a.recycle();
        }

    }



    private void setTexViewtBackground(Drawable drawableTextView) {
        textView.setBackgroundDrawable(drawableTextView);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setButtonAddBackgroud(Drawable drawableBtnAdd) {
        btnAdd.setBackground(drawableBtnAdd);
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setButtonSubBackgroud(Drawable drawableBtnSub) {
        btnSub.setBackground(drawableBtnSub);
    }


    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.mOnButtonClickListener = onButtonClickListener;
    }

    private void initView() {
        mInflate = LayoutInflater.from(getContext());
        View view = mInflate.inflate(R.layout.add_sub_view, this, true);

        btnAdd = (Button) view.findViewById(R.id.btn_add);
        btnSub = (Button) view.findViewById(R.id.btn_sub);
        textView = (TextView) view.findViewById(R.id.text);

        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);


    }

    public int addNum(){
        if (maxValue != 0) {
            if (mTextNum < maxValue) {
                return mTextNum += 1;
            }
            return maxValue;
        }else{
            return mTextNum +=1;
        }
    }

    public int subNum(){
        if (mTextNum > minValue) {
            return mTextNum -= 1;
        }
        return minValue;
    }


    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getmTextNum() {
        String value = textView.getText().toString();
        if (TextUtils.isEmpty(value)) {
            mTextNum = Integer.parseInt(value);
        }
        return mTextNum;
    }

    public void setmTextNum(int mTextNum) {
        textView.setText(mTextNum+"");
        this.mTextNum = mTextNum;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                textView.setText(addNum()+"");
                if (mOnButtonClickListener != null){
                    mOnButtonClickListener.onClickAddNum(v,mTextNum);
                }
                break;
            case R.id.btn_sub:
                textView.setText(subNum()+"");
                if (mOnButtonClickListener != null){
                    mOnButtonClickListener.onClickSubNum(v,mTextNum);
                }
                break;

        }
    }
    public interface OnButtonClickListener{
        void onClickAddNum(View v, int value);
        void onClickSubNum(View v, int value);
    }
}
