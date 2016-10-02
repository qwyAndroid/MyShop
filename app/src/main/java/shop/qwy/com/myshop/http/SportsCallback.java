package shop.qwy.com.myshop.http;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.util.Date;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;
import shop.qwy.com.myshop.LoginActivity;
import shop.qwy.com.myshop.MyApplication;
import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.utlis.ToastUtils;

/**
 * created by qwyAndroid on 2016/9/22
 */
public abstract class SportsCallback<T> extends BaseCallBack<T>{

    private Context mContext;
    private SpotsDialog mDialog;
    public SportsCallback(Context context) {
        mContext = context;
        initSpotDialog();
    }

    private void initSpotDialog() {
        mDialog = new SpotsDialog(mContext,"拼命加载中...");
    }

    public  void showDialog(){
        mDialog.show();
    }

    public  void dismissDialog(){
        mDialog.dismiss();
    }
    public void setLoadMessage(int resId){
        mDialog.setMessage(mContext.getString(resId));
    }
    @Override
    public void onBeforeRequest(Request request) {
        showDialog();
//        Log.e("TAG",new Date()+"");
    }

    @Override
    public void onFailure(Request request, IOException e) {
        dismissDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
//        Log.e("TAG",new Date()+"");
    }

    @Override
    public void onTokenError(Response response, int code) {
        ToastUtils.show(mContext, mContext.getString(R.string.token_error));

        Intent intent = new Intent();
        intent.setClass(mContext, LoginActivity.class);
        mContext.startActivity(intent);

        MyApplication.getInstance().clearUser();

    }
}
