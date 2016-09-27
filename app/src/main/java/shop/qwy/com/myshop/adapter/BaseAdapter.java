package shop.qwy.com.myshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


/**
 * created by qwyAndroid on 2016/9/24
 */
public abstract class BaseAdapter<T,H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>{

    protected List<T> mDatas;
    protected int mResourceId;
    protected Context context;
    protected OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;

    }
    public BaseAdapter(Context context,List<T> datas, int resourceId) {
        this.mDatas = datas;
        this.context = context;
        this.mResourceId = resourceId;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mResourceId, parent, false);
        return new BaseViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T item = getItem(position);

        bindData((H)holder,item);
    }

    protected abstract void bindData(H holder, T item);

    @Override
    public int getItemCount() {
        if(mDatas == null && mDatas.size()<=0){
            return 0;
        }
        return mDatas.size();
    }

    public T getItem(int position){
        return mDatas.get(position);
    }

    public T getData(int position){
        return mDatas.get(position);
    }

    public List<T> getDatas(){
        return mDatas;
    }

    public void addData(List<T> data){
        addData(0,data);
    }
    public void clearData(){

        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());
    }
    public void addData(int i, List<T> data) {
        if (data != null && data.size()>0){
            mDatas.addAll(data);

            notifyItemRangeChanged(i,mDatas.size());
        }
    }
}
