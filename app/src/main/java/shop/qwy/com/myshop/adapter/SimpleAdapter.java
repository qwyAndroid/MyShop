package shop.qwy.com.myshop.adapter;

import android.content.Context;

import java.util.List;

/**
 * created by qwyAndroid on 2016/9/24
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder>{

    public SimpleAdapter(Context context,List<T> datas, int resourceId) {
        super(context,datas, resourceId);
    }


}
