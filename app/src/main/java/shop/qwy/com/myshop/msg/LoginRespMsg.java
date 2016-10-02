package shop.qwy.com.myshop.msg;

/**
 * created by qwyAndroid on 2016/10/1
 */
public class LoginRespMsg<T> extends BaseRespMsg{
    private String token;

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
