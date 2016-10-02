package shop.qwy.com.myshop.http;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import shop.qwy.com.myshop.MyApplication;

/**
 * created by qwyAndroid on 2016/9/21
 */
public class OkHttpHelper {

    private static OkHttpHelper mInstance;
    private OkHttpClient mHttpClient;
    private Gson mGson;
    private Handler mHandler;

    public static final int TOKEN_MISSING=401;// token 丢失
    public static final int TOKEN_ERROR=402; // token 错误
    public static final int TOKEN_EXPIRE=403; // token 过期

    static {
        mInstance = new OkHttpHelper();
    }
    private OkHttpHelper() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS);

        mHttpClient = builder.build();
        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpHelper getInstance(){

        return mInstance;
    }

    public void get(String url,BaseCallBack callBack){
        get(url,null,callBack);
    }
    public void get(String url,Map<String,String> parmars,BaseCallBack callBack){
        Request request = builderGetRequest(url,parmars);
        doRequest(request,callBack);
    }
    private Request builderGetRequest(String url,Map<String,String> parmars) {

        return builderRequest(url,HttpMethodType.GET,parmars);
    }

    public void post(String url,Map<String,String> parmars,BaseCallBack callBack){
        Request request = builderPostRequest(url, parmars);
        doRequest(request,callBack);
    }

    private Request builderPostRequest(String url, Map<String, String> parmars) {
        return builderRequest(url,HttpMethodType.POST,parmars);
    }

    private Request builderRequest(String url,HttpMethodType type,Map<String,String> parmars) {
        Request.Builder builder = new Request.Builder()
                .url(url);
        if (type == HttpMethodType.GET){

            url = buildUrlParams(url,parmars);
            builder.url(url);

            builder.get();
        }else if (type == HttpMethodType.POST){
            RequestBody body = builderFromData(parmars);
            builder.post(body);
        }
        return  builder.build();
    }

    /**
     * init body of method post
     * @param parmars
     * @return
     */
    private RequestBody builderFromData(Map<String,String> parmars) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String,String> entry : parmars.entrySet()){
            builder.add(entry.getKey(),entry.getValue());
        }
        String token = MyApplication.getInstance().getToken();
        if (!TextUtils.isEmpty(token)){
            builder.add("token",token);
        }
        return builder.build();
    }

    enum HttpMethodType{
        GET,
        POST
    }

    public void doRequest(final Request request, final BaseCallBack callBack){
        callBack.onBeforeRequest(request);

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(request,e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                callBack.onResponse(response);
                callbackResponse(callBack,response);
                if (response.isSuccessful()){
                    String resultStr = response.body().string();
                    if (callBack.mType == String.class){
                        callBack.onSuccess(response,String.class);
                    }else{

                        try{
                            Object obj = mGson.fromJson(resultStr, callBack.mType);
                            callBackSuccess(callBack,response,obj);
                        }catch (JsonParseException e){
                           callbackError(callBack,response,e);
                        }
                    }
                }
                else if(response.code() == TOKEN_ERROR||response.code() == TOKEN_EXPIRE ||response.code() == TOKEN_MISSING ){

                    callbackTokenError(callBack,response);
                }
                else{
                    callBack.onError(response,response.code(),null);
                }
            }
        });
    }

    private void callbackTokenError(final  BaseCallBack callback , final Response response ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response,response.code());
            }
        });
    }
    private void callBackSuccess(final BaseCallBack callBack, final Response response, final Object obj) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callBack.onSuccess(response,obj);
            }
        });
    }

    private void callbackError(final  BaseCallBack callback , final Response response, final Exception e ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response,response.code(),e);
            }
        });
    }

    private void callbackResponse(final  BaseCallBack callback , final Response response ){

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }

    private   String buildUrlParams(String url ,Map<String,String> params) {

        if(params == null)
            params = new HashMap<>(1);

        String token = MyApplication.getInstance().getToken();
        if(!TextUtils.isEmpty(token))
            params.put("token",token);


        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }

        if(url.indexOf("?")>0){
            url = url +"&"+s;
        }else{
            url = url +"?"+s;
        }

        return url;
    }
}
