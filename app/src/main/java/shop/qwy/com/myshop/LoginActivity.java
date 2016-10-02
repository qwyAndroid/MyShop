package shop.qwy.com.myshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;
import shop.qwy.com.myshop.bean.User;
import shop.qwy.com.myshop.http.OkHttpHelper;
import shop.qwy.com.myshop.http.SportsCallback;
import shop.qwy.com.myshop.msg.LoginRespMsg;
import shop.qwy.com.myshop.utlis.DESUtil;
import shop.qwy.com.myshop.utlis.ToastUtils;
import shop.qwy.com.myshop.widget.ClearEditText;
import shop.qwy.com.myshop.widget.MyToolbar;

/**
 * created by qwyAndroid on 2016/10/1
 */
public class LoginActivity extends AppCompatActivity{

    @ViewInject(R.id.toolbar)
    private MyToolbar mToolBar;
    @ViewInject(R.id.etxt_phone)
    private ClearEditText mEtxtPhone;
    @ViewInject(R.id.etxt_pwd)
    private ClearEditText mEtxtPwd;

    private OkHttpHelper mHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewUtils.inject(this);

    }

    @OnClick(R.id.btn_login)
    private void login(View view){
        String phone = mEtxtPhone.getText().toString().trim();
        if(TextUtils.isEmpty(phone)){
            ToastUtils.show(this, "请输入手机号码");
            return;
        }

        String pwd = mEtxtPwd.getText().toString().trim();
        if(TextUtils.isEmpty(pwd)){
            ToastUtils.show(this,"请输入密码");
            return;
        }
        Map<String,String> params = new HashMap<>(2);
        params.put("phone",phone);
        params.put("password", DESUtil.encode(Contants.DES_KEY,pwd));

        mHelper.post(Contants.API.LOGIN, params, new SportsCallback<LoginRespMsg<User>>(this) {
            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                MyApplication application = MyApplication.getInstance();
                application.putUser(userLoginRespMsg.getData(),userLoginRespMsg.getToken());
                if(application.getIntent() == null){
                    setResult(RESULT_OK);
                    finish();
                }else{

                    application.jumpToTargetActivity(LoginActivity.this);
                    finish();

                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }
}
