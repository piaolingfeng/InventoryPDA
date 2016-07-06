package com.pda.birdex.pda.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.response.CountingOrderNoInfoEntity;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.widget.TitleView;

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
        if (b.getString("location_position") != null) {//揽收容器
            title.setTitle(getString(R.string.check_taking_info));
            toolMenu = getResources().getStringArray(R.array.taking_tool_menu);
            takingOrderNoInfoEntity = (TakingOrderNoInfoEntity) getIntent().getExtras().get("orderNoInfoEntity");
        } else {//清点容器
            title.setTitle(getString(R.string.check_counting_info));
            tv_taking_num_head.setText(R.string.count_num);
            toolMenu = getResources().getStringArray(R.array.counting_tool_menu);
            countingOrderNoInfoEntity = (CountingOrderNoInfoEntity) getIntent().getExtras().get("countingOrderNoInfoEntity");
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
            if(containerInfo!=null) {
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
            if(containerInfo!=null) {
                tv_taking_container.setText(containerInfo.getContainerId());
                tv_area.setText(containerInfo.getArea());
                tv_box_size.setText(containerInfo.getCount() + "");
            }
            if (countingOrderNoInfoEntity.getDetail().getOperationLog() != null & countingOrderNoInfoEntity.getDetail().getOperationLog().size() > 0) {
                tv_operation_man.setText(countingOrderNoInfoEntity.getDetail().getOperationLog().get(0).getOperator());
                tv_time.setText(countingOrderNoInfoEntity.getDetail().getOperationLog().get(0).getOpterateTime());
            }
        }

        title.setOnSaveItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                if (b.getString("location_position") != null)//揽收容器
                    intent.setClass(CheckActivity.this, TakingToolActivity.class);
                else
                    intent.setClass(CheckActivity.this, CountToolActivity.class);
                Bundle b = getIntent().getExtras();//把加载fragment的位置传递给takingtool
                b.putInt("position", position);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }
}