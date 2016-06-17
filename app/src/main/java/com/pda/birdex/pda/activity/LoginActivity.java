package com.pda.birdex.pda.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.loopj.android.http.RequestParams;
import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.api.BirdApi;
import com.pda.birdex.pda.utils.PreferenceUtils;
import com.pda.birdex.pda.utils.T;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by hyj on 2016/3/25.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    // 用户名
    @Bind(R.id.username_edit)
    EditText username;

    // 密码
    @Bind(R.id.password_edit)
    EditText password;

    // 记住密码
    @Bind(R.id.remember_cb)
    CheckBox remember;

    // 登录按钮
    @Bind(R.id.login_bt)
    Button login;

    // 设备信息
    public String device_info = "";


    public static String description;

    @Override
    public int getContentLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    public void initializeContentViews() {
//        initSystemBar(R.color.transparent);
        // 开启缓存 service
//        Intent service = new Intent(this, CacheService.class);
//        startService(service);

        initData();
    }


    // 获取设备信息
    private String getDevice_info() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();
        sb.append("\nDeviceModel = " + android.os.Build.MODEL);
        sb.append("\nDeviceVERSION_RELEASE = " + android.os.Build.VERSION.RELEASE);
        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
        sb.append("\nLine1Number = " + tm.getLine1Number());
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
        sb.append("\nNetworkType = " + tm.getNetworkType());
        sb.append("\nPhoneType = " + tm.getPhoneType());
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
        sb.append("\nSimOperator = " + tm.getSimOperator());
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
        sb.append("\nSimState = " + tm.getSimState());
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
        Log.e("info", sb.toString());
        return sb.toString();
    }


    public static final int CALL_PHONE_REQUEST_CODE = 1;

    private void initData() {

        // 确认是否勾选了 记住密码
        boolean ischecked = PreferenceUtils.getPrefBoolean(this, "remember", false);
        if (ischecked) {
            // 选中了 记住密码
            String usernamestr = PreferenceUtils.getPrefString(this, "username", "");
            String passwordstr = PreferenceUtils.getPrefString(this, "password", "");
            // 设置进去
            username.setText(usernamestr);
            password.setText(passwordstr);
        }
        remember.setChecked(ischecked);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请CALL_PHONE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE_REQUEST_CODE);
        } else {
            device_info = getDevice_info();
        }

    }


    // 6.0 权限控制
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PHONE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted 用户允许
                device_info = getDevice_info();
            } else {
                // Permission Denied
            }
        }
    }

    @Override
    protected void onDestroy() {
        BirdApi.cancelRequestWithTag(TAG);
        super.onDestroy();
    }

    // 执行登录操作
    private void login() {
//        showLoading();
        spEdit();
        RequestParams params = new RequestParams();
        params.put("account", username.getText().toString());
        params.put("password", password.getText().toString());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

//        BirdApi.login(MyApplication.getInstans(), params, handler);
    }


    private void spEdit() {
        if (remember.isChecked()) {
            // 选中了， 执行保存操作
            PreferenceUtils.setPrefString(this, "username", username.getText().toString());
            PreferenceUtils.setPrefString(this, "password", password.getText().toString());
            PreferenceUtils.setPrefBoolean(this, "remember", remember.isChecked());
        } else {
            // 如果是取消  就全部设置为空
            PreferenceUtils.setPrefString(this, "username", "");
            PreferenceUtils.setPrefString(this, "password", "");
            PreferenceUtils.setPrefBoolean(this, "remember", false);
        }
    }


    @OnClick({R.id.remember_cb, R.id.login_bt})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击记住密码
//            case R.id.remember_cb:
//                spEdit();
//                break;

            // 点击登录按钮
            case R.id.login_bt:
                // 先检查帐号密码不能为空
                if (TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(password.getText())) {
                    T.showShort(MyApplication.getInstans(), getString(R.string.notempty));
                    login();
                } else {
                    login();
                }
                break;
        }
    }
}
