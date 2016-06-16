package com.pda.birdex.pda.utils;

import android.os.Environment;


/**
 * Created by chuming.zhuang on 2016/3/18.
 */
public class Constant {
    public static final String NAME = "/InventoryPDA";
    public static final String BASEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + NAME;

    //数据库名
    public static final String DBName="InventoryPDAData";

    //获取状态栏高度
    public static int Status_Height=0;
}
