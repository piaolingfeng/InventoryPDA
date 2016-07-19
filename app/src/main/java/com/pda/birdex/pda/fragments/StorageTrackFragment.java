package com.pda.birdex.pda.fragments;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.StockInContainerInfoEntity;
import com.pda.birdex.pda.utils.StringUtils;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/8.
 * 追踪箱号
 */
public class StorageTrackFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "StorageTrackFragment";
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;
    @Bind(R.id.edt_vessel_num)
    ClearEditText edt_vessel_num;
    @Bind(R.id.btn_edit)
    Button btn_edit;
    @Bind(R.id.edt_storage_old_no)
    ClearEditText edt_storage_old_no;
    @Bind(R.id.tv_storage_old_no)
    TextView tv_storage_old_no;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    String location_position;//判断入口
    StockInContainerInfoEntity entity;
    String stockNum;

    // 转出
    @Bind(R.id.tv_out)
    TextView tv_out;

    // 转自
    @Bind(R.id.tv_in)
    TextView tv_in;

    @Bind(R.id.edt_upc)
    ClearEditText edt_upc;
    @Bind(R.id.tv_upc)
    TextView tv_upc;
    @Bind(R.id.edt_amout)
    ClearEditText edt_amout;
    @Bind(R.id.tv_amout)
    TextView tv_amout;

    //标记位， true为转出  false为转自  默认 true
    private boolean flag = true;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_storage_track_layout;
    }

    @Override
    public void barInitializeContentViews() {
        if (bundle != null && bundle.getString("location_position") != null) {//从StorageFragmentActivity进来
            location_position = bundle.getString("location_position");
        } else {
            bundle = getActivity().getIntent().getExtras();
            entity = (StockInContainerInfoEntity) bundle.getSerializable("StockInContainerInfoEntity");
            stockNum = bundle.getString("stockNum");
        }


        if ("SecondIndex".equals(location_position)) {//从入库的首页进来
            edt_vessel_num.setVisibility(View.VISIBLE);
            tv_vessel_num.setVisibility(View.INVISIBLE);
        } else {//从查看容器进来
            tv_vessel_num.setText(stockNum);
            edt_vessel_num.setText(stockNum);
            if (entity != null && !StringUtils.isEmpty(entity.getLink()) && entity.getUpcData().size() > 0) {//未提交过
                disEnableEditMode();
                tv_storage_old_no.setText(entity.getLink());
                edt_storage_old_no.setText(entity.getLink());
                edt_upc.setText(entity.getUpcData().get(0).getUpc());
                tv_upc.setText(entity.getUpcData().get(0).getUpc());
                edt_amout.setText(entity.getUpcData().get(0).getCount());
                tv_amout.setText(entity.getUpcData().get(0).getCount());
            } else {
                editMode();
            }
        }
        edt_storage_old_no.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    String string = v.getText().toString();
                    ClearEditTextCallBack(string);
                }
                return false;
            }
        });

        edt_vessel_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_vessel_num.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_vessel_num);
                }
            }
        });
        edt_storage_old_no.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_storage_old_no.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_storage_old_no);
                }
            }
        });
        edt_upc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_upc.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_upc);
                }
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        if ("SecondIndex".equals(location_position)) {
            return edt_vessel_num;
        }
        return edt_storage_old_no;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (!this.isHidden()) {
//            if (getEdt_input() == edt_vessel_num && "SecondIndex".equals(location_position)) {//入库首页入口进入
//                edt_storage_old_no.requestFocus();
//                return;
//            }
//            if (edt_storage_old_no.getVisibility() == View.VISIBLE) {//工具包进入
//
//            }


            if (edt_vessel_num.hasFocus()) {
                edt_storage_old_no.requestFocus();//切换焦点
                return;
            }
            if (edt_storage_old_no.hasFocus()) {
                edt_upc.requestFocus();//切换焦点
                return;
            }
            if (edt_upc.hasFocus()) {
                edt_amout.requestFocus();//切换焦点
                setEdt_input(null);
                return;
            }
        }
    }

    private void editMode() {
        btn_edit.setVisibility(View.INVISIBLE);
        edt_storage_old_no.setVisibility(View.VISIBLE);
        tv_storage_old_no.setVisibility(View.INVISIBLE);
        edt_storage_old_no.setText(tv_storage_old_no.getText().toString());
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));

        edt_upc.setVisibility(View.VISIBLE);
        edt_upc.setText(tv_upc.getText());
        tv_upc.setVisibility(View.INVISIBLE);

        edt_amout.setVisibility(View.VISIBLE);
        edt_amout.setText(tv_amout.getText());
        tv_amout.setVisibility(View.INVISIBLE);
    }

    private void disEnableEditMode() {
        btn_edit.setVisibility(View.VISIBLE);
        edt_storage_old_no.setVisibility(View.INVISIBLE);
        tv_storage_old_no.setVisibility(View.VISIBLE);
        tv_storage_old_no.setText(edt_storage_old_no.getText().toString());
        btn_commit.setClickable(false);
        btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
        btn_commit.setTextColor(getResources().getColor(R.color.white));

        tv_upc.setVisibility(View.VISIBLE);
        tv_upc.setText(edt_upc.getText());
        edt_upc.setVisibility(View.INVISIBLE);

        tv_amout.setVisibility(View.VISIBLE);
        tv_amout.setText(edt_amout.getText());
        edt_amout.setVisibility(View.INVISIBLE);
    }

    private void commit() {
        JSONObject object = new JSONObject();
        try {
            object.put("oldContainerNo", edt_storage_old_no.getText().toString());
            if ("SecondIndex".equals(location_position)) {//从入库的首页进来
                object.put("newContainerNo", edt_vessel_num.getText().toString());
            } else {
                object.put("newContainerNo", tv_vessel_num.getText().toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BirdApi.postStockInTrack(getActivity(), object, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                T.showShort(getActivity(), getString(R.string.taking_submit_suc));
                if (!"SecondIndex".equals(location_position)) {
                    disEnableEditMode();
                }
                //日志上报
                String orderId = "";
                String tid = "";
                String upc = edt_upc.getText() + "";
                long count = Long.parseLong(edt_amout.getText() + "");
                String ctFrom = "";
                String ctTo = "";
                if (flag)//true为转出  false为转自  默认 true
                {
                    if ("SecondIndex".equals(location_position)) {//从入库的首页进来
                        ctFrom = edt_vessel_num.getText().toString();
                    } else {
                        ctFrom = tv_vessel_num.getText().toString();
                    }
                    ctTo = edt_storage_old_no.getText().toString();
                } else {
                    if ("SecondIndex".equals(location_position)) {//从入库的首页进来
                        ctTo = edt_vessel_num.getText().toString();
                    } else {
                        ctTo = tv_vessel_num.getText().toString();
                    }
                    ctFrom = edt_storage_old_no.getText().toString();
                }
                try {//不管是哪个入口都会返回上面两个参数
                    orderId = object.getString("orderNo");
                    tid = object.getString("tid");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                MyApplication.loggingUpload.stockInTrack(getActivity(), tag, orderId, tid,ctFrom,ctTo,upc,count);
            }

            @Override
            public void errorCallBack(JSONObject object) {
                T.showShort(getActivity(), getString(R.string.taking_submit_fal));

            }
        }

                , tag, true);
        }

                // 点击转出

    private void clickOut() {
        tv_out.setBackgroundResource(R.drawable.textview_border_y);
        tv_out.setTextColor(Color.parseColor("#FFFFFF"));
        tv_in.setBackgroundResource(R.drawable.textview_border_n);
        tv_in.setTextColor(getResources().getColor(R.color.context_2));
        flag = true;
    }

    // 点击转自
    private void clickIn() {
        tv_in.setBackgroundResource(R.drawable.textview_border_y);
        tv_in.setTextColor(Color.parseColor("#FFFFFF"));
        tv_out.setBackgroundResource(R.drawable.textview_border_n);
        tv_out.setTextColor(getResources().getColor(R.color.context_2));
        flag = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }

    @OnClick({R.id.btn_commit, R.id.btn_edit, R.id.tv_out, R.id.tv_in})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                if (TextUtils.isEmpty(edt_vessel_num.getText())) {
                    T.showShort(getContext(), getString(R.string.count_vessel_empty));
                    return;
                }
                if (TextUtils.isEmpty(edt_storage_old_no.getText())) {
                    T.showShort(getContext(), getString(R.string.count_relevance_empty));
                    return;
                }
                if (TextUtils.isEmpty(edt_upc.getText())) {
                    T.showShort(getContext(), getString(R.string.count_upc_empty));
                    return;
                }
                if (TextUtils.isEmpty(edt_amout.getText())) {
                    T.showShort(getContext(), getString(R.string.amount_empty));
                    return;
                }
                commit();
//                disEnableEditMode();
                break;
            case R.id.btn_edit:
                editMode();
                break;
            case R.id.tv_out:
                clickOut();
                break;

            case R.id.tv_in:
                clickIn();
                break;
        }
    }
}
