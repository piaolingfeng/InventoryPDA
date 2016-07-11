package com.pda.birdex.pda.fragments;

import android.app.Activity;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.PhotoShowActivity;
import com.pda.birdex.pda.adapter.PhotoGVAdapter;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.MyGridView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * Created by hyj on 2016/7/8.
 */
public class PhotoFragment extends BaseFragment {

    @Bind(R.id.photview_ll_gv)
    MyGridView gv;

    @Bind(R.id.photview_exception_cb)
    CheckBox exception_cb;


    // 存储照片路径的 list
    private ArrayList<String> pathList = new ArrayList<String>();

    // 图片 path
    private String filePath;

    // 存放所有返回图片地址的 list
    private List<String> photoUrl = new ArrayList<>();

    // 是否要显示 标记异常
    private boolean isException = true;


    private final static int PHOTO_GREQUEST_CODE = 2;
    private final static int PHOTO_SHOW = 4;


    // 压缩完图片后 重新刷新
    private static final int COMPRESS_REFLASH = 1001;

    public PhotoFragment() {

    }

    public PhotoFragment(boolean flag) {
        isException = flag;
    }

    @Override
    protected void key(int keyCode, KeyEvent event) {

    }

    @Override
    public int getContentLayoutResId() {
        return R.layout.view_photoview;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isException) {
            exception_cb.setVisibility(View.VISIBLE);
        } else {
            exception_cb.setVisibility(View.GONE);
        }
    }

    @Override
    public void initializeContentViews() {
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position < pathList.size()) {

                    Intent showIntent = new Intent();
                    Bundle b = new Bundle();
                    b.putString("path", pathList.get(position) + "");
                    b.putInt("position", position);
                    showIntent.putExtras(b);
                    showIntent.setClass(getContext(), PhotoShowActivity.class);
                    startActivityForResult(showIntent, PHOTO_SHOW);
                    getActivity().overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
                } else {
                    if (pathList.size() <= 10) {
                        Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        filePath = getFileName();
                        photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));

                        startActivityForResult(photoIntent, PHOTO_GREQUEST_CODE);
                    } else {
                        T.showShort(getContext(), getString(R.string.photo_most));
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
                        T.showLong(getContext(), getString(R.string.sdcard_fail));
                        return;
                    }
                    pathList.add(filePath);
                    MyTask1 myTask1 = new MyTask1();
                    myTask1.execute(filePath);
//                    PhotoGVAdapter takingAdapter = new PhotoGVAdapter(getApplication(), pathList);
//                    gv.setAdapter(takingAdapter);
                }
                break;

            case PHOTO_SHOW:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
//                    String newpath = bundle.getString("newpath");
                    int newposition = bundle.getInt("position");
                    pathList.remove(newposition);
//                    pathList.add(newposition, newpath);
                }
                PhotoGVAdapter adapter = new PhotoGVAdapter(getContext(), pathList);
                gv.setAdapter(adapter);
                break;
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


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case COMPRESS_REFLASH:
                    // 压缩完图片后 重新刷新
                    PhotoGVAdapter adapter = new PhotoGVAdapter(getContext(), pathList);
                    gv.setAdapter(adapter);
                    break;

            }
        }
    };

    // 将图片地址返回回去
    public List<String> getPathList() {
        return pathList;
    }

    // 是否选中了 标记异常
    public boolean isChecked() {
        return exception_cb.isChecked();
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

    @Override
    protected void lazyLoad() {

    }
}
