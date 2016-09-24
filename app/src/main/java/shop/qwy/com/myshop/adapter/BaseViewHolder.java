package shop.qwy.com.myshop.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * created by qwyAndroid on 2016/9/24
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private SparseArray<View> views;
    protected BaseAdapter.OnItemClickListener mListener;

    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener listener) {
        super(itemView);
        itemView.setOnClickListener(this);
        mListener = listener;
        this.views = new SparseArray<View>();
    }
    public View getView(int viewId){
        return findView(viewId);
    }

    public TextView getTextView(int viewId){
        return findView(viewId);
    }

    public Button getButton(int viewId) {
        return findView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return findView(viewId);
    }
    private <T extends View> T findView(int id){
        View view = views.get(id);
        if(view == null){
            view = itemView.findViewById(id);
            views.put(id,view);
        }
        return (T)view;
    }

    @Override
    public void onClick(View v) {
        if(mListener != null){
            mListener.onItemClick(v,getLayoutPosition());
        }
    }
}
