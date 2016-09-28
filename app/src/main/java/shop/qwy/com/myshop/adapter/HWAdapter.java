package shop.qwy.com.myshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.bean.ShoppingCart;
import shop.qwy.com.myshop.bean.Wears;
import shop.qwy.com.myshop.utlis.CartProvider;
import shop.qwy.com.myshop.utlis.ToastUtils;

/**
 * created by qwyAndroid on 2016/9/24
 */
public class HWAdapter extends SimpleAdapter<Wears>{

    CartProvider mCartProvider;
    private Context mContext;
    public HWAdapter(Context context, List<Wears> datas, int resourceId) {
        super(context,datas, resourceId);
        mContext = context;
        mCartProvider = new CartProvider(mContext);
    }

    @Override
    protected void bindData(BaseViewHolder holder, final Wears wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        holder.getTextView(R.id.text_title).setText(wares.getName());
        holder.getTextView(R.id.text_price).setText("￥"+wares.getPrice());

        Button button = holder.getButton(R.id.buynow);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCartProvider.put(convertData(wares));
                    ToastUtils.show(mContext, "已添加到购物车");
                }
            });
        }
    }
    public void  resetLayout(int layoutId){


        this.mResourceId  = layoutId;

        notifyItemRangeChanged(0,getDatas().size());


    }
    public ShoppingCart convertData(Wears item){

        ShoppingCart cart = new ShoppingCart();

        cart.setId(item.getId());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());

        return cart;
    }
}
