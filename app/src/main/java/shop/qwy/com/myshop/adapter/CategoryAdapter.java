package shop.qwy.com.myshop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.bean.Category;

/**
 * created by qwyAndroid on 2016/9/24
 */
public class CategoryAdapter extends SimpleAdapter<Category>{

    public CategoryAdapter(Context context, List<Category> datas, int resourceId) {
        super(context,datas, resourceId);
    }

    @Override
    protected void bindData(BaseViewHolder holder, Category item) {
        TextView textView = holder.getTextView(R.id.textView);
        textView.setText(item.getName());


    }
}
