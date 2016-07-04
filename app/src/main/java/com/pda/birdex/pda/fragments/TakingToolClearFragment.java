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
public class TakingToolClearFragment extends BarScanBaseFragment implements View.OnClickListener {
    String tag = "TakingToolClearFragment";

    @Bind(R.id.edt_taking_num)
    com.pda.birdex.pda.widget.ClearEditText edt_taking_num;

    @Bind(R.id.edt_box_size)
    com.pda.birdex.pda.widget.ClearEditText edt_box_size;

    @Bind(R.id.gv)
    com.pda.birdex.pda.widget.MyGridView gv;

    // 揽收单号
    @Bind(R.id.tv_taking_num)
    TextView tv_taking_num;

    //区域
    @Bind(R.id.tv_area)
    TextView tv_area;


    // 存储照片路径的 list
    private ArrayList<String> pathList = new ArrayList<String>();

    // 图片 path
    private String filePath;

    // 存放所有返回图片地址的 list
    private List<String> photoUrl = new ArrayList<>();


    private final static int SCANNIN_GREQUEST_CODE = 1;
    private final static int PHOTO_GREQUEST_CODE = 2;
    private final static int COMPRESS_DOWN = 3;
    private final static int PHOTO_SHOW = 4;

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

    // 记录上传图片成功返回的 url条数
    private int sucCounts = 0;

    private static final int PIC_COMPRESS = 1;
    // 压缩完图片后 重新刷新
    private static final int COMPRESS_REFLASH = 1001;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case PIC_COMPRESS:
//                    String path = (String) msg.obj;
                    MyTask task = new MyTask();
                    task.execute();
                    break;

                case COMPRESS_REFLASH:
                    // 压缩完图片后 重新刷新
                    PhotoGVAdapter adapter = new PhotoGVAdapter(getContext(), pathList);
                    gv.setAdapter(adapter);
                    break;

                case COMPRESS_DOWN:

                    String path1 = msg.obj.toString();

                    RequestParams myparams = new RequestParams();
                    File file = new File(path1);
//                    File file = new File("/storage/sdcard0/logs/recovery/20150430_104402.log");

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
            tv_taking_num.setText(takingOrder.getBaseInfo().getTakingOrderNo());
            edt_taking_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        getAreaMes(edt_taking_num.getText() + "");
                    }
                }
            });
        } else {//打印数量
            // 从揽收任务进入的
            orderNoInfoEntity = (TakingOrderNoInfoEntity) getActivity().getIntent().getExtras().get("orderNoInfoEntity");
            containerInfo = (ContainerInfo) getActivity().getIntent().getExtras().get("containerInfo");
            tv_taking_num.setText(orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTakingOrderNo());
            tv_area.setText(containerInfo.getArea());
        }

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position < pathList.size()) {

                    Intent showIntent = new Intent();
                    Bundle b = new Bundle();
//                b.putParcelable("bitmap",bitmapList.get(position));
                    b.putString("path", pathList.get(position) + "");
                    b.putInt("position", position);
                    showIntent.putExtras(b);
                    showIntent.setClass(getActivity().getApplicationContext(), PhotoShowActivity.class);
                    startActivityForResult(showIntent, PHOTO_SHOW);
                    getActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                } else {
                    if (pathList.size() <= 10) {
                        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        filePath = getFileName();
                        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));

                        startActivityForResult(photoIntent, PHOTO_GREQUEST_CODE);
                    } else {
                        T.showShort(getContext(), getString(R.string.taking_upload_photo_count));
                    }
                }
            }
        });

        PhotoGVAdapter adapter = new PhotoGVAdapter(getContext(), pathList);
        gv.setAdapter(adapter);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        T.showLong(getContext(), "SDCard读取失败，请重试");
                        return;
                    }
                    pathList.add(filePath);
                    MyTask1 myTask1 = new MyTask1();
                    myTask1.execute(filePath);
//                    PhotoGVAdapter adapter = new PhotoGVAdapter(getContext(), pathList);
//                    gv.setAdapter(adapter);
                }
                break;

            case PHOTO_SHOW:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    String newpath = bundle.getString("newpath");
                    int newposition = bundle.getInt("position");
                    pathList.remove(newposition);
                    pathList.add(newposition, newpath);
                }
                PhotoGVAdapter adapter = new PhotoGVAdapter(getContext(), pathList);
                gv.setAdapter(adapter);
                break;
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

//            RequestParams params = new RequestParams();
            if ("1".equals(from)) {
                jsonObject.put("tid", takingOrder.getBaseInfo().getTid());
                jsonObject.put("owner", takingOrder.getPerson().getCo());
            } else if ("2".equals(from)) {
                jsonObject.put("tid", orderNoInfoEntity.getDetail().getBaseInfo().getBaseInfo().getTid());
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

//        for (int i = 0; i < pathList.size(); i++) {
//
//            Message message = Message.obtain();
//            message.what = PIC_COMPRESS;
//            message.obj = pathList.get(i);
//            handler.sendMessage(message);
//
//        }

        Message message = Message.obtain();
        message.what = PIC_COMPRESS;
//        message.obj = pathList.get(i);
        handler.sendMessage(message);

    }


    class MyTask extends AsyncTask<String, Integer, Void> {
        String path;

        @Override
        protected Void doInBackground(String... params) {

            photoUrl.clear();
            sucCounts = 0;

//            path = params[0];
            for (int i = 0; i < pathList.size(); i++) {
                path = pathList.get(i);
//                Bitmap bt = comp(path);
//                saveBitmapFile(bt, path);

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
//            Message message = Message.obtain();
//            message.what = COMPRESS_DOWN;
//            message.obj = path;
//            handler.sendMessage(message);
        }
    }


    // 压缩图片的 Task
    class MyTask1 extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String picPath = params[0];
//            Bitmap bt = comp(picPath);
//            saveBitmapFile(bt, picPath);
            compress(picPath);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Message msg = Message.obtain();
            msg.what = COMPRESS_REFLASH;
            handler.sendMessage(msg);
        }
    }

    private DisplayMetrics dm;

    public void compress(String srcPath) {
        dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        float hh = dm.heightPixels;
        float ww = dm.widthPixels;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size = 0;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }
        opts.inSampleSize = size;
        bitmap = BitmapFactory.decodeFile(srcPath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        System.out.println(baos.toByteArray().length);
        while (baos.toByteArray().length > 100 * 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 20;
            System.out.println(baos.toByteArray().length);
        }
        try {
//            baos.writeTo(new FileOutputStream("/mnt/sdcard/Servyou/photo/buffer/22.jpg"));
            baos.writeTo(new FileOutputStream(srcPath));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 生成文件路径和文件名
     *
     * @return
     */
    private String getFileName() {
        String saveDir = Environment.getExternalStorageDirectory() + "/myPic";
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdirs(); // 创建文件夹
        }
        //用日期作为文件名，确保唯一性
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String fileName = saveDir + "/" + formatter.format(date) + ".jpg";

//        File file = new File(fileName);
//        if (!file.exists()) {
//            file.mkdirs();
//        }
        return fileName;
    }

    @Override
    public ClearEditText getClearEditText() {
        return edt_taking_num;
    }


    // 通过用户编码+容器号 获取区域信息
    private void getAreaMes(String code) {
        String owner = takingOrder.getPerson().getCo();
        if (!(TextUtils.isEmpty(code) || TextUtils.isEmpty(owner))) {
            // CT-160629000001 测试
            BirdApi.getArea(getContext(), owner + "/" + code, new RequestCallBackInterface() {

                @Override
                public void successCallBack(JSONObject object) {
                    try {
                        if ("success".equals(object.getString("result"))) {
                            tv_area.setText(object.getString("area"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void errorCallBack(JSONObject object) {

                }
            }, tag, true);
        }
    }


    @Override
    public void ClearEditTextCallBack(String code) {
        if (this.isVisible()) {
            if ("1".equals(from)) {
                // 说明是从揽收进入的 需要通过容器号 调用接口  获取区域信息
                getAreaMes(code);
            }
        }
    }
}
