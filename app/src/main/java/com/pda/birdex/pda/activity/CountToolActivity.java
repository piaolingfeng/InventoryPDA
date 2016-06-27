package com.pda.birdex.pda.activity;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by chuming.zhuang on 2016/6/24.
 */
public class CountToolActivity extends BaseActivity {
    @Bind(R.id.title)
    TitleView title;

    int tabPosition=0;
    List<String> currentMenuList = new ArrayList<>();

    private void dealToolMenuList(){
        switch (tabPosition){
            case 0://未开始
                break;
            case 1://已分类
                break;
            case 2://已清点
                break;
        }
    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_tool_layout;
    }

    @Override
    public void initializeContentViews() {
        String []tabList = {getString(R.string.not_start),getString(R.string.has_classified),
                getString(R.string.has_counted),getString(R.string.has_transfer)};//tablayoutName
        tabPosition = getIntent().getIntExtra("statusPosition",0);
        String []toolMenu = getResources().getStringArray(R.array.tool_menu);
        for (String title:toolMenu){
            currentMenuList.add(title);
        }
        title.setTitle(currentMenuList.get(0));
//        title.
    }
}
