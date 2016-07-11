package com.pda.birdex.pda.fragments;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pda.birdex.pda.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/8.
 * 解除绑定
 */
public class StorageUnBindFragment extends BaseFragment implements View.OnClickListener{
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;
    @Bind(R.id.tv_storage_order)
    TextView tv_storage_order;
    @Bind(R.id.tv_not_unbind)
    TextView tv_not_unbind;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_storage_unbind_layout;
    }

    @Override
    public void initializeContentViews() {

    }

    private void editMode(){
        tv_not_unbind.setVisibility(View.GONE);
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
    }

    private void disableEditMode(){
        tv_not_unbind.setVisibility(View.VISIBLE);
        btn_commit.setClickable(false);
        btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
        btn_commit.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void lazyLoad() {

    }

    @OnClick({R.id.btn_commit})
    @Override
    public void onClick(View v) {

    }
}
