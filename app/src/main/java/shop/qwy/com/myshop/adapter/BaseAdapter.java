package shop.qwy.com.myshop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Iterator;
import java.util.List;


/**
 * created by qwyAndroid on 2016/9/24
 */
public abstract class BaseAdapter<T,H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>{

    protected List<T> mDatas;
    protected int mResourceId;
    protected final Context context;
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

        for (Iterator it = mDatas.iterator(); it.hasNext();){

            T t = (T) it.next();
            int position = mDatas.indexOf(t);
            it.remove();
            notifyItemRemoved(position);
        }
    }
    public void addData(int position, List<T> list) {
        if(list !=null && list.size()>0) {

            for (T t:list) {
                mDatas.add(position, t);
                notifyItemInserted(position);
            }

        }
    }

    public void refreshData(List<T> list){

        if(list !=null && list.size()>0){

            clearData();
            int size = list.size();
            for (int i=0;i<size;i++){
                mDatas.add(i,list.get(i));
                notifyItemInserted(i);
            }

        }
    }

    public void loadMoreData(List<T> list){

        if(list !=null && list.size()>0){

            int size = list.size();
            int begin = mDatas.size();
            for (int i=0;i<size;i++){
                mDatas.add(list.get(i));
                notifyItemInserted(i+begin);
            }

        }

    }
}
