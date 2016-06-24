package com.pda.birdex.pda.fragments;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.CountBussinessActivity;
import com.pda.birdex.pda.adapter.IndexAdapter;
import com.pda.birdex.pda.entity.CommonItemEntity;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/22.
 */
public class CountMissionBussniessFragment extends BaseFragment {


    @Bind(R.id.xrcy)
    XRecyclerView xrcy;
    String tag = "CountMissionBussniessFragment";

    IndexAdapter adapter;
    List<CommonItemEntity> indexList;
    String[] alist = {"格格家", "美囤妈妈", "网易惠惠", "懂球帝", "其他"};


    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_countmissionbussiness_layout;
    }

    @Override
    public void initializeContentViews() {
        indexList = new ArrayList<>();
        for (int i = 0; i < alist.length; i++) {
            CommonItemEntity entity = new CommonItemEntity();
            entity.setName(alist[i]);
            entity.setCount("99");
            indexList.add(entity);
        }
        adapter = new IndexAdapter(getActivity(), indexList);
        xrcy.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        xrcy.setAdapter(adapter);
        adapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 4) {
                    bus.post("count_mission_other");//进入到其他页面，数据替换成其他list的实体
                } else {
                    Intent intent = new Intent(getActivity(), CountBussinessActivity.class);
                    intent.putExtra("bussinessCode","");
                    getActivity().startActivity(intent);
                }
            }
        });
        xrcy.setPullRefreshEnabled(false);
        xrcy.setLoadingMoreEnabled(false);
    }

    //通过网络请求获取商家列表
    public void getBussiness(){

    }

    @Override
    protected void lazyLoad() {

    }
}
