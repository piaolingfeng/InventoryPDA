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
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.PhotoShowActivity;
import com.pda.birdex.pda.adapter.PhotoGVAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.entity.TakingOrder;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
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
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingPhotoFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "TakingPhotoFragment";

    @Bind(R.id.edt_taking_num)
    com.pda.birdex.pda.widget.ClearEditText edt_taking_num;

    // 揽收单号
    @Bind(R.id.tv_taking_num)
    TextView tv_taking_num;

    private final static int COMPRESS_DOWN = 3;

    // 存储照片路径的 list
    private List<String> pathList = new ArrayList<String>();

    // 标记是从揽收、或者 揽收任务 跳转过来的
    // 1:揽收 2:揽收任务
    private String from;
    private String tid;//供上传日志使用

    // 存放所有返回图片地址的 list
    private List<String> photoUrl = new ArrayList<>();
    TakingOrder takingOrder;//位置1进来传来的实体
    TakingOrderNoInfoEntity orderNoInfoEntity;//位置2进来传来的实体
    ContainerInfo containerInfo;//位置2进来时传进来的item

    // 记录上传图片成功返回的 url条数
    private int sucCounts = 0;

    // 图片 fragment
    private PhotoFragment photoFragment;

    private static final int PIC_COMPRESS = 1;

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
                                    T.showShort(MyApplication.getInstans(), getString(R.string.taking_upload_fal));
                                    dismissDialog();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            super.onSuccess(statusCode, headers, responseString);
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
                                        JSONObject jsonObject = new JSONObject();
                                        try {

                                            // 调用提交上传图片接口
                                            jsonObject.put("containerNo", edt_taking_num.getText() + "");
                                            jsonObject.put("isException", photoFragment.isChecked());
                                            String urlsStr = GsonHelper.createJsonString(photoUrl);
                                            JSONArray array = new JSONArray(urlsStr);
                                            jsonObject.put("photoIds", array);
                                            //上传日志
                                            String orderId = tv_taking_num.getText().toString();
                                            MyApplication.loggingUpload.takeTakePhoto(getActivity(), tag, orderId, tid, sucCounts, photoFragment.isChecked());
                                            BirdApi.uploadPicSubmit(getContext(), jsonObject, new RequestCallBackInterface() {

                                                @Override
                                                public void successCallBack(JSONObject object) {
                                                    try {
                                                        if ("success".equals(object.getString("result"))) {
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
                                            T.showShort(MyApplication.getInstans(), getString(R.string.taking_upload_fal));
                                            dismissDialog();
                                        }
                                    }
                                } else {
                                    T.showShort(MyApplication.getInstans(), getString(R.string.taking_upload_fal));
                                    dismissDialog();
                                }
                            } else {
                                T.showShort(MyApplication.getInstans(), getString(R.string.taking_upload_fal));
                                dismissDialog();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            T.showShort(MyApplication.getInstans(), getString(R.string.taking_upload_fal));
                            dismissDialog();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            T.showShort(MyApplication.getInstans(), getString(R.string.taking_upload_fal));
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
    public int getbarContentLayoutResId() {
        return R.layout.fragment_taking_tool_photo_layout;
    }

    @Override
    public void barInitializeContentViews() {

        from = getActivity().getIntent().getExtras().getString("location_position");

        if ("1".equals(from)) {
            takingOrder = (TakingOrder) getActivity().getIntent().getExtras().get("takingOrder");
            tv_taking_num.setText(takingOrder.getBaseInfo().getTakingOrderNo());
            tid = takingOrder.getBaseInfo().getTid();
//            edt_taking_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (!hasFocus) {
//                        getAreaMes(edt_taking_num.getText() + "");
//                    }
//                }
//            });
        } else {//打印数量
            orderNoInfoEntity = (TakingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("orderNoInfoEntity");
            containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
            tv_taking_num.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
//            tv_area.setText(containerInfo.getArea());
        }
        edt_taking_num.requestFocus();

        photoFragment = new PhotoFragment();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.framelayout_2, photoFragment).commit();
    }


    @OnClick({R.id.btn_commit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                // 如果 upc 为空，照片 list 也为空， 不进行上传操作
                if (TextUtils.isEmpty(edt_taking_num.getText())) {
                    T.showShort(getContext(), getString(R.string.taking_num_toast));
                    return;
                }
                pathList = photoFragment.getPathList();
                if (pathList.size() == 0) {
                    T.showShort(getContext(), getString(R.string.taking_upload_empty_p));
                } else {
                    if (pathList.size() <= 10) {
                        // 进度条
                        progressDialog();
                        // 上传图片
                        uploadPic();
                    } else {
                        // 限制图片数量在 3-5 之间
                        T.showShort(getContext(), getString(R.string.taking_upload_photo_count));
                    }
                }
                break;
        }
    }

    // 上传图片
    private void uploadPic() {
        Message message = Message.obtain();
        message.what = PIC_COMPRESS;
        handler.sendMessage(message);
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


    // 关闭 上传进度条
    private void dismissDialog() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
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
        return edt_taking_num;
    }


    @Override
    public void ClearEditTextCallBack(String code) {
//        if (!this.isHidden()) {
//            if ("1".equals(from)) {
//                // 说明是从揽收进入的 需要通过容器号 调用接口  获取区域信息
//                getAreaMes(code);
//            }
//        }
    }
}
