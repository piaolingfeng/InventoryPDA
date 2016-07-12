package com.pda.birdex.pda.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by chuming.zhuang on 2016/7/8.
 * 拍照
 */
public class StoragePhotoFragment extends BarScanBaseFragment implements View.OnClickListener {

    public static final String TAG = "StoragePhotoFragment";

    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;
    @Bind(R.id.btn_edit)
    Button btn_edit;
    @Bind(R.id.edt_upc)
    ClearEditText edt_upc;
    @Bind(R.id.tv_upc)
    TextView tv_upc;
    @Bind(R.id.btn_commit)
    Button btn_commit;
    // 异常 textview
    @Bind(R.id.tv_exception)
    TextView tv_exception;

    // 图片 fragment
    private PhotoFragment photoFragment;

    // 存储照片路径的 list
    private List<String> pathList = new ArrayList<String>();

    // 存放所有返回图片地址的 list
    private List<String> photoUrl = new ArrayList<>();

    // 记录上传图片成功返回的 url条数
    private int sucCounts = 0;

    private final static int COMPRESS_DOWN = 3;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
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
                                            jsonObject.put("containerNo", tv_vessel_num.getText() + "");
//                                            jsonObject.put("owner", ); owner暂时拿不到
                                            jsonObject.put("upc", edt_upc.getText() + "");
                                            jsonObject.put("isException", photoFragment.isChecked());
                                            String urlsStr = GsonHelper.createJsonString(photoUrl);
                                            JSONArray array = new JSONArray(urlsStr);
                                            jsonObject.put("photoIds", array);
                                            BirdApi.postStockUploadPhoto(getContext(), jsonObject, new RequestCallBackInterface() {

                                                @Override
                                                public void successCallBack(JSONObject object) {
                                                    try {
                                                        if ("success".equals(object.getString("result"))) {
                                                            T.showShort(getContext(), getString(R.string.taking_upload_suc));
                                                            disableEditMode();
                                                            photoFragment.showException(false);
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
                                            }, TAG, true);
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
        return R.layout.fragment_storage_photo_layout;
    }

    @Override
    public void barInitializeContentViews() {
        edt_upc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    String string = v.getText().toString();
                    ClearEditTextCallBack(string);
                }
                return false;
            }
        });

        photoFragment = new PhotoFragment();
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.framelayout_1, photoFragment).commit();
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_upc;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (this.isVisible && edt_upc.getVisibility() == View.VISIBLE) {

        }
    }

    private void commit() {
        // 进度条
        progressDialog();

        MyTask task = new MyTask();
        task.execute();
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

    private void editMode() {
        btn_edit.setVisibility(View.INVISIBLE);
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
        edt_upc.setVisibility(View.VISIBLE);
        tv_upc.setVisibility(View.INVISIBLE);
    }

    private void disableEditMode() {
        btn_edit.setVisibility(View.VISIBLE);
        btn_commit.setClickable(false);
        btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
        btn_commit.setTextColor(getResources().getColor(R.color.white));
        edt_upc.setVisibility(View.INVISIBLE);
        tv_upc.setVisibility(View.VISIBLE);

        if(photoFragment.isChecked()){
            tv_exception.setVisibility(View.VISIBLE);
        } else {
            tv_exception.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.btn_commit, R.id.btn_edit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_commit:
                if (TextUtils.isEmpty(tv_vessel_num.getText())) {
                    T.showShort(getContext(), getString(R.string.count_vessel_empty));
                    return;
                }
                if (TextUtils.isEmpty(edt_upc.getText())) {
                    T.showShort(getContext(), getString(R.string.count_upc_empty));
                    return;
                }
                pathList = photoFragment.getPathList();
                if (pathList == null || pathList.size() == 0) {
                    T.showShort(getContext(), getString(R.string.taking_upload_empty_p));
                    return;
                } else {
                    if (pathList.size() <= 10) {
                        commit();
                    } else {
                        T.showShort(getContext(), getString(R.string.taking_upload_photo_count));
                        return;
                    }
                }
                break;
            case R.id.btn_edit:
                editMode();
                photoFragment.showException(true);
                break;
        }
    }
}
