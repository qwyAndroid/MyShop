package shop.qwy.com.myshop;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.Serializable;

import dmax.dialog.SpotsDialog;
import shop.qwy.com.myshop.bean.Wears;
import shop.qwy.com.myshop.utlis.CartProvider;
import shop.qwy.com.myshop.utlis.ToastUtils;
import shop.qwy.com.myshop.widget.MyToolbar;

/**
 * created by qwyAndroid on 2016/9/28
 */
public class WearDetailActivity extends AppCompatActivity{

    private WebView mWebView;
    private MyToolbar mToolBar;

    private Wears mWear;

    private CartProvider cartProvider;
    private WebInteface mWebInteface;

    private SpotsDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ware_detail);

        Serializable  serializable= getIntent().getSerializableExtra(Contants.WARE);
        if(serializable ==null) {
            this.finish();
        }else{
            mWear = (Wears) serializable;
        }

        cartProvider = new CartProvider(this);
        mWebView = (WebView) findViewById(R.id.webView);
        mToolBar = (MyToolbar) findViewById(R.id.toolbar);

        mDialog = new SpotsDialog(this,"loading....");
        mDialog.show();

        initWebView();
        initToolbar();
    }

    private void initToolbar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WearDetailActivity.this.finish();
            }
        });
    }

    private void initWebView() {

        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setAppCacheEnabled(true);

        mWebView.loadUrl(Contants.API.WARES_DETAIL);
        mWebInteface = new WebInteface(this);
        mWebView.addJavascriptInterface(mWebInteface,"appInterface");
        mWebView.setWebViewClient(new WClient());
    }

    class  WClient extends WebViewClient {


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);


            if(mDialog !=null && mDialog.isShowing())
                mDialog.dismiss();

            mWebInteface.showDetail();


        }
    }
    class WebInteface {
        private Context mContext;
        public WebInteface(Context context) {
               this.mContext = context;
        }

        public  void showDetail(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mWebView.loadUrl("javascript:showDetail("+mWear.getId()+")");

                }
            });
        }

        @JavascriptInterface
        public void buy(long id){
            cartProvider.put(mWear);
            ToastUtils.show(mContext,"已添加到购物车");
        }

        @JavascriptInterface
        public void addFavorites(long id){
        }
    }
}
