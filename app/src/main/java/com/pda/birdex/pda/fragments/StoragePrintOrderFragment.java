package com.pda.birdex.pda.fragments;

import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.PrintEntity;
import com.pda.birdex.pda.response.StockInContainerInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.T;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/8.
 * 打印容器单
 */
public class StoragePrintOrderFragment extends BaseFragment implements View.OnClickListener {
    String tag = "StoragePrintOrderFragment";
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;//容器号
    @Bind(R.id.tv_storage_order)
    TextView tv_storage_order;//入库单号
    @Bind(R.id.tv_business)
    TextView tv_business;//商家

    StockInContainerInfoEntity entity;
    String stockNum;//容器号

    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_storage_printorder_layout;
    }

    @Override
    public void initializeContentViews() {
        bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            entity = (StockInContainerInfoEntity) bundle.getSerializable("StockInContainerInfoEntity");
            stockNum = bundle.getString("stockNum");
            tv_vessel_num.setText(stockNum);
            tv_storage_order.setText(entity.getOrderNo());
            tv_business.setText(entity.getOwner());
        }
    }

    @Override
    protected void lazyLoad() {

    }

    private void print() {
        JSONObject object = new JSONObject();
        try {
            if (entity != null) {
                object.put("count", 1);
                object.put("owner", entity.getOwner());
                object.put("orderNo", entity.getTid());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BirdApi.postStockInCodePrint(getActivity(), object, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                PrintEntity pentity = GsonHelper.getPerson(object.toString(), PrintEntity.class);
                if (pentity != null) {
                    bus.post(pentity.getData());
                    //日志上报
                    String orderId = entity.getOrderNo();
                    String tid = entity.getTid();
                    MyApplication.loggingUpload.stockInPrint(getActivity(),tag,orderId,tid,pentity.getData());
                }
                else
                    T.showShort(getActivity(), getString(R.string.parse_fail));
            }

            @Override
            public void errorCallBack(JSONObject object) {

            }
        }, tag, true);
    }

    @OnClick(R.id.btn_commit)
    @Override
    public void onClick(View v) {
        print();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }
}
