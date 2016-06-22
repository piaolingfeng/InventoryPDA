package com.pda.birdex.pda.interfaces;

import org.json.JSONObject;

/**
 * Created by chuming.zhuang on 2016/6/22.
 */
public interface RequestCallBackInterface {
    void successCallBack(JSONObject object);
    void errorCallBack(JSONObject object);
}
