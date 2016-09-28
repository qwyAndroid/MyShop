package shop.qwy.com.myshop.utlis;

import android.content.Context;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;
import shop.qwy.com.myshop.bean.Page;
import shop.qwy.com.myshop.http.OkHttpHelper;
import shop.qwy.com.myshop.http.SportsCallback;

/**
 * created by qwyAndroid on 2016/9/27
 */
public class Pager {

    public static Builder builder;
    private OkHttpHelper mHelper;

    private  static final int STATE_NORMAL=0;
    private  static final int STATE_REFREH=1;
    private  static final int STATE_MORE=2;


    private int state=STATE_NORMAL;

    private Pager() {
        mHelper = OkHttpHelper.getInstance();
        initRefreshLayout();
    }

    private  void initRefreshLayout(){

        builder.mRefreshView.setLoadMore(builder.canLoadMore);
        builder.mRefreshView.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                builder.mRefreshView.setLoadMore(builder.canLoadMore);
                refreshData();

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                builder.mRefreshView.setLoadMore(builder.canLoadMore);
                if(builder.currPage <=builder.totalPage)
                    loadMoreData();
                else{
                    Toast.makeText(builder.mContext,"沒有更多了，亲 —— _ ——",Toast.LENGTH_SHORT).show();
                    builder.mRefreshView.finishRefreshLoadMore();
                    builder.mRefreshView.setLoadMore(false);
                }


            }
        });
    }

    public void request(){

        requestData();
    }
    private void loadMoreData() {
        builder.currPage += 1;
        state = STATE_MORE;
        requestData();
    }

    private void refreshData() {
        builder.currPage = 1;
        state = STATE_REFREH;
        requestData();
    }

    private String buildUrl(){

        return builder.url +"?"+buildUrlParams();
    }


    private   String buildUrlParams() {


        HashMap<String, Object> map = (HashMap<String, Object>) builder.params;

        map.put("curPage",builder.currPage);
        map.put("pageSize",builder.pageSize);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0,s.length()-1);
        }
        return s;
    }
    private void requestData() {
//        String url = Contants.API.WARES_HOT+"?curPage="+currPage+"&pageSize="+pageSize;

        mHelper.get(buildUrl(), new RequestCallBack(builder.mContext));
    }

    private <T> void showData(List<T> datas,int totalPage,int totalCount) {
        switch (state) {
            case STATE_NORMAL:
                    if (builder.mListener != null){
                        builder.mListener.onLoad(datas,totalPage,totalCount);
                    }

                break;
            case STATE_REFREH:
                builder.mRefreshView.finishRefresh();
                if (builder.mListener != null){
                    builder.mListener.onFresh(datas,totalPage,totalCount);
                }
                break;
            case STATE_MORE:
                builder.mRefreshView.finishRefreshLoadMore();
                if (builder.mListener != null){
                    builder.mListener.onLoadmore(datas,totalPage,totalCount);
                }
                break;
        }
    }
    public static Builder newBuilder(){
        builder = new Builder();
        return builder;
    }

    public void putParam(String key, Object value) {
        builder.params.put(key,value);
    }

    public static class Builder{
        private Context mContext;
        private Type mType;
        private String url;

        private boolean canLoadMore;

        private MaterialRefreshLayout mRefreshView;

        private OnPageListener mListener;

        private int currPage = 1;
        private int pageSize = 10;
        private int totalPage=1;
        private int totalCount=1;

        private Map<String,Object> params = new HashMap<>(5);

        public Builder setUrl(String url) {
            this.url = url;
            return builder;
        }
        public Builder setPageSize(int pageSize){
            this.pageSize = pageSize;
            return builder;
        }
        public Builder putParmers(String key,Object value){
            params.put(key,value);
            return builder;
        }

        public Builder setmRefreshView(MaterialRefreshLayout mRefreshView) {
            this.mRefreshView = mRefreshView;
            return builder;
        }

        public Builder setCanLoadMore(boolean canLoadMore) {
            this.canLoadMore = canLoadMore;
            return builder;
        }
        public Pager build(Context context, Type type){
            this.mContext = context;
            this.mType = type;

            valid();
            return new Pager();
        }

        public Builder setOnPageListener(OnPageListener listener){
            this.mListener = listener;
            return builder;
        }
        private void valid(){


            if(this.mContext==null)
                throw  new RuntimeException("content can't be null");

            if(this.url==null || "".equals(this.url))
                throw  new RuntimeException("url can't be  null");

            if(this.mRefreshView==null)
                throw  new RuntimeException("MaterialRefreshLayout can't be  null");
        }


    }
    public interface  OnPageListener<T>{
        void onLoad(List<T> datas,int totalPage,int totalCount);
        void onFresh(List<T> datas,int totalPage,int totalCount);
        void onLoadmore(List<T> datas,int totalPage,int totalCount);

    }
    class RequestCallBack<T> extends SportsCallback<Page<T>>{

        public RequestCallBack(Context context) {
            super(context);

            super.mType = builder.mType;
        }

        @Override
        public void onSuccess(Response response, Page<T> page) {
            builder.currPage = page.getCurrentPage();
            builder.pageSize = page.getPageSize();
            builder.totalPage = page.getTotalPage();
            builder.totalCount = page.getTotalCount();
            showData(page.getList(),page.getTotalPage(),page.getTotalCount());
        }

        @Override
        public void onError(Response response, int code, Exception e) {

        }
    }
}
