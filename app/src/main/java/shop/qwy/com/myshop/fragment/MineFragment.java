package shop.qwy.com.myshop.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import shop.qwy.com.myshop.Contants;
import shop.qwy.com.myshop.LoginActivity;
import shop.qwy.com.myshop.MyApplication;
import shop.qwy.com.myshop.R;
import shop.qwy.com.myshop.bean.User;

/**
 * 作者：仇伟阳
 */
public class MineFragment extends Fragment {

    @ViewInject(R.id.img_head)
    private CircleImageView mImageHead;

    @ViewInject(R.id.txt_username)
    private TextView mTxtUserName;

    @ViewInject(R.id.btn_logout)
    private Button mbtnLogout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);

        ViewUtils.inject(this,view);

//        initUser();
        return view;
    }

    private void initUser() {
        User user =  MyApplication.getInstance().getUser();
        showUser(user);
    }

    @OnClick(value = {R.id.img_head,R.id.txt_username})
    public void toLogin(View view){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivityForResult(intent, Contants.REQUEST_CODE);

    }
    @OnClick(R.id.btn_logout)
    public void logout(View view){

       MyApplication.getInstance().clearUser();
        showUser(null);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        User user =  MyApplication.getInstance().getUser();
        showUser(user);

    }

    private void showUser(User user) {
        if(user!=null){

            if(!TextUtils.isEmpty(user.getLogo_url()))
                showHeadImage(user.getLogo_url());

            mTxtUserName.setText(user.getUsername());

            mbtnLogout.setVisibility(View.VISIBLE);
        }
        else {
            mTxtUserName.setText(R.string.to_login);
            mbtnLogout.setVisibility(View.GONE);
        }
    }
    private void showHeadImage(String url){

        Picasso.with(getActivity()).load(url).into(mImageHead);
    }

}
