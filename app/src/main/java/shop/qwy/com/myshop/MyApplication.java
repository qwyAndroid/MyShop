package shop.qwy.com.myshop;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * created by qwyAndroid on 2016/9/23
 */
public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
