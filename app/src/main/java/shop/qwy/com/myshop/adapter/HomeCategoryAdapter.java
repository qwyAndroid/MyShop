package shop.qwy.com.myshop.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.bean.Campaign;
import shop.qwy.com.myshop.bean.HomeCampaign;
import shop.qwy.com.myshop.bean.HomeCategory;

/**
 * created by qwyAndroid on 2016/9/21
 */
public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.ViewHolder>{

    private  static int VIEW_TYPE_L=0;
    private  static int VIEW_TYPE_R=1;

    private LayoutInflater mInflate;
    private List<HomeCampaign> mDatas;
    private onItemClickListener mListener;
    private  Context mContext;
    public HomeCategoryAdapter(List<HomeCampaign> datas, Context context){
        this.mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        mInflate = LayoutInflater.from(parent.getContext());
        View view;
        if(type == VIEW_TYPE_R){
            view = mInflate.inflate(R.layout.template_home_cardview2,parent,false);
        }else {
            view = mInflate.inflate(R.layout.template_home_cardview, parent, false);
        }
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HomeCampaign campaign = mDatas.get(position);
        holder.textTitle.setText(campaign.getTitle());
        Picasso.with(mContext).load(campaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
        Picasso.with(mContext).load(campaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
        Picasso.with(mContext).load(campaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position % 2 ==0){
            return VIEW_TYPE_R;
        }
        return VIEW_TYPE_L;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;
        public ViewHolder(View itemView) {
            super(itemView);

            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            HomeCampaign homeCampaign = mDatas.get(getLayoutPosition());
            switch (v.getId()){
                case R.id.imgview_big:
                    mListener.onClick(v,homeCampaign.getCpOne());
                    break;
                case R.id.imgview_small_top:
                    mListener.onClick(v,homeCampaign.getCpTwo());
                    break;
                case R.id.imgview_small_bottom:
                    mListener.onClick(v,homeCampaign.getCpThree());
                    break;
            }
        }
    }
    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;

    }
    public interface onItemClickListener{
        void onClick(View v,Campaign campaign);
    }
}
