package com.pda.birdex.pda.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.MissionMerchantActivity;
import com.pda.birdex.pda.adapter.MerchantAdapter;
import com.pda.birdex.pda.entity.Merchant;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.utils.StringUtils;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/22.
 */
public class MissionBussniessFragment extends BaseFragment {


    @Bind(R.id.xrcy)
    XRecyclerView xrcy;
    String tag = "MissionBussniessFragment";

    MerchantAdapter adapter;
    List<Merchant> list;//放所有的商家；
    List<Merchant> index_list;//放优化筛选出来的商家
    List<Merchant> now_displayList;
    String[] alist = {};

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
        alist = getResources().getStringArray(R.array.merchant_index);
        list = new ArrayList<>();
        index_list = new ArrayList<>();
        now_displayList = new ArrayList<>();
        adapter = new MerchantAdapter(getActivity(), now_displayList);
        xrcy.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        xrcy.setAdapter(adapter);
        adapter.setOnRecycleViewItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (getString(R.string.more).equals(now_displayList.get(position).getMerchantName())) {
                    bus.post("count_mission_other");//进入到其他页面，数据替换成其他list的实体
                } else {
                    Intent intent = new Intent(getActivity(), MissionMerchantActivity.class);
                    bundle.putString("MerchantId", now_displayList.get(position).getMerchantId());
                    bundle.putString("merchantName",now_displayList.get(position).getMerchantName());
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            }
        });
        xrcy.setPullRefreshEnabled(false);
        xrcy.setLoadingMoreEnabled(false);
    }

    //请求网络后返回的list
    @Subscribe
    public void onEvent(List<Merchant> list) {
//       if(bundle!=null){
//           this.list = (List<Merchant>) bundle.get("merchantList");
//       }
        this.list = list;//保存所有商家
        dealIndexList();//优化后的首页列表，默认情况要显示
        now_displayList = index_list;//获取到数据后显示首页列表
        adapter.setList(now_displayList);
        adapter.notifyDataSetChanged();
    }

    //优化筛选列表
    private void dealIndexList() {
        index_list = new ArrayList<>();
        for (int i = 0; i < alist.length; i++) {
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).getMerchantName().equals(alist[i])) {
                    index_list.add(list.get(j));
                    break;
                }
            }
        }
        Merchant merchant = new Merchant();
        merchant.setMerchantName(getString(R.string.more));//最后一项添加more
        index_list.add(merchant);
    }

    @Subscribe
    public void onSerchEvent(String text) {
        if (StringUtils.isEmpty(text)) {
            now_displayList = index_list;
        } else {
            now_displayList = search(text, list);
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (adapter!=null) {
            now_displayList = index_list;
            adapter.setList(index_list);
            adapter.notifyDataSetChanged();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void lazyLoad() {

    }
}
