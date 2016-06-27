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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.adapter.PhotoGVAdapter;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.ClearEditText;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
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
public class CountPhotoActivity extends BarScanActivity implements View.OnClickListener{

    @Bind(R.id.vessel_et)
    com.pda.birdex.pda.widget.ClearEditText vessel_et;

    @Bind(R.id.upc_et)
    com.pda.birdex.pda.widget.ClearEditText upc_et;

    @Bind(R.id.titleView)
    com.pda.birdex.pda.widget.TitleView titleView;

    // 存放照片的 gridview
    @Bind(R.id.gv)
    com.pda.birdex.pda.widget.MyGridView gv;

    // 存储照片路径的 list
    private ArrayList<String> pathList = new ArrayList<String>();

    // 图片 path
    private String filePath;


    private final static int SCANNIN_GREQUEST_CODE = 1;
    private final static int PHOTO_GREQUEST_CODE = 2;
    private final static int COMPRESS_DOWN = 3;
    private final static int PHOTO_SHOW = 4;

    @Override
    public int getbarContentLayoutResId() {
        return R.layout.activity_count_photo;
    }

    private static final int PIC_COMPRESS = 1;
    private static final int UPC_COMPRESS = 2;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case PIC_COMPRESS:
                    String path = (String) msg.obj;
                    MyTask task = new MyTask();
                    task.execute(path);
                    break;
                case UPC_COMPRESS:
                    // 执行 upc 及 图片urls 上传
                    TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String szImei = TelephonyMgr.getDeviceId();

                    RequestParams upcParams = new RequestParams();
                    upcParams.put("openId", "birdex");
                    upcParams.put("userCode", szImei);
//                    upcParams.put("upc", mTextView.getText().toString());
                    upcParams.put("imgUrls", urls);
                    BirdApi.upLoadUpc(CountPhotoActivity.this, upcParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                if (response.getString("code").equals("0")) {
                                    T.showShort(MyApplication.getInstans(), "条形码上传成功");
                                    mProgress.dismiss();
                                    clearList();
                                } else {
                                    T.showShort(CountPhotoActivity.this, response.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            T.showShort(MyApplication.getInstans(), "条形码上传失败");
                            mProgress.dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            T.showShort(MyApplication.getInstans(), "条形码上传失败");
                            mProgress.dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            T.showShort(MyApplication.getInstans(), "条形码上传失败");
                            mProgress.dismiss();
                        }
                    });
                    break;
                case COMPRESS_DOWN:

                    String path1 = msg.obj.toString();

                    RequestParams myparams = new RequestParams();
                    File file = new File(path1);

                    try {
                        myparams.put("file", file, "image/jpeg");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        mProgress.dismiss();
                    }

                    BirdApi.uploadPic(CountPhotoActivity.this, myparams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                if (response.getString("code").equals("0")) {
                                    urls.add(response.getString("file"));
                                    progress++;
                                    mProgress.setProgress(progress);
                                    if (progress == pathList.size()) {
//                                    mProgress.dismiss();
                                        T.showShort(MyApplication.getInstans(), "图片上传成功");
                                        Message msg = Message.obtain();
                                        msg.what = UPC_COMPRESS;
                                        handler.sendMessage(msg);
                                    }
                                } else {
                                    T.showShort(CountPhotoActivity.this, response.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            T.showShort(MyApplication.getInstans(), "上传失败");
                            mProgress.dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            T.showShort(MyApplication.getInstans(), "上传失败");
                            mProgress.dismiss();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            T.showShort(MyApplication.getInstans(), "上传失败");
                            mProgress.dismiss();
                        }
                    });
                    break;
            }
        }
    };

    private void clearList() {
        // 删除掉已经上传的文件  先不做
//        for (int i = 0; i < pathList.size(); i++) {
//            File file = new File(pathList.get(i).toString());
//            if (file.exists()) {
//                file.delete();
//            }
//        }
        // 清空 list
        pathList.clear();
//        mTextView.setText("");
        PhotoGVAdapter adapter = new PhotoGVAdapter(getApplication(), pathList);
        gv.setAdapter(adapter);
    }


    @Override
    public void barInitializeContentViews() {
        titleView.setTitle(getString(R.string.photo));

        vessel_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                vessel_et.overrideOnFocusChange(hasFocus);
                if (hasFocus) {
                    setEdt_input(vessel_et);
                }
            }
        });
        upc_et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                upc_et.overrideOnFocusChange(hasFocus);
                if(hasFocus){
                    setEdt_input(upc_et);
                }
            }
        });


        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position < pathList.size()) {

                    Intent showIntent = new Intent();
                    Bundle b = new Bundle();
                    b.putString("path", pathList.get(position) + "");
                    b.putInt("position", position);
                    showIntent.putExtras(b);
                    showIntent.setClass(getApplicationContext(), PhotoShowActivity.class);
                    startActivityForResult(showIntent, PHOTO_SHOW);
                    overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                } else {
                    if (pathList.size() <= 10) {
                        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        filePath = getFileName();
                        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));

                        startActivityForResult(photoIntent, PHOTO_GREQUEST_CODE);
                    } else {
                        T.showShort(CountPhotoActivity.this, "照片不能超过10张");
                    }
                }
            }
        });

        PhotoGVAdapter adapter = new PhotoGVAdapter(getApplication(), pathList);
        gv.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PHOTO_GREQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
                        Log.i("TestFile",
                                "SD card is not avaiable/writeable right now.");
                        T.showLong(this, "SDCard读取失败，请重试");
                        return;
                    }
                    pathList.add(filePath);

                    PhotoGVAdapter adapter = new PhotoGVAdapter(getApplication(), pathList);
                    gv.setAdapter(adapter);
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
                PhotoGVAdapter adapter = new PhotoGVAdapter(getApplication(), pathList);
                gv.setAdapter(adapter);
                break;
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

        return fileName;
    }


    private Bitmap comp(String path) {

        Bitmap image = BitmapFactory.decodeFile(path);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 100) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        try {
            isBm.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;//压缩好比例大小后再进行质量压缩
    }

    // 将 bitmap 保存成图片
    public void saveBitmapFile(Bitmap bitmap, String imgpath) {
        File file = new File(imgpath);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.upload, R.id.gallery})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload:
                // 如果 upc 为空，照片 list 也为空， 不进行上传操作
                if (pathList.size() == 0) {
                    T.showShort(CountPhotoActivity.this, "条形码和照片不能同时为空");
                } else {
                    if (pathList.size() <= 10) {
                        // 进度条
                        progressDialog();
                        // 上传图片
                        uploadPic();
                    } else {
                        // 限制图片数量在 3-5 之间
                        T.showShort(CountPhotoActivity.this, "照片不能超过10张");
                    }
                }
                break;

            case R.id.gallery:
                // 打开相册 选择照片
                Intent intentG = new Intent();
                intentG.setType("image/*");
                intentG.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentG, 3);
                break;
        }
    }

    private List<String> urls = new ArrayList<>();

    // 上传图片
    private void uploadPic() {

        for (int i = 0; i < pathList.size(); i++) {

            Message message = Message.obtain();
            message.what = PIC_COMPRESS;
            message.obj = pathList.get(i);
            handler.sendMessage(message);

        }

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

    class MyTask extends AsyncTask<String, Integer, Void> {
        String path;

        @Override
        protected Void doInBackground(String... params) {
            path = params[0];

            saveBitmapFile(comp(path), path);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Message message = Message.obtain();
            message.what = COMPRESS_DOWN;
            message.obj = path;
            handler.sendMessage(message);
        }
    }

    @Override
    public ClearEditText getClearEditText() {
        return vessel_et;
    }

    @Override
    public void ClearEditTextCallBack(String code) {

    }
}
