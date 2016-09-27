package shop.qwy.com.myshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.bean.CategoryWaers;

/**
 * created by qwyAndroid on 2016/9/24
 */
public class CategoryWaersAdapter extends SimpleAdapter<CategoryWaers>{

    public CategoryWaersAdapter(Context context,List<CategoryWaers> datas, int resourceId) {
        super(context,datas, resourceId);
    }

    @Override
    protected void bindData(BaseViewHolder holder, CategoryWaers item) {
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);

        simpleDraweeView.setImageURI(Uri.parse(item.getImgUrl()));

        holder.getTextView(R.id.text_title).setText(item.getName());
        holder.getTextView(R.id.text_price).setText("￥"+item.getPrice());

    }
}
