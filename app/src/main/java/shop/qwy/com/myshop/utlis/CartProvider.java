package shop.qwy.com.myshop.utlis;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import shop.qwy.com.myshop.bean.ShoppingCart;
import shop.qwy.com.myshop.bean.Wears;

/**
 * created by qwyAndroid on 2016/9/25
 */
public class CartProvider {

    public static final String CART_JSON="cart_json";

    private SparseArray<ShoppingCart> datas = null;
    private Context mContext;
    public CartProvider(Context context) {
        mContext = context;

        datas = new SparseArray<>();
        listToSparce();
    }

    public void put(ShoppingCart cart){
        ShoppingCart temp = datas.get(cart.getId().intValue());
        if (temp != null){
            temp.setCount(temp.getCount()+1);
        }else{
            temp = cart;
            temp.setCount(1);
        }
        datas.put(cart.getId().intValue(),temp);
        commit();
    }
    public void put(Wears wear){
        put(convertData(wear));
    }
    public void update(ShoppingCart cart){
        datas.put(cart.getId().intValue(),cart);
        commit();
    }
    public void delete(ShoppingCart cart){
        datas.delete(cart.getId().intValue());
        commit();
    }

    public List<ShoppingCart> getAll(){
        return getDataFromLocal();
    }

    private void commit(){
        List<ShoppingCart> carts = sparceToList();
        PreferencesUtils.putString(mContext,CART_JSON,JSONUtil.toJSON(carts));

    }
    private void listToSparce(){
        List<ShoppingCart> carts = getDataFromLocal();
        if(carts!=null && carts.size()>0){

            for (ShoppingCart cart:
                    carts) {

                datas.put(cart.getId().intValue(),cart);
            }
        }

    }
    private List<ShoppingCart> sparceToList(){
            List<ShoppingCart> list = new ArrayList<>(datas.size());
            for (int i =0;i<datas.size();i++){
                list.add(datas.valueAt(i));
            }
        return list;
    }
    private List<ShoppingCart> getDataFromLocal(){
        String json = PreferencesUtils.getString(mContext, CART_JSON);
        List<ShoppingCart> carts = null;
        if(json != null) {
             carts = JSONUtil.fromJson(json, new TypeToken<List<ShoppingCart>>() {}.getType());
        }
        return  carts;
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
