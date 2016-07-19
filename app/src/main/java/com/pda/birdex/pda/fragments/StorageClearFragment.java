package com.pda.birdex.pda.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.PhotoGVUNAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.UpcData;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.StockInContainerInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.MyGridView;

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
 * 清点
 */
public class StorageClearFragment extends BarScanBaseFragment implements View.OnClickListener {

    public static final String TAG = "StorageClearFragment";

    @Bind(R.id.tv_vessel_num)
    TextView tv_vessel_num;
    @Bind(R.id.tv_storage_order)
    TextView tv_storage_order;
    @Bind(R.id.btn_edit)
    Button btn_edit;
    @Bind(R.id.edt_upc)
    ClearEditText edt_upc;
    @Bind(R.id.tv_upc)
    TextView tv_upc;
    @Bind(R.id.edt_amount)
    ClearEditText edt_amount;
    @Bind(R.id.tv_amount)
    TextView tv_amount;
    @Bind(R.id.btn_commit)
    Button btn_commit;

    @Bind(R.id.gv)
    MyGridView gv;

    @Bind(R.id.framelayout_2)
    FrameLayout frameLayout;

    // 图片 fragment
    private PhotoFragment photoFragment;

    // 存储照片路径的 list
    private List<String> pathList = new ArrayList<String>();

    // 存放所有返回图片地址的 list
    private List<String> photoUrl = new ArrayList<>();

    private StockInContainerInfoEntity entity;
    private String stockNum;//容器号

    // 记录上传图片成功返回的 url条数
    private int sucCounts = 0;

    private final static int COMPRESS_DOWN = 3;
    private final static int UPLOAD = 4;

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
                                        commit();
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
                case UPLOAD:
                    commit();
                    break;
            }
        }
    };

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_storage_clear_layout;
    }

    @Override
    public void barInitializeContentViews() {

        photoFragment = new PhotoFragment(false);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.framelayout_2, photoFragment).commit();

        bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            entity = (StockInContainerInfoEntity) bundle.getSerializable("StockInContainerInfoEntity");

            stockNum = bundle.getString("stockNum");
            if (!TextUtils.isEmpty(stockNum)) {
                tv_vessel_num.setText(stockNum);
            }
            String order = entity.getOrderNo();
            if (!TextUtils.isEmpty(order)) {
                tv_storage_order.setText(order);
            }

            List<String> urls = entity.getPhotoUrl();
            if (urls != null && urls.size() > 0) {
                photoFragment.setPathList(urls);
            }

            if (entity.getUpcData().size() > 0) {
                tv_upc.setText(entity.getUpcData().get(0).getUpc());//upc取第一条数据
                edt_upc.setText(entity.getUpcData().get(0).getUpc());//upc取第一条数据
                String count = entity.getUpcData().get(0).getCount();
                if (!TextUtils.isEmpty(count)) {
                    edt_amount.setText(count);
                    tv_amount.setText(count);
                }

                disableEditMode();
            }

        }
    }

    private void editMode() {
        btn_edit.setVisibility(View.INVISIBLE);
        btn_commit.setClickable(true);
        btn_commit.setBackgroundResource(R.drawable.rect_fullbluew_selector);
        btn_commit.setTextColor(getResources().getColor(R.color.btn_blue_selector));
        edt_upc.setVisibility(View.VISIBLE);
        tv_upc.setVisibility(View.INVISIBLE);
        edt_amount.setVisibility(View.VISIBLE);
        tv_amount.setVisibility(View.INVISIBLE);

        gv.setVisibility(View.GONE);
        frameLayout.setVisibility(View.VISIBLE);
    }

    private void disableEditMode() {
        btn_edit.setVisibility(View.VISIBLE);
        btn_commit.setClickable(false);
        btn_commit.setBackgroundResource(R.drawable.rect_fullgray);
        btn_commit.setTextColor(getResources().getColor(R.color.white));
        edt_upc.setVisibility(View.INVISIBLE);
        tv_upc.setVisibility(View.VISIBLE);
        edt_amount.setVisibility(View.INVISIBLE);
        tv_amount.setVisibility(View.VISIBLE);

        pathList = photoFragment.getPathList();
        PhotoGVUNAdapter adapter = new PhotoGVUNAdapter(getContext(), pathList);
        gv.setAdapter(adapter);
        gv.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_upc;
    }

    @Override
    public void ClearEditTextCallBack(String code) {
        if (!this.isHidden() && edt_upc.getVisibility() == View.VISIBLE) {
            if (edt_upc.hasFocus()) {
                edt_amount.requestFocus();//进入下一个
            } else
                edt_upc.requestFocus();
        }
    }


    //提交
    private void commit() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (entity != null) {
                jsonObject.put("tid", entity.getTid());
                jsonObject.put("owner", entity.getOwner());
            }
            jsonObject.put("containerNo", tv_vessel_num.getText() + "");
            String urlsStr = GsonHelper.createJsonString(photoUrl);
            JSONArray array = new JSONArray(urlsStr);
            jsonObject.put("photoIds", array);

            List<UpcData> upcDatas = new ArrayList<>();
            UpcData upcData = new UpcData();
            upcData.setUpc(edt_upc.getText() + "");
            upcData.setCount(edt_amount.getText() + "");
            upcDatas.add(upcData);

            String upcStr = GsonHelper.createJsonString(upcDatas);
            JSONArray array1 = new JSONArray(upcStr);
            jsonObject.put("upcData", array1);

            BirdApi.postStockCounting(getContext(), jsonObject, new RequestCallBackInterface() {

                @Override
                public void successCallBack(JSONObject object) {
                    try {
                        if ("success".equals(object.getString("result"))) {
                            T.showShort(getContext(), getString(R.string.taking_submit_suc));
                            disableEditMode();
                            photoFragment.showException(false);
                            tv_upc.setText(edt_upc.getText());
                            tv_amount.setText(edt_amount.getText());
                            //日志上报
                            String orderId = entity.getOrderNo();
                            String tid = entity.getTid();
                            String upc = edt_upc.getText()+"";
                            long upcCount=Long.parseLong(edt_amount.getText().toString());
                            String ctNo = tv_vessel_num.getText().toString();
                            MyApplication.loggingUpload.stockInClear(getActivity(),TAG,orderId,tid,ctNo,upc,upcCount);
                        } else {
                            T.showShort(getContext(), getString(R.string.taking_submit_fal));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dismissDialog();
                }

                @Override
                public void errorCallBack(JSONObject object) {
                    T.showShort(getContext(), getString(R.string.taking_submit_fal));
                    dismissDialog();
                }
            }, TAG, true);
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

    // 上传图片
    private void uploadPic() {
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
                if(path.startsWith("http")){
                    // http 开头的 说明已经上传过 图片服务器了
                    String[] md5s = path.split("/");
                    String md5 = md5s[md5s.length-1];

                    progress++;
                    mProgress.setProgress(progress);
                    sucCounts++;
                    photoUrl.add(md5);

                    if (sucCounts == pathList.size()) {
                        Message message = Message.obtain();
                        message.what = UPLOAD;
                        message.obj = path;
                        handler.sendMessage(message);
                    }
                }else {
                    Message message = Message.obtain();
                    message.what = COMPRESS_DOWN;
                    message.obj = path;
                    handler.sendMessage(message);
                }
            }
            return null;
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
                if (TextUtils.isEmpty(tv_storage_order.getText())) {
                    T.showShort(getContext(), getString(R.string.storage_order_empty));
                    return;
                }
                if (TextUtils.isEmpty(edt_upc.getText())) {
                    T.showShort(getContext(), getString(R.string.count_upc_empty));
                    return;
                }
                if (TextUtils.isEmpty(edt_amount.getText())) {
                    T.showShort(getContext(), getString(R.string.amount_empty));
                    return;
                }
                pathList = photoFragment.getPathList();
                if (pathList.size() > 0) {
                    //先要将图片上传到图片服务器
                    uploadPic();
                } else {
                    commit();
                }
                break;
            case R.id.btn_edit:
                editMode();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BirdApi.cancelRequestWithTag(TAG);
    }
}
