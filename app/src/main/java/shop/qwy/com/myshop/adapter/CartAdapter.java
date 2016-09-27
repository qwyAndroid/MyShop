package shop.qwy.com.myshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Iterator;
import java.util.List;

import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.bean.ShoppingCart;
import shop.qwy.com.myshop.utlis.CartProvider;
import shop.qwy.com.myshop.widget.AddAndSubView;

/**
 * created by qwyAndroid on 2016/9/25
 */
public class CartAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener, View.OnClickListener {

    private AddAndSubView mNunCtrl;
    private CartProvider cartProvider;

    private TextView textTotal;
    private CheckBox checkAll;
    private CheckBox checkBox;

    public CartAdapter(Context context, List<ShoppingCart> datas, int resourceId) {
        super(context,datas, resourceId);
//        cartProvider = new CartProvider(context);
    }

    public CartAdapter(Context context, List<ShoppingCart> mCarts,
                       int resourceId, TextView txtTotal, CheckBox checkboxAll) {
        super(context,mCarts,resourceId);
        textTotal = txtTotal;
        checkAll = checkboxAll;
        cartProvider = new CartProvider(context);

        checkAll.setOnClickListener(this);

        setOnItemClickListener(this);
        showTotalPrice();

    }


    public void showTotalPrice() {
        Float totalPrice = getTotalPrice();
        textTotal.setText("合计:"+totalPrice);
    }

    private Float getTotalPrice() {
        float sum = 0;
        if (isNull()){
            return sum;
        }

        for (ShoppingCart cart : mDatas){
            if (cart.isChecked()){
                sum += cart.getPrice() * cart.getCount();
            }
        }

        return sum;
    }

    private boolean isNull(){
        return (mDatas == null && mDatas.size()<=0);
    }

    @Override
    protected void bindData(BaseViewHolder holder, final ShoppingCart item) {
        holder.getTextView(R.id.text_title).setText(item.getName());
        holder.getTextView(R.id.text_price).setText("￥"+item.getPrice());

        mNunCtrl = (AddAndSubView) holder.getView(R.id.num_control);
        mNunCtrl.setmTextNum(item.getCount());

        mNunCtrl.setOnButtonClickListener(new AddAndSubView.OnButtonClickListener() {
            @Override
            public void onClickAddNum(View v, int value) {
                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }

            @Override
            public void onClickSubNum(View v, int value) {
                item.setCount(value);
                cartProvider.update(item);
                showTotalPrice();
            }
        });
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        checkBox = (CheckBox) holder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());
    }

    @Override
    public void onItemClick(View view, int position) {
        ShoppingCart cart =  getItem(position);
        cart.setChecked(!cart.isChecked());
        notifyItemChanged(position);
        checkListener();
        showTotalPrice();
    }

    public void checkListener(){
        int count = mDatas.size();
        int checkNum = 0;
        for (ShoppingCart cart : mDatas){
            if (cart.isChecked()){
                checkNum += 1;
            }else{
                checkAll.setChecked(false);
                break;
            }
        }
        if(count == checkNum){
            checkAll.setChecked(true);
        }
    }

    public void checkAll_None(boolean isCkecked){

        if (isNull()) return;
        int i = 0;
        for (ShoppingCart cart : mDatas) {
                cart.setChecked(isCkecked);
                notifyItemChanged(i);
                i++;
        }
    }

    public void delItem(){
        if (isNull()) return;

        for (Iterator<ShoppingCart> it = mDatas.iterator();it.hasNext();){
            ShoppingCart cart = it.next();
            if (cart.isChecked()){
                int position = mDatas.indexOf(cart);
                cartProvider.delete(cart);
                it.remove();
                notifyItemRemoved(position);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.checkbox_all){
            checkAll_None(checkAll.isChecked());
            showTotalPrice();
        }
    }
}
