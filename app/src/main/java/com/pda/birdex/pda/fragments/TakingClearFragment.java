package com.pda.birdex.pda.fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.BaseActivity;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.entity.ContainerInfo;
import com.pda.birdex.pda.entity.TakingOrder;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.response.TakingOrderNoInfoEntity;
import com.pda.birdex.pda.utils.GsonHelper;
import com.pda.birdex.pda.utils.SoftKeyboardUtil;
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
 * Created by chuming.zhuang on 2016/6/25.
 */
public class TakingClearFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "TakingClearFragment";

    @Bind(R.id.edt_taking_num)
    com.pda.birdex.pda.widget.ClearEditText edt_taking_num;

    @Bind(R.id.edt_box_size)
    com.pda.birdex.pda.widget.ClearEditText edt_box_size;

    // 揽收单号
    @Bind(R.id.tv_taking_num)
    TextView tv_taking_num;

    // 存储照片路径的 list
    private List<String> pathList = new ArrayList<String>();

    // 存放所有返回图片地址的 list
    private List<String> photoUrl = new ArrayList<>();


    private final static int COMPRESS_DOWN = 3;

    TakingOrder takingOrder;//位置1进来传来的实体
    TakingOrderNoInfoEntity orderNoInfoEntity;//位置2进来传来的实体
    ContainerInfo containerInfo;//位置2进来时传进来的item

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.fragment_taking_tool_clear_layout;
    }

    // 标记是从揽收、或者 揽收任务 跳转过来的
    // 1:揽收 2:揽收任务
    private String from;

    // 图片 fragment
    private PhotoFragment photoFragment;

    // 记录上传图片成功返回的 url条数
    private int sucCounts = 0;

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
//                                    String str = BirdApi.UPLOADIP + result;
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

        from = getActivity().getIntent().getExtras().getString("location_position");

        if (from.equals("1")) {
            // 从揽收进入的
            takingOrder = (TakingOrder) getActivity().getIntent().getExtras().get("takingOrder");
            if(takingOrder!=null) {
                tv_taking_num.setText(takingOrder.getBaseInfo().getTakingOrderNo());
            }
//            edt_taking_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if (!hasFocus) {
//                        getAreaMes(edt_taking_num.getText() + "");
//                    }
//                }
//            });
        } else {//打印数量
            // 从揽收任务进入的
            orderNoInfoEntity = (TakingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("orderNoInfoEntity");
            if(orderNoInfoEntity!=null) {
                containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
                tv_taking_num.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
            }
//            tv_area.setText(containerInfo.getArea());
        }

        edt_taking_num.requestFocus();

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
                if (TextUtils.isEmpty(edt_taking_num.getText())) {
                    T.showShort(getContext(), getString(R.string.taking_num_toast));
                    return;
                }
                if (TextUtils.isEmpty(edt_box_size.getText())) {
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
            String orderId = tv_taking_num.getText().toString(); //日志上报
            String tid = null;
//            RequestParams params = new RequestParams();
            if ("1".equals(from)) {
                tid = takingOrder.getBaseInfo().getTid();
                jsonObject.put("tid", tid);
                jsonObject.put("owner", takingOrder.getPerson().getCo());
            } else if ("2".equals(from)) {
                tid = orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid();
                jsonObject.put("tid",tid );
                jsonObject.put("owner", orderNoInfoEntity.getDetail().getBaseInfo().getPerson().getCo());
            }
//        params.put("takingOrderNo", tv_taking_num.getText() + "");
//        List containerList = new ArrayList();
//        containerList.add(edt_taking_container.getText() + "");
            jsonObject.put("containerNo", edt_taking_num.getText() + "");
            jsonObject.put("count", edt_box_size.getText() + "");

            String str = GsonHelper.createJsonString(photoUrl);
            JSONArray jsonArray = new JSONArray(str);
            jsonObject.put("photoUrl", jsonArray);

            //日志上报
            String ctNo = edt_taking_num.getText().toString();
            int count = Integer.parseInt(edt_box_size.getText().toString());
            MyApplication.loggingUpload.takeTakeClear(getActivity(), tag, orderId, tid, ctNo, count);

            BirdApi.takingSubmit(getActivity(), jsonObject, new RequestCallBackInterface() {

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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }


    @Override
    public ClearEditText getClearEditText() {
        return edt_taking_num;
    }


    @Override
    public void ClearEditTextCallBack(String code) {
        if (!this.isHidden()) {
            SoftKeyboardUtil.hideSoftKeyboard((BaseActivity) getActivity());
            if(edt_taking_num.hasFocus()){
                edt_box_size.requestFocus();
            }
        }
//            if ("1".equals(from)) {
//                // 说明是从揽收进入的 需要通过容器号 调用接口  获取区域信息
//                getAreaMes(code);
//            }
//        }
    }
}
