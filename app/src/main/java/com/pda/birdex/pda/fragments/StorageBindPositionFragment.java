package com.pda.birdex.pda.fragments;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.StockInContainerInfoEntity;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/8.
 * 绑定库位
 */
public class StorageBindPositionFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "StorageBindPositionFragment";
    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;//容器号
    @Bind(R.id.tv_upc)
    TextView tv_upc;//upc
    @Bind(R.id.tv_amount)
    TextView tv_amount;
    @Bind(R.id.btn_edit)
    Button btn_edit;//查看参考库位
    @Bind(R.id.edt_storage_position)
    ClearEditText edt_storage_position;
    @Bind(R.id.tv_storage_position)
    TextView tv_storage_position;
    @Bind(R.id.btn_commit)
    Button btn_commit;
//    @Bind(R.id.tablayout)
//    TabLayout tabLayout;

    @Bind(R.id.capacityLevel_1)
    TextView capacityLevel_1;
    @Bind(R.id.capacityLevel_2)
    TextView capacityLevel_2;
    @Bind(R.id.capacityLevel_3)
    TextView capacityLevel_3;

    StockInContainerInfoEntity entity;
    String stockNum;//容器号

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_storage_bindposition_layout;
    }

    @Override
    public void barInitializeContentViews() {
        bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            entity = (StockInContainerInfoEntity) bundle.getSerializable("StockInContainerInfoEntity");
            stockNum = bundle.getString("stockNum");
//            String []tabList = getResources().getStringArray(R.array.capacityLevel);
            //添加3种分类
//            for (int i = 0; i < tabList.length; i++) {
//                View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_tab_layout, null);
//                TextView textView = (TextView) view.findViewById(R.id.tv_tab_title);
//                textView.setText(tabList[i]);
//                tabLayout.addTab(tabLayout.newTab().setCustomView(view));
//            }
            if (entity != null) {
                tv_vessel_num.setText(stockNum);
                if (entity.getUpcData().size() > 0) {
                    tv_upc.setText(entity.getUpcData().get(0).getUpc());
                    tv_amount.setText(entity.getUpcData().get(0).getCount());
                }
                String level = entity.getCapacityLevel();
                switch (level) {
                    case "1":
                        setSelect(capacityLevel_1);
                        break;
                    case "2":
                        setSelect(capacityLevel_2);
                        break;
                    case "3":
                        setSelect(capacityLevel_3);
                        break;
                }
                if(TextUtils.isEmpty(entity.getAreaNo()))
                    editMode();
                else
                    disableEditMode();
            }
        }
    }

    private void editMode() {
        edt_storage_position.setVisibility(View.VISIBLE);
        tv_storage_position.setVisibility(View.INVISIBLE);
        edt_storage_position.setText(tv_storage_position.getText());
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
    }

    private void disableEditMode() {
        edt_storage_position.setVisibility(View.INVISIBLE);
        tv_storage_position.setVisibility(View.VISIBLE);
        tv_storage_position.setText(edt_storage_position.getText());
        btn_commit.setClickable(false);
        btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
        btn_commit.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_storage_position;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (!this.isHidden() && edt_storage_position.getVisibility() == View.VISIBLE) {

        }
    }

    @OnClick({R.id.btn_commit, R.id.btn_edit, R.id.capacityLevel_1, R.id.capacityLevel_2, R.id.capacityLevel_3})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                commit();
                break;
            case R.id.btn_edit:
                editMode();
                break;
            case R.id.capacityLevel_1:
            case R.id.capacityLevel_2:
            case R.id.capacityLevel_3:
                setSelect(v);
                break;
        }
    }

    private void commit() {
        if (TextUtils.isEmpty(tv_amount.getText()) || TextUtils.isEmpty(tv_upc.getText())) {
            T.showShort(getActivity(), getString(R.string.storage_before_clear));//清点后再提交绑库位
            return;
        }
        if (entity == null)
            return;
        JSONObject object = new JSONObject();
        String owner = entity.getOwner();
        String storage_position = edt_storage_position.getText().toString();
        try {
            object.put("containerNo", tv_vessel_num.getText());
            object.put("owner", owner);
            object.put("areaCode", storage_position);
            object.put("level", level);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BirdApi.postStockBndArea(getActivity(), object, new RequestCallBackInterface() {
            @Override
            public void successCallBack(JSONObject object) {
                disableEditMode();
            }

            @Override
            public void errorCallBack(JSONObject object) {

            }
        }, tag, true);
    }

    @Override
    public void onDestroy() {
        BirdApi.cancelRequestWithTag(tag);
        super.onDestroy();
    }

    String level = "1";

    private void setSelect(View v) {
        switch (v.getId()) {
            case R.id.capacityLevel_1:
                capacityLevel_1.setTextColor(getResources().getColor(R.color.white));
                capacityLevel_2.setTextColor(getResources().getColor(R.color.blue_head_1));
                capacityLevel_3.setTextColor(getResources().getColor(R.color.blue_head_1));
                capacityLevel_1.setBackgroundColor(getResources().getColor(R.color.blue_head_1));
                capacityLevel_2.setBackgroundColor(getResources().getColor(R.color.white));
                capacityLevel_3.setBackgroundColor(getResources().getColor(R.color.white));
                level = "1";
                break;
            case R.id.capacityLevel_2:
                capacityLevel_1.setTextColor(getResources().getColor(R.color.blue_head_1));
                capacityLevel_2.setTextColor(getResources().getColor(R.color.white));
                capacityLevel_3.setTextColor(getResources().getColor(R.color.blue_head_1));
                capacityLevel_1.setBackgroundColor(getResources().getColor(R.color.white));
                capacityLevel_2.setBackgroundColor(getResources().getColor(R.color.blue_head_1));
                capacityLevel_3.setBackgroundColor(getResources().getColor(R.color.white));
                level = "2";
                break;
            case R.id.capacityLevel_3:
                capacityLevel_1.setTextColor(getResources().getColor(R.color.blue_head_1));
                capacityLevel_2.setTextColor(getResources().getColor(R.color.blue_head_1));
                capacityLevel_3.setTextColor(getResources().getColor(R.color.white));
                capacityLevel_1.setBackgroundColor(getResources().getColor(R.color.white));
                capacityLevel_2.setBackgroundColor(getResources().getColor(R.color.white));
                capacityLevel_3.setBackgroundColor(getResources().getColor(R.color.blue_head_1));
                level = "3";
                break;
        }
    }
}
