package com.pda.birdex.pda.api;

import android.content.Context;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pda.birdex.pda.MyApplication;

import org.apache.http.Header;

/**
 * Created by chuming.zhuang on 2016/3/18.
 * 请求接口
 */
public class BirdApi {
    public static String SERVER_ADDRESS = "192.168.1.207";
    public static String PORT = "8089";//8002
    public static String BASE_URL = "http://" + SERVER_ADDRESS + ":" + PORT;//

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
     * @param params
     * @param jsonHttpResponseHandler
     */
    private static void get(Context context, String url, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        MyApplication.ahc.get(context, BASE_URL + "/" + url, params, jsonHttpResponseHandler);
    }

    private static void get(Context context, String url, RequestParams params, TextHttpResponseHandler textHttpResponseHandler) {
        MyApplication.ahc.get(context, BASE_URL + "/" + url, params, textHttpResponseHandler);
    }

    /**
     * 请求接口方法
     *
     * @param context
     * @param params
     * @param jsonHttpResponseHandler
     */
    public static void CheckUpdate(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        post(context, "UpdateCenter/CheckVersion", params, jsonHttpResponseHandler);
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

}
