package com.pda.birdex.pda.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.interfaces.OnRecycleViewItemClickListener;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/29.
 */
public class TakingCheckActivity extends  BaseActivity {
    @Bind(R.id.title)
    TitleView title;
    @Bind(R.id.tv_taking_num)
    TextView tv_taking_num;
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


    List<String> currentMenuList = new ArrayList<>();
    String[] toolMenu;

    ContainerInfo containerInfo;
    TakingOrderNoInfoEntity orderNoInfoEntity;
    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_check_layout;
    }

    @Override
    public void initializeContentViews() {
        title.setTitle(getString(R.string.check_taking_info));
        title.setMenuVisble(true);
        toolMenu = getResources().getStringArray(R.array.taking_tool_menu);
        for (String title : toolMenu) {
            currentMenuList.add(title);
        }
        title.setSaveList(currentMenuList);
        title.setMenuVisble(true);
        orderNoInfoEntity = (TakingOrderNoInfoEntity) getIntent().getExtras().get("orderNoInfoEntity");
        containerInfo = (ContainerInfo) getIntent().getExtras().get("containerInfo");
        if(orderNoInfoEntity!=null) {
            tv_taking_num.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
            tv_taking_container.setText(containerInfo.getContainerId());
            tv_area.setText(containerInfo.getArea());
            tv_box_size.setText(containerInfo.getCount() + "");
            if(orderNoInfoEntity.getDetail().getOperationLog()!=null&orderNoInfoEntity.getDetail().getOperationLog().size()>0) {
                tv_operation_man.setText(orderNoInfoEntity.getDetail().getOperationLog().get(0).getOperator());
                tv_time.setText(orderNoInfoEntity.getDetail().getOperationLog().get(0).getOpterateTime());
            }
        }
        title.setOnSaveItemClickListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(TakingCheckActivity.this,TakingToolActivity.class);
                getIntent().getExtras().putInt("position", position);//把加载fragment的位置传递给takingtool
                intent.putExtras(getIntent().getExtras());
                startActivity(intent);
            }
        });
    }
}
