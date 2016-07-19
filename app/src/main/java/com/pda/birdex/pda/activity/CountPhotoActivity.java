package com.pda.birdex.pda.activity;

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
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.PhotoGVAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.fragments.BaseFragment;
import com.pda.birdex.pda.fragments.PhotoFragment;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
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
 * Created by hyj on 2016/6/23.
 */
public class CountPhotoActivity extends BarScanActivity implements View.OnClickListener, BaseFragment.OnFragmentInteractionListener, BackHandledInterface {

    private static final String TAG = "PhotoActivity";

    private final static int COMPRESS_DOWN = 3;

    // 存储照片路径的 list
    private List<String> pathList = new ArrayList<String>();

    // 存放所有返回图片地址的 list
    private List<String> photoUrl = new ArrayList<>();


    @Bind(R.id.titleView)
    com.pda.birdex.pda.widget.TitleView titleView;

    @Bind(R.id.vessel_et)
    com.pda.birdex.pda.widget.ClearEditText vessel_et;

    @Bind(R.id.upc_et)
    com.pda.birdex.pda.widget.ClearEditText upc_et;

    // 记录上传图片成功返回的 url条数
    private int sucCounts = 0;

    // 图片 fragment
    private com.pda.birdex.pda.fragments.PhotoFragment photoFragment;

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
//                                    String str = BirdApi.UPLOADIP + result;
                                    photoUrl.add(result);
                                    progress++;
                                    mProgress.setProgress(progress);
                                    sucCounts++;
                                    if (sucCounts == pathList.size()) {
                                        JSONObject jsonObject = new JSONObject();
                                        try {

                                            // 调用提交上传图片接口
                                            jsonObject.put("containerNo", vessel_et.getText() + "");
                                            jsonObject.put("upc", upc_et.getText() + "");
                                            jsonObject.put("isException", photoFragment.isChecked());
                                            String urlsStr = GsonHelper.createJsonString(photoUrl);
                                            JSONArray array = new JSONArray(urlsStr);
                                            jsonObject.put("photoIds", array);

                                            BirdApi.countingUploadPicSubmit(CountPhotoActivity.this, jsonObject, new RequestCallBackInterface() {

                                                @Override
                                                public void successCallBack(JSONObject object) {
                                                    try {
                                                        if ("success".equals(object.getString("result"))) {
                                                            T.showShort(CountPhotoActivity.this, getString(R.string.taking_upload_suc));
                                                            pathList.clear();
                                                            photoUrl.clear();
                                                            photoFragment.setPathList(new ArrayList<String>());
                                                            vessel_et.setText("");
                                                            upc_et.setText("");
                                                        } else {
                                                            T.showShort(CountPhotoActivity.this, getString(R.string.taking_upload_fal));
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    dismissDialog();

                                                }

                                                @Override
                                                public void errorCallBack(JSONObject object) {
                                                    T.showShort(CountPhotoActivity.this, getString(R.string.taking_upload_fal));
                                                    dismissDialog();
                                                }
                                            }, TAG, true);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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
    public int getbarContentLayoutResId() {
        return R.layout.activity_count_photo;
    }

    @Override
    public void barInitializeContentViews() {
        // 初始化 title
        titleView.setTitle("拍照");

        vessel_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                vessel_et.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(vessel_et);
                }
            }
        });
        vessel_et.requestFocus();
        upc_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                upc_et.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(upc_et);
                }
            }
        });

        photoFragment = new PhotoFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.framelayout, photoFragment).commit();
    }

    @Override
    public ClearEditText getClearEditText() {
        return vessel_et;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if(vessel_et.hasFocus()){
            upc_et.requestFocus();
        }
    }

    @OnClick({R.id.upload})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.upload:
                // 如果 upc 为空，照片 list 也为空， 不进行上传操作
                if (TextUtils.isEmpty(vessel_et.getText())) {
                    T.showShort(CountPhotoActivity.this, getString(R.string.count_vessel_empty));
                    return;
                }
                if (TextUtils.isEmpty(upc_et.getText())) {
                    T.showShort(CountPhotoActivity.this, getString(R.string.count_upc_empty));
                    return;
                }
                pathList = photoFragment.getPathList();
                if (pathList.size() == 0) {
                    T.showShort(CountPhotoActivity.this, getString(R.string.taking_upload_empty_p));
                } else {
                    if (pathList.size() <= 10) {
                        // 进度条
                        progressDialog();
                        // 上传图片
                        uploadPic();
                    } else {
                        // 限制图片数量不大于10张
                        T.showShort(CountPhotoActivity.this, getString(R.string.taking_upload_photo_count));
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
        mProgress = new ProgressDialog(this);
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

    @Override
    public void setSelectedFragment(BaseFragment selectedFragment) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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

}
