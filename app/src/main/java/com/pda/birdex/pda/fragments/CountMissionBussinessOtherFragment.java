package com.pda.birdex.pda.fragments;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.CountBussinessActivity;
import com.pda.birdex.pda.adapter.CountMissionItemOtherAdapter;
import com.pda.birdex.pda.entity.CommonItemEntity;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.utils.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/23.
 */
public class CountMissionBussinessOtherFragment extends BaseFragment {
    @Bind(R.id.xrcy)
    XRecyclerView xrcy;
    CountMissionItemOtherAdapter adapter;
    List<CommonItemEntity> list = new ArrayList<>();
    String[] alists = {"aaa", "bbb", "ccc", "ddd", "eee"};
    String title = "";

    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.fragment_countmissionbussiness_layout;
    }

    @Override
    public void initializeContentViews() {
        for (int i = 0; i < alists.length; i++) {
            CommonItemEntity entity = new CommonItemEntity();
            entity.setName(alists[i]);
            entity.setCount("20");
            list.add(entity);
        }
        adapter = new CountMissionItemOtherAdapter(getContext(), list);
        adapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), CountBussinessActivity.class);
                bundle.putString("bussinessCode", "");
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        xrcy.setLayoutManager(new LinearLayoutManager(getActivity()));
        xrcy.setAdapter(adapter);
        xrcy.setLoadingMoreEnabled(true);
        xrcy.setPullRefreshEnabled(false);
        xrcy.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {

            }
        });
        xrcy.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL_LIST));
    }

    //通过网络请求获取其他商家信息
    public void getOtherBussiness() {

    }

    @Override
    protected void lazyLoad() {

    }
}
