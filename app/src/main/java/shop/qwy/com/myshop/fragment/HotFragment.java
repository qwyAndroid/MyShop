package shop.qwy.com.myshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.util.List;

import okhttp3.Response;
import shop.qwy.com.myshop.Contants;
import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.adapter.BaseAdapter;
import shop.qwy.com.myshop.adapter.BaseViewHolder;
import shop.qwy.com.myshop.adapter.DividerItemDecoration;
import shop.qwy.com.myshop.adapter.HWAdapter;
import shop.qwy.com.myshop.adapter.HotWearsAdapter;
import shop.qwy.com.myshop.adapter.SimpleAdapter;
import shop.qwy.com.myshop.bean.Page;
import shop.qwy.com.myshop.bean.Wears;
import shop.qwy.com.myshop.http.OkHttpHelper;
import shop.qwy.com.myshop.http.SportsCallback;

/**
 * 作者：仇伟阳
 */
public class HotFragment extends Fragment{

    private int currPage = 1;
    private int pageSize = 10;
    private int totalPage=1;

    private OkHttpHelper mHelper = OkHttpHelper.getInstance();
    private MaterialRefreshLayout mRefreshView;
    private RecyclerView mRecycleView;

    private List<Wears> datas;

//    private HotWearsAdapter wearsAdapter;
    private HWAdapter mAdapter;

    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;

    private int state=STATE_NORMAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        mRefreshView = (MaterialRefreshLayout) view.findViewById(R.id.refresh_view);
        mRecycleView = (RecyclerView) view.findViewById(R.id.recyclerview);
        initRefreshLayout();
        getData();
        return view;
    }
    private  void initRefreshLayout(){

        mRefreshView.setLoadMore(true);
        mRefreshView.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

               refreshData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if(currPage <=totalPage)
                    loadMoreData();
                else{
                    Toast.makeText(getContext(),"沒有更多了，亲 —— _ ——",Toast.LENGTH_SHORT).show();
                    mRefreshView.finishRefreshLoadMore();
                }


            }
        });
    }

    private void loadMoreData() {
        currPage += 1;
        state = STATE_MORE;
        getData();
    }

    private void refreshData() {
        currPage = 1;
        state = STATE_REFREH;
        getData();
    }

    private void getData() {
        String url = Contants.API.WARES_HOT+"?curPage="+currPage+"&pageSize="+pageSize;
        mHelper.get(url, new SportsCallback<Page<Wears>>(getContext()) {


            @Override
            public void onSuccess(Response response, Page<Wears> wearsPage) {
                datas = wearsPage.getList();
                currPage = wearsPage.getCurrentPage();
                pageSize = wearsPage.getPageSize();
                totalPage = wearsPage.getTotalPage();

                showData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showData() {
        switch (state) {
            case STATE_NORMAL:
//                wearsAdapter = new HotWearsAdapter(datas);
                mAdapter = new HWAdapter(getContext(),datas,R.layout.template_hot_wears);
                mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Wears wears = datas.get(position);
                        Toast.makeText(getContext(),wears.getPrice()+"",Toast.LENGTH_SHORT).show();
                    }
                });
                mRecycleView.setAdapter(mAdapter);
                mRecycleView.setItemAnimator(new DefaultItemAnimator());
                mRecycleView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
                mRecycleView.setLayoutManager(new LinearLayoutManager(this.getContext()));


                break;
            case STATE_REFREH:
                mAdapter.clearData();
                mAdapter.addData(datas);

                mRecycleView.scrollToPosition(0);
                mRefreshView.finishRefresh();
                break;
            case STATE_MORE:
                mAdapter.addData(mAdapter.getDatas().size(),datas);

                mRecycleView.scrollToPosition(mAdapter.getDatas().size());
                mRefreshView.finishRefreshLoadMore();
                break;
        }
    }


}
