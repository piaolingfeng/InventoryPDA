package com.pda.birdex.pda.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.BaseActivity;
import com.pda.birdex.pda.activity.PhotoShowActivity;
import com.pda.birdex.pda.adapter.PhotoGVAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.entity.UpcData;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.CountingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.HideSoftKeyboardUtil;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/6/24.
 */
public class CountToolClearFragment extends BarScanBaseFragment implements View.OnClickListener {

    String tag = "CountToolClearFragment";

    @Bind(R.id.edt_count_num)
    com.pda.birdex.pda.widget.ClearEditText edt_count_num;

    @Bind(R.id.edt_count_length)
    com.pda.birdex.pda.widget.ClearEditText edt_count_length;

    @Bind(R.id.edt_upc)
    com.pda.birdex.pda.widget.ClearEditText edt_upc;

    // 揽收单号
    @Bind(R.id.tv_count_num)
    TextView tv_count_num;

    // 存储照片路径的 list
    private List<String> pathList = new ArrayList<String>();

    // 存放所有返回图片地址的 list
    private List<String> photoUrl = new ArrayList<>();


    private final static int COMPRESS_DOWN = 3;

    CountingOrderNoInfoEntity countingOrderNoInfoEntity;//清点任务详情
    ContainerInfo containerInfo;//位置2进来时传进来的item

    private String owner;
    private String tid;
    private String orderNo;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_count_tool_clear_layout;
    }


    // 记录上传图片成功返回的 url条数
    private int sucCounts = 0;

    // 图片 fragment
    private PhotoFragment photoFragment;

    private static final int PIC_COMPRESS = 1;
    // 压缩完图片后 重新刷新
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case PIC_COMPRESS:
                    MyTask task = new MyTask();
                    task.execute();
                    break;

                case COMPRESS_DOWN:

                    String path1 = msg.obj.toString();

                    RequestParams myparams = new RequestParams();
                    File file = new File(path1);
                    try {
                        myparams.put("file", file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        dismissDialog();
                    }

                    BirdApi.uploadPic(MyApplication.getInstans(), myparams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                if ("false".equals(response.getString("ret"))) {
                                    T.showShort(MyApplication.getInstans(), "上传失败");
                                    dismissDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                            dismissDialog();
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
                            dismissDialog();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            // 返回的是 html格式，进行解析
                            String[] spit1 = responseString.split("MD5:");
                            if (spit1.length >= 2) {
                                String tail = spit1[1];
                                String[] spit2 = tail.split("</h1>");
                                if (spit2.length >= 2) {
                                    String result = spit2[0].trim();
                                    photoUrl.add(result);
                                    progress++;
                                    mProgress.setProgress(progress);
                                    sucCounts++;
                                    if (sucCounts == pathList.size()) {
                                        // 调用提交 揽收接口
                                        dataSubmit();
                                    }
                                } else {
                                    T.showShort(MyApplication.getInstans(), "上传失败");
                                    dismissDialog();
                                }
                            } else {
                                T.showShort(MyApplication.getInstans(), "上传失败");
                                dismissDialog();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            T.showShort(MyApplication.getInstans(), "上传失败");
                            dismissDialog();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            T.showShort(MyApplication.getInstans(), "上传失败");
                            dismissDialog();
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                        }
                    });

                    break;
            }
        }
    };

    @Override
    public void barInitializeContentViews() {

        countingOrderNoInfoEntity = (CountingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("countingOrderNoInfoEntity");
        containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
        if (countingOrderNoInfoEntity != null) {
            orderNo = countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getOrderNo();
            tv_count_num.setText(orderNo);
            owner = countingOrderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo();
            tid = countingOrderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid();
        }

        edt_count_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_count_num.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_count_num);
                }
            }
        });
        edt_count_num.requestFocus();//获取焦点
        edt_upc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                edt_upc.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(edt_upc);
                }
            }
        });


        photoFragment = new PhotoFragment(false);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.framelayout_1, photoFragment).commit();
    }

    private ProgressDialog mProgress;

    private int progress = 0;

    /**
     * 进度条Dialog
     */
    private void progressDialog() {
        progress = 0;
        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("上传中...");
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setCancelable(false);
        mProgress.setMax(pathList.size());
        mProgress.show();
    }


    @OnClick({R.id.btn_commit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                // 揽收容器号不能为空
                if (TextUtils.isEmpty(edt_count_num.getText())) {
                    T.showShort(getContext(), getString(R.string.count_num_toast));
                    return;
                }

                if (TextUtils.isEmpty(edt_upc.getText())) {
                    T.showShort(getContext(), getString(R.string.upc_empty));
                    return;
                }

                if (TextUtils.isEmpty(edt_count_length.getText())) {
                    T.showShort(getContext(), getString(R.string.box_size_toast));
                    return;
                }

                pathList = photoFragment.getPathList();
                // 如果有拍摄照片  则需要将照片上传到图片服务器
                if (pathList.size() > 0) {
                    // 进度条
                    progressDialog();
                    // 上传图片
                    uploadPic();
                } else {
                    // 直接调用接口
                    dataSubmit();
                }

                break;
        }
    }

    // 调用数据提交接口
    private void dataSubmit() {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("tid", tid);
            jsonObject.put("owner", owner);
            jsonObject.put("containerNo", edt_count_num.getText() + "");

            String str = GsonHelper.createJsonString(photoUrl);
            JSONArray jsonArray = new JSONArray(str);
            jsonObject.put("photoIds", jsonArray);

            List<UpcData> list = new ArrayList<>();
            UpcData upcData = new UpcData();
            upcData.setCount(edt_count_length.getText() + "");
            upcData.setUpc(edt_upc.getText() + "");
            list.add(upcData);

            JSONArray upcArr = new JSONArray(GsonHelper.createJsonString(list));
            jsonObject.put("upcData", upcArr);

            BirdApi.countSubmit(getActivity(), jsonObject, new RequestCallBackInterface() {

                @Override
                public void successCallBack(JSONObject object) {
                    try {
                        if ("success".equals(object.getString("result"))) {
                            //清点日志上报
                            String upc = edt_upc.getText().toString();
                            int count = Integer.parseInt(edt_count_length.getText().toString());
                            String ctNo = edt_count_num.getText().toString();
                            MyApplication.loggingUpload.countClearCommit(getActivity(), tag, orderNo, tid, upc,ctNo,count);
                            T.showShort(getContext(), getString(R.string.taking_upload_suc));
                        } else {
                            T.showShort(getContext(), getString(R.string.taking_upload_fal));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dismissDialog();
                }

                @Override
                public void errorCallBack(JSONObject object) {
                    T.showShort(getContext(), getString(R.string.taking_upload_fal));
                    dismissDialog();
                }
            }, tag, true);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 关闭 上传进度条
    private void dismissDialog() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }


    // 上传图片
    private void uploadPic() {
        Message message = Message.obtain();
        message.what = PIC_COMPRESS;
        handler.sendMessage(message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(tag);
    }

    class MyTask extends AsyncTask<String, Integer, Void> {
        String path;

        @Override
        protected Void doInBackground(String... params) {

            photoUrl.clear();
            sucCounts = 0;

            for (int i = 0; i < pathList.size(); i++) {
                path = pathList.get(i);
                Message message = Message.obtain();
                message.what = COMPRESS_DOWN;
                message.obj = path;
                handler.sendMessage(message);
            }
            return null;
        }
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_count_num;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (this.isVisible()) {
            HideSoftKeyboardUtil.hideSoftKeyboard((BaseActivity)getActivity());
            if(edt_count_num.hasFocus()){
                edt_upc.requestFocus();//切换焦点
                return;
            }
            if(edt_upc.hasFocus()){
                edt_count_length.requestFocus();
                setEdt_input(null);
            }
        }
//            if ("1".equals(from)) {
//                // 说明是从揽收进入的 需要通过容器号 调用接口  获取区域信息
//                getAreaMes(code);
//            }
//        }
    }
}
