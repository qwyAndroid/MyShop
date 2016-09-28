package shop.qwy.com.myshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.List;

import okhttp3.Response;
import shop.qwy.com.myshop.Contants;
import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.adapter.BaseAdapter;
import shop.qwy.com.myshop.adapter.CategoryAdapter;
import shop.qwy.com.myshop.adapter.CategoryWaersAdapter;
import shop.qwy.com.myshop.adapter.DividerItemDecoration;
import shop.qwy.com.myshop.adapter.DividerItemDecortion;
import shop.qwy.com.myshop.bean.Banner;
import shop.qwy.com.myshop.bean.Category;
import shop.qwy.com.myshop.bean.CategoryPage;
import shop.qwy.com.myshop.bean.CategoryWaers;
import shop.qwy.com.myshop.http.OkHttpHelper;
import shop.qwy.com.myshop.http.SportsCallback;
import shop.qwy.com.myshop.utlis.ToastUtils;

/**
 * 作者：仇伟阳
 */
public class CategoryFragment extends Fragment{
    private RecyclerView mRecycleView;
    private SliderLayout mSliderLayout;
    private MaterialRefreshLayout mRefreshLayout;

    private  RecyclerView mWaresRecycleView;
    private OkHttpHelper mHelper = OkHttpHelper.getInstance();

    private List<Banner> mbanner;
    private List<Category> mCategory;

    private List<CategoryWaers> mWaers;
//    categoryId=11&curPage=1&pageSize=10
    private long categoryId = 1;
    private int curPage = 1;
    private int pageSize=10;
    private int totalPage = 1;

    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;
    private int state=STATE_NORMAL;
    private CategoryWaersAdapter categoryWaersAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        mRecycleView = (RecyclerView) view.findViewById(R.id.recyclerview_category);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);
        mRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh_layout);
        mWaresRecycleView = (RecyclerView) view.findViewById(R.id.recyclerview_wares);

        requestCategoryData();

        requestBannerData();

//        requestWaresData(categoryId);
        initFreshLayout();
        return view;
    }

    private void requestWaresData(long categoryId) {
        String url = Contants.API.WARES_LIST+"?categoryId="+categoryId+
                "&curPage="+curPage+"&pageSize="+pageSize;
        mHelper.get(url, new SportsCallback<CategoryPage<CategoryWaers>>(getContext()) {


            @Override
            public void onSuccess(Response response, CategoryPage<CategoryWaers> categoryPage) {
//                mWaers = categoryPage.getList();

                curPage = categoryPage.getCurrentPage();
//                pageSize = categoryPage.getPageSize();
                totalPage = categoryPage.getTotalPage();
                
                showWaresData(categoryPage.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.e("TAG",""+code);
            }
        });
    }

    private void showWaresData(final List<CategoryWaers> wares) {
        switch (state){
            case STATE_NORMAL:
                if(categoryWaersAdapter == null) {
                    categoryWaersAdapter = new
                            CategoryWaersAdapter(getContext(),wares, R.layout.template_grid_wares);

                    categoryWaersAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Toast.makeText(getContext(), "￥" + wares.get(position).getPrice(), Toast.LENGTH_SHORT).show();

                        }
                    });
                    mWaresRecycleView.setAdapter(categoryWaersAdapter);
                    mWaresRecycleView.setLayoutManager(new GridLayoutManager(getContext(),2));
                    mWaresRecycleView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
                    mWaresRecycleView.setItemAnimator(new DefaultItemAnimator());

                }else{
                    categoryWaersAdapter.clearData();
                    categoryWaersAdapter.addData(wares);
                }

                break;
            case STATE_REFREH:
                categoryWaersAdapter.clearData();
                categoryWaersAdapter.addData(wares);

                mWaresRecycleView.scrollToPosition(0);
                mRefreshLayout.finishRefresh();
            case STATE_MORE:
                categoryWaersAdapter.addData(categoryWaersAdapter.getDatas().size(),wares);

                mWaresRecycleView.scrollToPosition(categoryWaersAdapter.getDatas().size());
                mRefreshLayout.finishRefreshLoadMore();
        }

    }

    private void initFreshLayout() {
        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

                refreshWaersData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                if(curPage < totalPage){
                    refreshLoadMore();
                }else{


                    Toast.makeText(getContext(), "沒有更多了，亲 —— _ ——", Toast.LENGTH_SHORT).show();

                    mRefreshLayout.finishRefreshLoadMore();
                    mRefreshLayout.setLoadMore(false);
                }
            }
        });
    }

    private void refreshLoadMore() {
        curPage += 1;
        state = STATE_MORE;
        requestWaresData(categoryId);
    }

    private void refreshWaersData() {
//        categoryId = mCategory.get()
        curPage = 1 ;
        state = STATE_REFREH;
        requestWaresData(categoryId);
    }

    private void requestBannerData() {
        String url = Contants.API.BANNER+"?type=1";
        mHelper.get(url, new SportsCallback<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mbanner = banners;

                showBannerData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showBannerData() {
        DefaultSliderView sliderView;
        for (Banner banner : mbanner){
            sliderView = new DefaultSliderView(getContext());
            sliderView.image(banner.getImgUrl());
            sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
            mSliderLayout.addSlider(sliderView);

        }

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderLayout.setDuration(3000);
    }

    private void requestCategoryData() {
        mHelper.get(Contants.API.CATEGORY_LIST, new SportsCallback<List<Category>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Category> categories) {
//                mCategory = categories;

                showCategoryData(categories);
                if(categories !=null && categories.size()>0){
                    categoryId = categories.get(0).getId();
                    requestWaresData(categoryId);
            }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showCategoryData(final List<Category> categories) {
        final CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(),categories, R.layout.template_single_text);
        mRecycleView.setAdapter(categoryAdapter);
        mRecycleView.addItemDecoration(new DividerItemDecortion());
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        categoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position <= 9) {
//                Toast.makeText(getContext(),mCategory.get(position).getName(),Toast.LENGTH_SHORT).show();
//                categoryId = categoryAdapter.getItem(position).getId();
                    categoryId = position + 1;
                    curPage = 1;

                    state = STATE_NORMAL;
                    requestWaresData(categoryId);

                }else{
                    ToastUtils.show(getContext(),"暂时没有商品");
                }
            }
        });
    }


}
