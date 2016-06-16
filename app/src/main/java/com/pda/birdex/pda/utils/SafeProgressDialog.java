package com.pda.birdex.pda.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

/**
 * Created by yujie.huang on 2016/2/17.
 */
public class SafeProgressDialog extends Dialog {

    Activity mParentActivity;

    public SafeProgressDialog(Context context, int theme) {
        super(context, theme);
        mParentActivity = (Activity) context;
    }


    @Override
    public void dismiss()
    {
        if (mParentActivity != null && !mParentActivity.isFinishing())
        {
            super.dismiss();    //调用超类对应方法
        }
    }

}
