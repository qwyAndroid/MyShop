package shop.qwy.com.myshop;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import shop.qwy.com.myshop.adapter.DividerItemDecoration;
import shop.qwy.com.myshop.adapter.HWAdapter;
import shop.qwy.com.myshop.bean.Page;
import shop.qwy.com.myshop.bean.Wears;
import shop.qwy.com.myshop.utlis.Pager;
import shop.qwy.com.myshop.widget.MyToolbar;

/**
 * created by qwyAndroid on 2016/9/28
 */
public class WearsListActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, Pager.OnPageListener, View.OnClickListener {
    public static final int TAG_DEFAULT=0;
    public static final int TAG_SALE=1;
    public static final int TAG_PRICE=2;

    public static final int ACTION_LIST = 0;
    public static final int ACTION_GRID = 1;

    private TabLayout mTablayout;
    private RecyclerView mRecycleView;
    private MaterialRefreshLayout mRefreshView;
    private TextView txtSummary;
    private MyToolbar mToolbar;

    private int orderBy = 0;
    private long campaignId = 0;
    private HWAdapter hwAdapter;
    private Pager pager;
    private Button rightButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waers_list);
        mTablayout = (TabLayout) findViewById(R.id.tab_layout);
        mRefreshView = (MaterialRefreshLayout) findViewById(R.id.refresh_layout);
        mRecycleView = (RecyclerView) findViewById(R.id.recycler_view);
        txtSummary = (TextView) findViewById(R.id.txt_summary);
        mToolbar = (MyToolbar) findViewById(R.id.toolbar);

        campaignId=getIntent().getLongExtra(Contants.COMPAINGAIN_ID,0);

        initTabLayout();
        initToolBar();
        init();
    }

    private void initToolBar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WearsListActivity.this.finish();
            }
        });

        mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        rightButton = mToolbar.getRightButton();
        rightButton.setTag(ACTION_LIST);
        rightButton.setOnClickListener(this);
    }

    private void init() {
        pager = Pager.newBuilder()
                .setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .putParmers("campaignId",campaignId)
                .putParmers("orderBy",orderBy)
                .setmRefreshView(mRefreshView)
                .setCanLoadMore(true)
                .setOnPageListener(this)
                .build(this,new TypeToken<Page<Wears>>(){}.getType());
        pager.request();
    }

    private void initTabLayout() {
        TabLayout.Tab tab= mTablayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);

        mTablayout.addTab(tab);



        tab= mTablayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);

        mTablayout.addTab(tab);

        tab= mTablayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);

        mTablayout.addTab(tab);


        mTablayout.setOnTabSelectedListener(this);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pager.putParam("orderBy",orderBy);
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onLoad(List datas, int totalPage, int totalCount) {
        txtSummary.setText("共有"+totalCount+"件商品");
        if (hwAdapter == null) {
            hwAdapter = new HWAdapter(this, datas, R.layout.template_hot_wears);
            mRecycleView.setAdapter(hwAdapter);
            mRecycleView.setLayoutManager(new LinearLayoutManager(this));
            mRecycleView.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL_LIST));
        }else{

            hwAdapter.refreshData(datas);
        }
    }

    @Override
    public void onFresh(List datas, int totalPage, int totalCount) {
//        hwAdapter.clearData();
//        hwAdapter.addData(datas);
        hwAdapter.refreshData(datas);
        mRecycleView.scrollToPosition(0);
    }

    @Override
    public void onLoadmore(List datas, int totalPage, int totalCount) {
//        hwAdapter.addData(hwAdapter.getDatas().size(),datas);
        hwAdapter.loadMoreData(datas);
        mRecycleView.scrollToPosition(hwAdapter.getDatas().size());
    }

    @Override
    public void onClick(View v) {
        int action = (int) v.getTag();
        switch (action){
            case ACTION_GRID:
                mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
                rightButton.setTag(ACTION_LIST);
                hwAdapter.resetLayout(R.layout.template_hot_wears);
                mRecycleView.setLayoutManager(new LinearLayoutManager(this));
                mRecycleView.setAdapter(hwAdapter);
                break;
            case ACTION_LIST:
                mToolbar.setRightButtonIcon(R.drawable.icon_list_32);
                rightButton.setTag(ACTION_GRID);
                hwAdapter.resetLayout(R.layout.template_grid_wares);
                mRecycleView.setLayoutManager(new GridLayoutManager(this,2));
                mRecycleView.setAdapter(hwAdapter);
                break;
        }
    }
}
