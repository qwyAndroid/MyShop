package shop.qwy.com.myshop.bean;

import java.io.Serializable;

/**
 * created by qwyAndroid on
 */
public class BaseBean implements Serializable {
    protected  long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
