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
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.adapter.DividerItemDecortion;
import shop.qwy.com.myshop.adapter.HomeCategoryAdapter;
import shop.qwy.com.myshop.bean.Banner;
import shop.qwy.com.myshop.bean.HomeCategory;

/**
 * 作者：仇伟阳
 */
public class HomeFragment extends Fragment{

    private static  final  String TAG="HomeFragment";
    private SliderLayout sliderShow;

    private PagerIndicator mIndicator;
    private RecyclerView mRecyclerView;
    private List<Banner> mBanner;
    private Gson mGson = new Gson();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        sliderShow = (SliderLayout) view.findViewById(R.id.slider);

        mIndicator= (PagerIndicator) view.findViewById(R.id.custom_indicator);
//        initSlider();
        requestImagers();
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        List<HomeCategory> datas = new ArrayList<>(15);

        HomeCategory category = new HomeCategory("热门活动",R.drawable.img_big_1,R.drawable.img_1_small1,R.drawable.img_1_small2);
        datas.add(category);

        category = new HomeCategory("有利可图",R.drawable.img_big_4,R.drawable.img_4_small1,R.drawable.img_4_small2);
        datas.add(category);
        category = new HomeCategory("品牌街",R.drawable.img_big_2,R.drawable.img_2_small1,R.drawable.img_2_small2);
        datas.add(category);

        category = new HomeCategory("金融街 包赚翻",R.drawable.img_big_1,R.drawable.img_3_small1,R.drawable.imag_3_small2);
        datas.add(category);

        category = new HomeCategory("超值购",R.drawable.img_big_0,R.drawable.img_0_small1,R.drawable.img_0_small2);
        datas.add(category);
        HomeCategoryAdapter adapter = new HomeCategoryAdapter(datas);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecortion());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

    }
    private void requestImagers(){
        String url ="http://112.124.22.238:8081/course_api/banner/query?type=1";
        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("type","1")
//
//                .build();
        Request request = new Request.Builder()
                .url(url)
//                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String json = response.body().string();
                    Type type = new TypeToken<List<Banner>>(){}.getType();
                    mBanner =mGson.fromJson(json,type);

                    initSlider();
                }
            }
        });
    }
    private void initSlider() {//loading tab
        TextSliderView textSliderView;
        for (final Banner banner : mBanner){
            textSliderView = new TextSliderView(this.getActivity());
            textSliderView.image(banner.getImgUrl());
            textSliderView.description(banner.getName());
            sliderShow.addSlider(textSliderView);
            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Toast.makeText(HomeFragment.this.getActivity(),banner.getName(),Toast.LENGTH_LONG).show();
                }
            });

        }

        sliderShow.setCustomIndicator(mIndicator);
        sliderShow.setCustomAnimation(new DescriptionAnimation());
            //default indicator
//        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderShow.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        sliderShow.setDuration(300);

        sliderShow.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG,"onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
                sliderShow.setCurrentPosition(position);
                Log.d(TAG,"onPageSelected" +  position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG,"onPageScrollStateChanged");
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        sliderShow.stopAutoCycle();
    }
}
