package com.pda.birdex.pda.response;

import java.io.Serializable;

/**
 * Created by chuming.zhuang on 2016/6/27.
 */
public class BaseEntity  implements Serializable{
    String result = "";//返回success,表示成功，fail表示失败
    String errMsg = "";

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    /**
     * Created by chuming.zhuang on 2016/6/27.
     */

}
