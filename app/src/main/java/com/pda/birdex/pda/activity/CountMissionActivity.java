package com.pda.birdex.pda.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.IndexAdapter;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/22.
 */
public class CountMissionActivity extends BarScanActivity {

    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.edt_input_business)
    ClearEditText edt_input_business;
    @Bind(R.id.rcy)
    RecyclerView rcy;

    IndexAdapter adapter;
    List<String> indexList;
    String []alist ={"格格家","美囤妈妈","网易惠惠","懂球帝","其他"};

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_countmission_layout;
    }

    @Override
    public void barInitializeContentViews() {

        title.setTitle("清点任务");
        indexList = new ArrayList<>();
        for(int i =0 ; i<alist.length;i++){
            indexList.add(alist[i]);
        }
        edt_input_business.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String string = v.getText().toString();
//                    search(string);
                    ClearEditTextCallBack(string);
//                    T.showShort(MyApplication.getInstans(), "dddd" + actionId);
                }
                return false;
            }
        });
        adapter = new IndexAdapter(this,indexList);
        rcy.setLayoutManager(new GridLayoutManager(this,2));
        rcy.setAdapter(adapter);
        adapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 5){

                }else{

                }
            }
        });
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_input_business;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }
}
