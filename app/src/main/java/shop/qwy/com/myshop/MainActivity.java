package shop.qwy.com.myshop;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import shop.qwy.com.myshop.bean.Tab;
import shop.qwy.com.myshop.fragment.CartFragment;
import shop.qwy.com.myshop.fragment.CategoryFragment;
import shop.qwy.com.myshop.fragment.HomeFragment;
import shop.qwy.com.myshop.fragment.HotFragment;
import shop.qwy.com.myshop.fragment.MineFragment;
import shop.qwy.com.myshop.widget.FragmentTabHost;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private LayoutInflater mInflate;

    private CartFragment cartFragment;

    private List<Tab> mTabs = new ArrayList<>(5);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTab();
    }

    private void initTab() {
        Tab home = new Tab(R.drawable.selector_icon_home,R.string.home,HomeFragment.class);
        Tab cart = new Tab(R.drawable.selector_icon_cart,R.string.cart,CartFragment.class);
        Tab category = new Tab(R.drawable.selector_icon_category,R.string.catagory, CategoryFragment.class);
        Tab hot = new Tab(R.drawable.selector_icon_hot,R.string.hot,HotFragment.class);
        Tab mine = new Tab(R.drawable.selector_icon_mine,R.string.mine,MineFragment.class);

        mTabs.add(home);
        mTabs.add(hot);
        mTabs.add(category);
        mTabs.add(cart);
        mTabs.add(mine);

        mInflate = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        mTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);

        for (Tab tab : mTabs){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTittle()));

            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }



        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId == getString(R.string.cart)){
                    refData();
                }
            }
        });
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);
    }

    private void refData() {

        if(cartFragment ==null) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));

            if (fragment != null) {

                cartFragment = (CartFragment) fragment;

                cartFragment.refData();
//                cartFragment.changeToolbar();
            }
        }
        else
        {
            cartFragment.refData();
//            cartFragment.changeToolbar();
        }

    }

    private View buildIndicator(Tab tab) {
        View view = mInflate.inflate(R.layout.tab_indicator,null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTittle());

        return  view;
    }
}
