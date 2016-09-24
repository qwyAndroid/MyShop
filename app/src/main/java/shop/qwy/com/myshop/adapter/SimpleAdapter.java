package shop.qwy.com.myshop.adapter;

import java.util.List;

/**
 * created by qwyAndroid on 2016/9/24
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder>{

    public SimpleAdapter(List<T> datas, int resourceId) {
        super(datas, resourceId);
    }


}
