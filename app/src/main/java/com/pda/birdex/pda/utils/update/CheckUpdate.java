package com.pda.birdex.pda.utils.update;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.utils.SafeProgressDialog;


/**
 * Created by weiyu.wei on 2015/11/27.
 */
public class CheckUpdate {

    private static String vercode;
    private static Context context;
    private static String description;

    /**
     * 从服务器端获得版本号与版本名称
     *
     * @return
     */
    private static void getServerVer() {
        try{
            // 先模拟需要升级的情况
            newDialog(context);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void check(Context con,String description) {
        context = con;
        CheckUpdate.description= description;
        getServerVer();
    }


    private static void newDialog(final Context context) {
        final Dialog meDialog = new SafeProgressDialog(context, R.style.semester_dialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_setting1, null);
        meDialog.setContentView(view);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView bt_confirm = (TextView) view.findViewById(R.id.bt_confirm);
        TextView bt_cancle = (TextView) view.findViewById(R.id.bt_cancle);
        TextView content = (TextView) view.findViewById(R.id.cet_content);
        StringBuffer sb = new StringBuffer();
        sb.append(CheckUpdate.description);
        if (!TextUtils.isEmpty(vercode)) {
            sb.append("：" + vercode);
        }
        sb.append("\n是否下载更新？?");
        tv_title.setText("版本更新");
        content.setText(sb.toString());

        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateService.class);
                context.startService(intent);
                meDialog.dismiss();
            }
        });

        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meDialog.dismiss();
            }
        });
        meDialog.show();
    }
}
