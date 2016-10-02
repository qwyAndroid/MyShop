package shop.qwy.com.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import okhttp3.Response;
import shop.qwy.com.myshop.Contants;
import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.adapter.CartAdapter;
import shop.qwy.com.myshop.adapter.DividerItemDecoration;
import shop.qwy.com.myshop.bean.ShoppingCart;
import shop.qwy.com.myshop.bean.User;
import shop.qwy.com.myshop.http.OkHttpHelper;
import shop.qwy.com.myshop.http.SportsCallback;
import shop.qwy.com.myshop.utlis.CartProvider;
import shop.qwy.com.myshop.widget.MyToolbar;

/**
 * 作者：仇伟阳
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    public static final int ACTION_EDIT=1;
    public static final int ACTION_CAMPLATE=2;

    private CartProvider mCartHelper;

    private RecyclerView mRecycleView;
    private Button BtnOrder;
    private Button BtnDel;
    private CheckBox checkboxAll;
    private TextView txtTotal;
    private List<ShoppingCart> mCarts;
    private CartAdapter cartAdapter;
    private MyToolbar mToolbar;

    private OkHttpHelper mHelper = OkHttpHelper.getInstance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        mRecycleView = (RecyclerView) view.findViewById(R.id.recycler_view);
        txtTotal = (TextView) view.findViewById(R.id.txt_total);
        checkboxAll = (CheckBox) view.findViewById(R.id.checkbox_all);
        BtnOrder = (Button) view.findViewById(R.id.btn_order);
        BtnDel = (Button) view.findViewById(R.id.btn_del);
        mToolbar = (MyToolbar) view.findViewById(R.id.toolbar);
        mCartHelper = new CartProvider(getContext());

        setDelLintener();

        initToolbar();
        initCartData();
        return view;
    }

    private void setDelLintener() {
        BtnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cartAdapter.delItem();
            }
        });

        BtnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHelper.get(Contants.API.USER_DETAIL, new SportsCallback<User>(getActivity()) {
                    @Override
                    public void onSuccess(Response response, User o) {
                        Log.d("TAG","onsuccess"+response.code());
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {
                        Log.d("TAG","onError"+response.code());
                    }
                });
            }
        });
    }

    private void initToolbar() {
        mToolbar.setTitle("购物车");
        mToolbar.getRightButton().setVisibility(View.VISIBLE);
        mToolbar.setRightButtonIcon(R.drawable.icon_edit);

        mToolbar.getRightButton().setOnClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
//        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });
    }

    private void initCartData() {


        mCarts = mCartHelper.getAll();
//        Log.e("TAG",mCarts.+"++++++++++++++++++++++++++++++++++++++++");

        if (mCarts != null)//如果在这里判断
        cartAdapter = new CartAdapter(getContext(),
                mCarts, R.layout.template_carts,txtTotal,checkboxAll);

        mRecycleView.setAdapter(cartAdapter);
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycleView.addItemDecoration(new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL_LIST));

    }

    public void refData(){//到这就又空指针了
        initCartData();
        if (cartAdapter != null) {
            cartAdapter.clearData();
            List<ShoppingCart> carts = mCartHelper.getAll();
            cartAdapter.addData(carts);
        }
    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        switch (action){
            case ACTION_EDIT:
                showDelBtn();

                break;
            case ACTION_CAMPLATE:
                showOrderBtn();
                break;
        }
    }

    private void showDelBtn(){
        txtTotal.setVisibility(View.GONE);
        BtnOrder.setVisibility(View.GONE);
        BtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);
        mToolbar.setRightButtonIcon(R.drawable.icon_completed);

        cartAdapter.checkAll_None(false);
        checkboxAll.setChecked(false);
    }

    private void showOrderBtn(){
        txtTotal.setVisibility(View.VISIBLE);
        BtnOrder.setVisibility(View.VISIBLE);
        BtnDel.setVisibility(View.GONE);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
        mToolbar.setRightButtonIcon(R.drawable.icon_edit);

        cartAdapter.checkAll_None(true);
        checkboxAll.setChecked(true);

        cartAdapter.showTotalPrice();

    }
}
