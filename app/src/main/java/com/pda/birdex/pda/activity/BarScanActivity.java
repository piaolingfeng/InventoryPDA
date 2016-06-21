package com.pda.birdex.pda.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;

import com.pda.birdex.pda.widget.ClearEditText;

/**
 * Created by chuming.zhuang on 2016/6/17.
 */
public abstract class BarScanActivity extends BaseActivity {

    private final static String SCAN_ACTION = "urovo.rcv.message";//扫描结束action

    private Vibrator mVibrator;
    private ScanManager mScanManager;
    private SoundPool soundpool = null;
    private int soundid;
    private String barcodeStr;
    private boolean isScaning = false;

    private ClearEditText edt_input;

    public void setEdt_input(ClearEditText edt_input) {
        this.edt_input = edt_input;
    }

    @Override
    public int getContentLayoutResId() {
        return getbarContentLayoutResId();
    }

    @Override
    public void initializeContentViews() {
        barInitializeContentViews();
        edt_input = getClearEditText();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setupView();
    }

    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            isScaning = false;
            soundpool.play(soundid, 1, 1, 0, 0, 1);
            edt_input.setText("");
            mVibrator.vibrate(100);

            byte[] barcode = intent.getByteArrayExtra("barocode");
            //byte[] barcode = intent.getByteArrayExtra("barcode");
            int barocodelen = intent.getIntExtra("length", 0);
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            android.util.Log.i("debug", "----codetype--" + temp);
            barcodeStr = new String(barcode, 0, barocodelen);

            edt_input.setText(barcodeStr);

        }

    };

    private void initScan() {
        // TODO Auto-generated method stub
        mScanManager = new ScanManager();
        mScanManager.openScanner();

        mScanManager.switchOutputMode( 0);
        soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
        soundid = soundpool.load("/etc/Scan_new.ogg", 1);
    }

    private void setupView() {
        // TODO Auto-generated method stub
//        btn = (Button) findViewById(R.id.manager);
//        btn.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                if(mScanManager.getTriggerMode() != Triggering.CONTINUOUS)
//                    mScanManager.setTriggerMode(Triggering.CONTINUOUS);
//            }
//        });

//        mScan = (Button) findViewById(R.id.scan);
//        mScan.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                //if(type == 3)
//                mScanManager.stopDecode();
//                isScaning = true;
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//                mScanManager.startDecode();
//            }
//        });

//        mClose = (Button) findViewById(R.id.close);
//        mClose.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                // if(isScaning) {
//                //  isScaning = false;
//                mScanManager.stopDecode();
//                //}
//            }
//        });

        //btn.setVisibility(View.GONE);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if(mScanManager != null) {
            mScanManager.stopDecode();
            isScaning = false;
        }
        unregisterReceiver(mScanReceiver);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initScan();
        edt_input.setText("");
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
    }

    //设置布局
    public abstract int getbarContentLayoutResId();

    public abstract void barInitializeContentViews();

    public abstract ClearEditText getClearEditText();
}
