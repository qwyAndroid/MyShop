package shop.qwy.com.myshop.fragment;

import android.content.Intent;
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
import shop.qwy.com.myshop.Contants;
import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.WearsListActivity;
import shop.qwy.com.myshop.adapter.DividerItemDecortion;
import shop.qwy.com.myshop.adapter.HomeCategoryAdapter;
import shop.qwy.com.myshop.bean.Banner;
import shop.qwy.com.myshop.bean.Campaign;
import shop.qwy.com.myshop.bean.HomeCampaign;
import shop.qwy.com.myshop.bean.HomeCategory;
import shop.qwy.com.myshop.http.BaseCallBack;
import shop.qwy.com.myshop.http.OkHttpHelper;
import shop.qwy.com.myshop.http.SportsCallback;

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
    private OkHttpHelper mHelper;
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
        String url = Contants.API.CAMPAIGN_HOME;

       mHelper.get(url, new BaseCallBack<List<HomeCampaign>>() {
           @Override
           public void onBeforeRequest(Request request) {

           }

           @Override
           public void onFailure(Request request, IOException e) {

           }

           @Override
           public void onResponse(Response response) {

           }

           @Override
           public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
               initData(homeCampaigns);
           }


           @Override
           public void onError(Response response, int code, Exception e) {

           }
       });




    }

    private void initData(List<HomeCampaign> homeCampaigns) {
        HomeCategoryAdapter adapter = new HomeCategoryAdapter(homeCampaigns,getContext());
        adapter.setOnItemClickListener(new HomeCategoryAdapter.onItemClickListener() {
            @Override
            public void onClick(View v, Campaign campaign) {
//                Toast.makeText(getContext(),"title="+campaign.getTitle(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), WearsListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID,campaign.getId());

                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecortion());//add decoration 分割线
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    private void requestImagers(){
        String url = Contants.API.BASE_URL+"banner/query?type=1";
//        OkHttpClient client = new OkHttpClient();
////        RequestBody body = new FormBody.Builder()
////                .add("type","1")
////
////                .build();
//        Request request = new Request.Builder()
//                .url(url)
////                .post(body)
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if(response.isSuccessful()){
//                    String json = response.body().string();
//                    Type type = new TypeToken<List<Banner>>(){}.getType();
//                    mBanner =mGson.fromJson(json,type);
//
//                    initSlider();
//                }
//            }
//        });
        mHelper = OkHttpHelper.getInstance();
        mHelper.get(url, new SportsCallback<List<Banner>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner = banners;
                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

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
        sliderShow.setDuration(3000);

        sliderShow.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d(TAG,"onPageScrolled");
            }

            @Override
            public void onPageSelected(int position) {
//                sliderShow.setCurrentPosition(position);
//                Log.d(TAG,"onPageSelected" +  position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.d(TAG,"onPageScrollStateChanged");
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        sliderShow.stopAutoCycle();
    }
}
