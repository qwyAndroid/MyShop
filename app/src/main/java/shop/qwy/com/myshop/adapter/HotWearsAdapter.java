package shop.qwy.com.myshop.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.bean.Wears;

/**
 * created by qwyAndroid on 2016/9/23
 */
public class HotWearsAdapter extends RecyclerView.Adapter<HotWearsAdapter.ViewHolder>{
    private LayoutInflater mInflate;

    private List<Wears> mDatas;

    public HotWearsAdapter(List<Wears> mDatas) {
        this.mDatas = mDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflate = LayoutInflater.from(parent.getContext());
        View view = mInflate.inflate(R.layout.template_hot_wears,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Wears wears = getData(position);
        holder.image.setImageURI(Uri.parse(wears.getImgUrl()));
        holder.textTitle.setText(wears.getName());
        holder.textPrice.setText("￥"+wears.getPrice());
    }
    public Wears getData(int position){
        return mDatas.get(position);
    }

    public List<Wears> getDatas(){
        return mDatas;
    }

    public void addData(List<Wears> data){
        addData(0,data);
    }
    public void clearData(){

        mDatas.clear();
        notifyItemRangeRemoved(0,mDatas.size());
    }
    public void addData(int i, List<Wears> data) {
        if (data != null && data.size()>0){
            mDatas.addAll(data);

            notifyItemChanged(i,mDatas.size());
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView image;
        private TextView textTitle;
        private TextView textPrice;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (SimpleDraweeView) itemView.findViewById(R.id.drawee_view);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            textPrice = (TextView) itemView.findViewById(R.id.text_price);

        }
    }
}
