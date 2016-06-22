package com.pda.birdex.pda.api;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.utils.SafeProgressDialog;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.RotateLoading;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by chuming.zhuang on 2016/3/18.
 * 请求接口
 */
public class BirdApi {
    public static String SERVER_ADDRESS = "192.168.1.224";
    public static String PORT = "3000";//8002
    public static String BASE_URL = "http://" + SERVER_ADDRESS + ":" + PORT;//
    private static Dialog loadingDialog;

    public static void showLoading(Context mContext) {
        if (loadingDialog == null)
            loadingDialog = new SafeProgressDialog(mContext, R.style.semester_dialog);// 创建自定义样式dialog
//        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
        loadingDialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
        loadingDialog.setContentView(view);// 设置布局
        final RotateLoading loading = (RotateLoading) view.findViewById(R.id.rotateloading);
        loading.start();
        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                loading.stop();
            }
        });
        loadingDialog.show();
    }

    public static void hideLoading() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }


    /**
     * @param context
     * @param url
     * @param params
     * @param jsonHttpResponseHandler
     */
    public static void post(Context context, String url, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        MyApplication.ahc.post(context, BASE_URL + "/" + url, params, jsonHttpResponseHandler);
    }

    public static void post(Context context, String url, RequestParams params, TextHttpResponseHandler textHttpResponseHandler) {
        MyApplication.ahc.post(context, BASE_URL + "/" + url, params, textHttpResponseHandler);
    }

    public static void post(Context context, String url, Header[] headers, RequestParams params, String contentType, ResponseHandlerInterface responseHandler) {
        MyApplication.ahc.post(context, BASE_URL + "/" + url, params, responseHandler);
    }

    /**
     * 取消特定的tag请求
     */
    public static void cancelRequestWithTag(Object tag) {
        MyApplication.ahc.cancelRequestsByTAG(tag, true);
    }

    public static void cancelAllRequest() {
        MyApplication.ahc.cancelAllRequests(false);
    }

    /**
     * @param context
     * @param url
     * @param jsonHttpResponseHandler
     */
    private static void get(Context context, String url, JsonHttpResponseHandler jsonHttpResponseHandler) {
        MyApplication.ahc.get(context, BASE_URL + "/" + url, jsonHttpResponseHandler);
    }

    private static void get(Context context, String url, TextHttpResponseHandler textHttpResponseHandler) {
        MyApplication.ahc.get(context, BASE_URL + "/" + url, textHttpResponseHandler);
    }

    //查询揽收单结果
    public static void Check(Context context,String expressNo, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        getRequest(context, callBackInterface, "taking/check/" + expressNo, tag, showDialog);
    }

    /**
     * mContext 上下文对象
     * params请求参数
     * callBackInterface 请求成功回调
     * url，地址
     * tag,请求的标签
     * showDialog 是否显示等待框
     */
    public static void postRequest(final Context mContext, RequestParams params, final RequestCallBackInterface callBackInterface,
                                   String url, String tag, final boolean showDialog) {
        showLoading(mContext);
        JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (callBackInterface != null) {
                    if(response!=null) {
                        callBackInterface.successCallBack(response);
                    }else{
                        T.showShort(mContext,mContext.getString(R.string.successCallBack_error));
                    }
                }
                else {
                    T.showShort(mContext, mContext.getString(R.string.callBack_error));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                T.showShort(mContext, mContext.getString(R.string.request_error));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (showDialog)
                    hideLoading();
            }

        };
        jsonHttpResponseHandler.setTag(tag);
        post(mContext, url, params, jsonHttpResponseHandler);
    }


    // 上传图片
    public static void upLoadPic(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        post(context, "app_upload.php", params, jsonHttpResponseHandler);
    }

    // 上传 upc 图片 url
    public static void upLoadUpc(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
//        MyApplication.ahc.post(context, "http://192.168.1.215:8012/bs-product/productUpc/newByApp", params, jsonHttpResponseHandler);
//        MyApplication.ahc.post(context, "http://bs-product.apiv2.a.com/productUpc/newByApp", params, jsonHttpResponseHandler);
        MyApplication.ahc.post(context, "http://bs-product.apiv2.birdex.cn/productUpc/newByApp", params, jsonHttpResponseHandler);
    }

    /**
     * mContext 上下文对象
     * callBackInterface 请求成功回调
     * url，地址
     * tag,请求的标签
     * showDialog 是否显示等待框
     */
    public static void getRequest(final Context mContext, final RequestCallBackInterface callBackInterface,
                                  String url, String tag, final boolean showDialog) {
        showLoading(mContext);
        JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (callBackInterface != null) {
                    if(response!=null) {
                        callBackInterface.successCallBack(response);
                    }else{
                        T.showShort(mContext,mContext.getString(R.string.successCallBack_error));
                    }
                }
                else {
                    T.showShort(mContext, mContext.getString(R.string.callBack_error));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                T.showShort(mContext, mContext.getString(R.string.request_error));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (showDialog)
                    hideLoading();
            }

            @Override
            public void onStart() {
//                if(showDialog)
//                showLoading(mContext);
                super.onStart();
            }
        };
        jsonHttpResponseHandler.setTag(tag);
        get(mContext, url, jsonHttpResponseHandler);
    }
}
