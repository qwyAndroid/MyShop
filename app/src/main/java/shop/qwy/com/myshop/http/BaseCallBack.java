package shop.qwy.com.myshop.http;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * created by qwyAndroid on 2016/9/21
 */
public abstract class BaseCallBack<T> {

    public Type mType;

    static Type getSuperclassTypeParameter(Class<?> subclass)
    {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class)
        {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }


    public BaseCallBack()
    {
        mType = getSuperclassTypeParameter(getClass());
    }
    public abstract void onBeforeRequest(Request request);
    public abstract void onFailure(Request request,IOException e);

    /**
     *请求成功时调用此方法
     * @param response
     */
    public abstract  void onResponse(Response response);

    public abstract void onSuccess(Response response,T t);
    public abstract void onError(Response response, int code,Exception e);

    public abstract void onTokenError(Response response, int code);
}
