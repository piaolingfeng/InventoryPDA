package com.pda.birdex.pda.fragments;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.CountMissionMerchantActivity;
import com.pda.birdex.pda.adapter.MerchantOtherAdapter;
import com.pda.birdex.pda.entity.Merchant;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.utils.decoration.DividerItemDecoration;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/23.
 */
public class CountMissionBussinessOtherFragment extends BaseFragment {
    @Bind(R.id.xrcy)
    XRecyclerView xrcy;
    MerchantOtherAdapter adapter;
    List<Merchant> list;//放所有的商家；
    List<Merchant> now_displayList;
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
        bus.register(this);
        if (bundle != null) {
            list = (List<Merchant>) bundle.get("merchantList");
            title = bundle.getString("HeadName");
            now_displayList = list;//获取到数据后显示首页列表
        }
        adapter = new MerchantOtherAdapter(getContext(), now_displayList);
        adapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), CountMissionMerchantActivity.class);
                bundle.putString("MerchantId", now_displayList.get(position).getMerchantId());
                bundle.putString("merchantName",now_displayList.get(position).getMerchantName());
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


    @Subscribe
    public void onEvent(List<Merchant> list) {
        this.list = list;//保存所有商家
        now_displayList = list;//获取到数据后显示首页列表
        adapter.setList(now_displayList);
        adapter.notifyDataSetChanged();
    }


    @Subscribe
    public void onSerchEvent(String text) {
        now_displayList = search(text, list);
        adapter.setList(now_displayList);
        adapter.notifyDataSetChanged();
    }

    //模糊查询本地列表
    public List<Merchant> search(String name, List<Merchant> list) {
        List<Merchant> results = new ArrayList<>();
        Pattern pattern = Pattern.compile(name);
        for (int i = 0; i < list.size(); i++) {
            Matcher matcher = pattern.matcher(list.get(i).getMerchantName());
            if (matcher.find()) {
                results.add(list.get(i));
            }
        }
        return results;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }

    @Override
    protected void lazyLoad() {

    }
}
