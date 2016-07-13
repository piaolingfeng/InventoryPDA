package com.pda.birdex.pda.fragments;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.StockInContainerInfoEntity;
import com.pda.birdex.pda.utils.StringUtils;
import com.pda.birdex.pda.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/8.
 * 解除绑定
 */
public class StorageUnBindFragment extends BaseFragment implements View.OnClickListener {
    String tag= "StorageUnBindFragment";
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;
    @Bind(R.id.tv_storage_order)
    TextView tv_storage_order;
    @Bind(R.id.tv_not_unbind)
    TextView tv_not_unbind;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    StockInContainerInfoEntity entity;
    String stockNum;//容器号

    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_storage_unbind_layout;
    }

    @Override
    public void initializeContentViews() {
        if (bundle != null) {
            entity = (StockInContainerInfoEntity) bundle.getSerializable("StockInContainerInfoEntity");
            stockNum = bundle.getString("stockNum");
        }
        tv_vessel_num.setText(stockNum + "");
        if(entity!=null && StringUtils.isEmpty(entity.getOrderNo())){//异常，不能解绑
            disableEditMode();
        }else{
            if(entity!=null) {
                tv_storage_order.setText(entity.getOrderNo());
            }
            editMode();
        }
    }

    //解绑操作
    private void unBind(){
        JSONObject object = new JSONObject();
        try {
            object.put("containerNo",tv_vessel_num.getText());
            object.put("orderNo",tv_storage_order.getText());
            object.put("tid",entity.getTid());
            object.put("owner",entity.getOwner());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BirdApi.deleteRequest(getActivity(), object, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                T.showShort(getActivity(),getString(R.string.unbind_success));
            }

            @Override
            public void errorCallBack(JSONObject object) {
                T.showShort(getActivity(),getString(R.string.unbind_fail));
            }
        },"counting/code/unbind/area",tag,true);
    }

    private void editMode() {
        tv_not_unbind.setVisibility(View.GONE);
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
    }

    private void disableEditMode() {
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
        unBind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
