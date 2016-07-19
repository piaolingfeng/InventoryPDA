package com.pda.birdex.pda.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.services.BluetoothService;
import com.pda.birdex.pda.utils.PreferenceUtils;
import com.pda.birdex.pda.widget.ClearEditText;
import com.pda.birdex.pda.widget.TitleView;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

/**
 * Created by chuming.zhuang on 2016/6/30.
 */
public abstract class BasePrintBarScanActivity extends BarScanActivity {
    // Debugging
    private static final String TAG = "BTPrinter";
    private static final boolean D = true;

    // Message types sent from the BluetoothService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // Name of the connected device
    private static String mConnectedDeviceName;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth takingAdapter
    private BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the services
    public static BluetoothService mService;

    private String btAddress = "";

    EventBus bus;
    private TitleView titleView;

    @Override
    public int getbarContentLayoutResId() {
        return getPrintContentLayoutResId();
    }

    @Override
    public void barInitializeContentViews() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        titleView = printTitleView();
//        title.setTitle(getString(R.string.print_title));

        // If the takingAdapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        bus = EventBus.getDefault();
        printInitializeContentViews();

        //先判断蓝牙是否连接，如果未连接，从sp文件中拿到上次成功连接的address 进行连接
        bluetoothConnect();
    }


    //先判断蓝牙是否连接，如果未连接，从sp文件中拿到上次成功连接的address 进行连接
    private void bluetoothConnect() {
        if (!isBlueToothHeadsetConnected()) {
            // 没有连接
            btAddress = PreferenceUtils.getPrefString(getApplication(), "btAddress", "");
            if (!TextUtils.isEmpty(btAddress)) {

                setupChat();

                BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBtAdapter.isDiscovering()) {
                    mBtAdapter.cancelDiscovery();
                }

                // Request discover from BluetoothAdapter
                mBtAdapter.startDiscovery();
                //执行连接操作
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(btAddress);
                // Attempt to connect to the device
                mService.connect(device);
            }
        }
    }

    public boolean checkBluetoothAddress(String address) {
        if (address == null || address.length() != 17) {
            return false;
        }
        for (int i = 0; i < 17; i++) {
            char c = address.charAt(i);
            switch (i % 3) {
                case 0:
                case 1:
                    if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F')) {
                        // hex character, OK
                        break;
                    }
                    return false;
                case 2:
                    if (c == ':') {
                        break;  // OK
                    }
                    return false;
            }
        }
        return true;
    }

    private boolean isBlueToothHeadsetConnected() {
        boolean retval = true;
        try {
            retval = BluetoothAdapter.getDefaultAdapter().getProfileConnectionState(android.bluetooth.BluetoothProfile.HEADSET)
                    != android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;

        } catch (Exception exc) {
            // nothing to do
        }
        return retval;
    }

    @SuppressLint("NewApi")
    @Override
    public void onStart() {
        super.onStart();
        if (D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the session
        } else {
//            if (mService == null) setupChat();
            setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if (D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mService.getState() == BluetoothService.STATE_NONE) {
                // Start the Bluetooth services
                mService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

//        mSendButton = (Button) findViewById(R.id.button_send);
//        mSendButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Send a message using content of the edit text widget
//                TextView fontGrayView = (TextView) findViewById(R.id.fontGrayscaleValue);
////                String message = view.getText().toString();
//                String message = getResources().getString(R.string.PrintContent);//先从本地获取
//                String fontGrayValue = fontGrayView.getText().toString();
//                fontGrayscaleSet(Integer.parseInt(fontGrayValue));
//                sendMessage(message);
//            }
//        });
//        // Initialize the print button with a listener that for click events
//        mPrintButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if(mPrintButton.getText().equals("连接"))
//                {
//                    // Launch the DeviceListActivity to see devices and do scan
//                    Intent serverIntent = new Intent(BasePrintBarScanActivity.this, DeviceListActivity.class);
//                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
//                }
//                else
//                {
//                    mService.stop();
//                }
//            }
//        });

        // Initialize the BluetoothService to perform bluetooth connections
        if (mService == null) {//让服务只作用一次
//            mService = (BluetoothService) SavaObjectUtil.readObject(this,"mService");
//            if(mService==null){
            mService = new BluetoothService(this, mHandler);
//            }
        } else {
            if (titleView != null) {
                mService.setmHandler(mHandler);//重新赋予mHandler地址，activity已经被销毁，handler被重新new过
                switch (mService.getState()) {
                    case BluetoothService.STATE_NONE:
                    case BluetoothService.STATE_LISTEN:
                        titleView.setSaveText(getString(R.string.title_not_connected));
                        break;
                    case BluetoothService.STATE_CONNECTING:
                        titleView.setSaveText(getString(R.string.title_connecting));
                        break;
                    case BluetoothService.STATE_CONNECTED:
                        titleView.setSaveText(getString(R.string.title_connected_to) + mConnectedDeviceName);
                        break;
                }
            }
//            if(titleView!=null)
//            titleView.setSaveText(getString(R.string.title_connected_to) + mConnectedDeviceName);
        }

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    @Override
    public synchronized void onPause() {
        super.onPause();
        if (D) Log.e(TAG, "- ON PAUSE -");
    }

    @Override
    public void onStop() {
        super.onStop();
        if (D) Log.e(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth services
//        if (mService != null) mService.stop();
        if (D) Log.e(TAG, "--- ON DESTROY ---");
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mService.getState() != BluetoothService.STATE_CONNECTED) {
            Toast.makeText(this, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothService to write
            byte[] send;
            try {
                send = new String(message.getBytes(), "UTF-8").getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                send = message.getBytes();
            }
            mService.write(send);
//
//            // Reset out string buffer to zero and clear the edit text field
//            mOutStringBuffer.setLength(0);
//            mOutEditText.setText(mOutStringBuffer);
        }
    }

    private String data = "";
    // The Handler that gets information back from the BluetoothService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
//                    SavaObjectUtil.saveObject(BasePrintBarScanActivity.this,"mService",mService);
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:
                            if (titleView != null)
                                titleView.setSaveText(getString(R.string.title_connected_to) + mConnectedDeviceName);
//                            mTitle.append(mConnectedDeviceName);
//                            mPrintButton.setText(R.string.Print1);
                            PreferenceUtils.setPrefString(getApplication(), "btAddress", btAddress);
                            break;
                        case BluetoothService.STATE_CONNECTING:
//                            mTitle.setText(R.string.title_connecting);
                            if (titleView != null)
                                titleView.setSaveText(getString(R.string.title_connecting));
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
//                            mTitle.setText(R.string.title_not_connected);
                            if (titleView != null)
                                titleView.setSaveText(getString(R.string.title_not_connected));
//                            mPrintButton.setText(R.string.Print);
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    //byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    //String writeMessage = new String(writeBuf);
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    data += readMessage.trim();
                    byte bt0 = '\r';
                    byte bt1 = '\n';
                    if (msg.arg1 > 1 && readBuf[msg.arg1 - 2] == bt0 && readBuf[msg.arg1 - 1] == bt1) {
//                        mOutEditText.setText(data);
                        data = "";

                    }
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @SuppressLint("NewApi")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    btAddress = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    boolean flag = checkBluetoothAddress(btAddress);
                    System.out.print(flag);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(btAddress);
                    // Attempt to connect to the device
                    mService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occured
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan:
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                return true;
            case R.id.disconnect:
                // disconnect
                mService.stop();
                return true;
        }
        return false;
    }

    //设置布局
    public abstract int getPrintContentLayoutResId();

    public abstract void printInitializeContentViews();

    //设置布局

    public abstract TitleView printTitleView();

    //获取界面的clearedit
    public abstract ClearEditText getClearEditText();

    //扫描回调接口
    public abstract void ClearEditTextCallBack(String code);
}
