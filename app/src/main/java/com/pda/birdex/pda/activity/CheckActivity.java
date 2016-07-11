package com.pda.birdex.pda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.response.CountingOrderNoInfoEntity;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.widget.TitleView;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/29.
 */
public class CheckActivity extends BaseActivity {
    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.tv_taking_num)
    TextView tv_taking_num;
    @Bind(R.id.tv_taking_num_head)
    TextView tv_taking_num_head;
    @Bind(R.id.tv_print_num)
    TextView tv_print_num;
    @Bind(R.id.tv_taking_container)
    TextView tv_taking_container;
    @Bind(R.id.tv_check_map)
    Button tv_check_map;
    @Bind(R.id.tv_area)
    TextView tv_area;
    @Bind(R.id.tv_box_size)
    TextView tv_box_size;
    @Bind(R.id.tv_operation_man)
    TextView tv_operation_man;
    @Bind(R.id.tv_time)
    TextView tv_time;

    //查看清点容器的时候需要显示出下面的内容
    @Bind(R.id.tv_upc)
    TextView tv_upc;
    @Bind(R.id.tv_amount)
    TextView tv_amount;
    @Bind(R.id.pll_amount)
    PercentLinearLayout pll_amount;
    @Bind(R.id.pll_upc)
    PercentLinearLayout pll_upc;
    @Bind(R.id.pll_count_num)
    PercentLinearLayout pll_count_num;
    //入库才有的部分
    @Bind(R.id.pll_storage_position)
    PercentLinearLayout pll_storage_position;//库位
    @Bind(R.id.tv_storage_position)
    TextView tv_storage_position;
    Bundle b = null;

    List<String> currentMenuList = new ArrayList<>();
    String[] toolMenu;

    ContainerInfo containerInfo;
    TakingOrderNoInfoEntity takingOrderNoInfoEntity;//揽收任务详情
    CountingOrderNoInfoEntity countingOrderNoInfoEntity;//清点任务详情

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_check_layout;
    }

    @Override
    public void initializeContentViews() {
        b = getIntent().getExtras();
        if (b == null)
            return;
        if (b.getString("checkType").equals(getString(R.string.count))) {//清点
            title.setTitle(getString(R.string.check_counting_info));
            tv_taking_num_head.setText(R.string.count_num);
            pll_amount.setVisibility(View.VISIBLE);
            pll_upc.setVisibility(View.VISIBLE);
            toolMenu = getResources().getStringArray(R.array.counting_tool_menu);
            countingOrderNoInfoEntity = (CountingOrderNoInfoEntity) getIntent().getExtras().get("countingOrderNoInfoEntity");
        }
        if (b.getString("checkType").equals(getString(R.string.taking))) {//揽收
            title.setTitle(getString(R.string.check_taking_info));
            toolMenu = getResources().getStringArray(R.array.taking_tool_menu);
            takingOrderNoInfoEntity = (TakingOrderNoInfoEntity) getIntent().getExtras().get("orderNoInfoEntity");
        }
        if (b.getString("checkType").equals(getString(R.string.storge))) {//入库
            title.setTitle(getString(R.string.check_storage_info));
            tv_taking_num_head.setText(getString(R.string.count_vessel_no));
            tv_print_num.setText(getString(R.string.storage_order));
            pll_amount.setVisibility(View.VISIBLE);
            pll_upc.setVisibility(View.VISIBLE);
            toolMenu = getResources().getStringArray(R.array.storage_tool_list);
            pll_count_num.setVisibility(View.GONE);
            pll_storage_position.setVisibility(View.VISIBLE);
        }
        title.setMenuVisble(true);
        for (String title : toolMenu) {
            currentMenuList.add(title);
        }
        title.setSaveList(currentMenuList);
        title.setMenuVisble(true);
        containerInfo = (ContainerInfo) getIntent().getExtras().get("containerInfo");
        if (takingOrderNoInfoEntity != null) {
            tv_taking_num.setText(takingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
            if (containerInfo != null) {
                tv_taking_container.setText(containerInfo.getContainerId());
                tv_area.setText(containerInfo.getArea());
                tv_box_size.setText(containerInfo.getCount() + "");
            }
            if (takingOrderNoInfoEntity.getDetail().getOperationLog() != null & takingOrderNoInfoEntity.getDetail().getOperationLog().size() > 0) {
                tv_operation_man.setText(takingOrderNoInfoEntity.getDetail().getOperationLog().get(0).getOperator());
                tv_time.setText(takingOrderNoInfoEntity.getDetail().getOperationLog().get(0).getOpterateTime());

            }
        }

        if (countingOrderNoInfoEntity != null) {
            tv_taking_num.setText(countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getOrderNo());
            if (containerInfo != null) {
                tv_taking_container.setText(containerInfo.getContainerId());
                tv_area.setText(containerInfo.getArea());
                tv_box_size.setText(containerInfo.getCount() + "");
                tv_amount.setText(containerInfo.getCount() + "");
            }
//            tv_upc.setText(takingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid());upc暂时没有
            if (countingOrderNoInfoEntity.getDetail().getOperationLog() != null & countingOrderNoInfoEntity.getDetail().getOperationLog().size() > 0) {
                tv_operation_man.setText(countingOrderNoInfoEntity.getDetail().getOperationLog().get(0).getOperator());
                tv_time.setText(countingOrderNoInfoEntity.getDetail().getOperationLog().get(0).getOpterateTime());
            }
        }

        title.setOnSaveItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                if (b.getString("checkType").equals(getString(R.string.taking)))//揽收
                    intent.setClass(CheckActivity.this, TakingToolActivity.class);
                else if (b.getString("checkType").equals(getString(R.string.count)))
                    intent.setClass(CheckActivity.this, CountToolActivity.class);
                else if (b.getString("checkType").equals(getString(R.string.storge))) {
                    intent.setClass(CheckActivity.this, StorageToolActivity.class);
                }
                Bundle b = getIntent().getExtras();//把加载fragment的位置传递给takingtool
                b.putInt("position", position);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
}
