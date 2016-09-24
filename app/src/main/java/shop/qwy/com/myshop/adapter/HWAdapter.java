package shop.qwy.com.myshop.adapter;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.bean.Wears;

/**
 * created by qwyAndroid on 2016/9/24
 */
public class HWAdapter extends SimpleAdapter<Wears>{


    public HWAdapter(List<Wears> datas, int resourceId) {
        super(datas, resourceId);
    }

    @Override
    protected void bindData(BaseViewHolder holder, Wears wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        holder.getTextView(R.id.text_title).setText(wares.getName());
        holder.getTextView(R.id.text_price).setText("￥"+wares.getPrice());
    }
}
