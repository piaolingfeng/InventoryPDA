package com.pda.birdex.pda.api;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pda.birdex.pda.MyApplication;
import com.pda.birdex.pda.R;
import com.pda.birdex.pda.interfaces.RequestCallBackInterface;
import com.pda.birdex.pda.utils.PreferenceUtils;
import com.pda.birdex.pda.utils.SafeProgressDialog;
import com.pda.birdex.pda.utils.T;
import com.pda.birdex.pda.widget.RotateLoading;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by chuming.zhuang on 2016/3/18.
 * 请求接口
 */
public class BirdApi {
    public static String SERVER_ADDRESS = "192.168.1.224";
    public static String PORT = "3000";//8002
    public static String BASE_URL = "http://" + SERVER_ADDRESS + ":" + PORT;//
    public static String Logging_BASE_URL = "http://192.168.1.222:3020/api/v1/oplog";
    private static Dialog loadingDialog;

    public static AsyncHttpClient ahc;

    public static void showLoading(Context mContext) {
//        if (loadingDialog == null)
        if (loadingDialog != null) {
            if (loadingDialog.getContext() != mContext) {
                loadingDialog = new SafeProgressDialog(mContext, R.style.semester_dialog);// 创建自定义样式dialog
            }
        } else {
            loadingDialog = new SafeProgressDialog(mContext, R.style.semester_dialog);// 创建自定义样式dialog
        }
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

    public static void jsonPost(Context context, String url, HttpEntity entity, ResponseHandlerInterface responseHandlerInterface) {
        MyApplication.ahc.post(context, BASE_URL + "/" + url, entity, "application/json", responseHandlerInterface);
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
    public static void Check(Context context, String expressNo, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        getRequest(context, callBackInterface, "taking/check/" + expressNo, tag, showDialog);
    }

    //揽收单详情
    public static void takingOrderNoInfo(Context context, String orderNo, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        getRequest(context, callBackInterface, "taking/info/" + orderNo, tag, showDialog);
    }

    //获取所有商家
    public static void getAllMerchant(Context context, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        getRequest(context, callBackInterface, "merchant", tag, showDialog);
    }

    //查询商家揽收单列表
    public static void getMerchant(Context context, String params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        getRequest(context, callBackInterface, "taking/list/" + params, tag, showDialog);
    }

    //统计某商家某类型订单的数量,merchant=null listType=null时统计揽收任务总数,merchant=all listType=all时统计所有商家的所有类型订单数以List 返回，merchant=meitun listType=taking时统计美囤的揽收完成数
    public static void getTakingListCountMerchant(Context context, String params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        getRequest(context, callBackInterface, "taking/list/count/" + params, tag, showDialog);
    }

    //合并结果
//    public static void postTakingOrderNum(Context context, RequestParams params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
//        postRequest(context, params, callBackInterface, "taking/merge", tag, showDialog);
//    }
    //合并结果
    public static void postTakingOrderNum(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "taking/merge", tag, showDialog);
    }

    //创建无预报揽收
//    public static void postTakingCreat(Context context, RequestParams params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
//        postRequest(context, params, callBackInterface, "taking/create", tag, showDialog);
//    }
    //创建无预报揽收
    public static void postTakingCreat(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "taking/create", tag, showDialog);
    }

    //打印
//    public static void postTakingCodePrint(Context context, RequestParams params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
//        postRequest(context, params, callBackInterface, "code/print", tag, showDialog);
//    }
    //揽收：打印
    public static void postTakingCodePrint(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "code/print", tag, showDialog);
    }

    //揽收：打印相同
    public static void postTakingCodeSamePrint(Context context, String containerNo, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        postRequest(context, new RequestParams(), callBackInterface, "code/printSame/" + containerNo, tag, showDialog);
    }

    //揽收：打印新的
    public static void postTakingCodeNewPrint(Context context, String containerNo, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        postRequest(context, new RequestParams(), callBackInterface, "code/printNew/" + containerNo, tag, showDialog);
    }

    //登录
    public static void login(Context context, String params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        getRequest(context, callBackInterface, "user/login/" + params, tag, showDialog);
    }

    //通过容器号获取区域信息
    public static void getArea(Context context, String params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        getRequest(context, callBackInterface, "code/info/" + params, tag, showDialog);
    }

    // 揽收：绑定区域
//    public static void takingBind(Context context, RequestParams params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
//        postRequest(context, params, callBackInterface, "code/bindArea", tag, showDialog);
//    }
    // 揽收：绑定区域
    public static void takingBind(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "code/bindArea", tag, showDialog);
    }

    // 揽收：打印揽收单
//    public static void takingPrint(Context context, RequestParams params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
//        postRequest(context, params, callBackInterface, "taking/code/print", tag, showDialog);
//    }

    // 揽收：揽收清点提交
//    public static void takingSubmit(Context context,RequestParams params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
//        postRequest(context, params, callBackInterface, "taking/submit", tag, showDialog);
//    }
    // 揽收：揽收清点提交
    public static void takingSubmit(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "taking/submit", tag, showDialog);
    }

    // 揽收：绑单提交
//    public static void takingBindorderSubmit(Context context,RequestParams params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
//        postRequest(context, params, callBackInterface, "code/takeBindOrder", tag, showDialog);
//    }

    // 揽收：绑单提交
    public static void jsonTakingBindorderSubmit(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "code/takeBindOrder", tag, showDialog);
    }

    // 揽收：收货页面的绑定揽收单，绑单提交
    public static void jsonTakingBindorderBatSubmit(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "code/bindOrderBat", tag, showDialog);
    }
    //拍照上传地址
    public static final String UPLOADIP = "http://192.168.1.225:4869/";

    // 上传图片
    public static void uploadPic(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
        if (ahc == null) {
            ahc = new AsyncHttpClient();//获取网络连接超时
            ahc.setTimeout(8 * 1000);//设置30秒超时
            ahc.setConnectTimeout(4 * 1000);//设置30秒超时
            ahc.setMaxConnections(5);
        }
//        ahc.addHeader("Content-Type","jpeg");
        ahc.post(context, UPLOADIP + "upload", params, jsonHttpResponseHandler);
    }

    // 上传日志post
    public static AsyncHttpClient asyncHttpClient=null;
    private static void uploadLogging(Context context, String url, HttpEntity entity, ResponseHandlerInterface responseHandlerInterface) {
        if (asyncHttpClient == null) {
            asyncHttpClient = new AsyncHttpClient();//获取网络连接超时
            asyncHttpClient.setTimeout(8 * 1000);//设置30秒超时
            asyncHttpClient.setConnectTimeout(4 * 1000);//设置30秒超时
            asyncHttpClient.setMaxConnections(5);
            asyncHttpClient.addHeader("X-Access-Token", "dsssss");
            asyncHttpClient.addHeader("X-User-Id", PreferenceUtils.getPrefString(context, "userId", ""));
        }
        asyncHttpClient.post(context, url, entity, "application/json", responseHandlerInterface);
    }
    //    // 提交上传图片
//    public static void uploadPicSubmit(Context context, RequestParams params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
//        postRequest(context, params, callBackInterface, "photo", tag, showDialog);
//    }
    // 揽收-提交上传图片
    public static void uploadPicSubmit(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "photo", tag, showDialog);
    }






    /**
     *  清点
     * @param context
     * @param jsonObject
     * @param callBackInterface
     * @param tag
     * @param showDialog
     */
    // 清点：绑单提交
    public static void jsonCountBindorderSubmit(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "counting/code/bindOrderBat", tag, showDialog);
    }

    // 清点-提交上传图片
    public static void countingUploadPicSubmit(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "counting/photo", tag, showDialog);
    }

    // 清点：清点提交
    public static void countSubmit(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "counting/submit", tag, showDialog);
    }

    //清点：打印
    public static void postCountingCodePrint(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "counting/code/print", tag, showDialog);
    }

    //清点：打印相同
    public static void postCountingCodeSamePrint(Context context, String containerNo, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        postRequest(context, new RequestParams(), callBackInterface, "code/printSame/" + containerNo, tag, showDialog);
    }

    //清点：打印新的
    public static void postCountingCodeNewPrint(Context context, String containerNo, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        postRequest(context, new RequestParams(), callBackInterface, "code/printNew/" + containerNo, tag, showDialog);
    }

    //清点：追踪
    public static void jsonCountTrack(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "counting/track", tag, showDialog);
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
        if (showDialog)
            showLoading(mContext);
        JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (callBackInterface != null) {
                    if (response != null) {
                        try {
                            if ("success".equals(response.getString("result"))) {
                                callBackInterface.successCallBack(response);
                            } else {
                                T.showShort(mContext, response.getString("errMsg"));
                                callBackInterface.errorCallBack(response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        T.showShort(mContext, mContext.getString(R.string.successCallBack_error));
                    }
                } else {
                    T.showShort(mContext, mContext.getString(R.string.callBack_error));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (MyApplication.ahc != null) {
                    MyApplication.ahc.addHeader("x-access-token", PreferenceUtils.getPrefString(mContext, "token", ""));
                }
                switch (statusCode){
                    case 401:
                        T.showShort(mContext,mContext.getString(R.string.request401));
                        break;
                    case 404:
                        T.showShort(mContext,mContext.getString(R.string.request404));
                        break;
                }
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

    public static void jsonPostLoggingRequest(final Context mContext, JSONObject jsonObject, final RequestCallBackInterface callBackInterface,
                                        String tag, final boolean showDialog) {
        if (showDialog)
            showLoading(mContext);
        JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (callBackInterface != null) {
                    if (response != null) {
                        try {
                            if ("success".equals(response.getString("result"))) {
                                callBackInterface.successCallBack(response);
                            } else {
                                T.showShort(mContext, response.getString("errMsg"));
                                callBackInterface.errorCallBack(response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        T.showShort(mContext, mContext.getString(R.string.successCallBack_error));
                    }
                } else {
                    T.showShort(mContext, mContext.getString(R.string.callBack_error));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                switch (statusCode){
                    case 401:
                        T.showShort(mContext,mContext.getString(R.string.request401));
                        break;
                    case 404:
                        T.showShort(mContext,mContext.getString(R.string.request404));
                        break;
                }
//                callBackInterface.errorCallBack(errorResponse);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (showDialog)
                    hideLoading();
            }

        };
        jsonHttpResponseHandler.setTag(tag);
        try {
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            uploadLogging(mContext, Logging_BASE_URL, stringEntity, jsonHttpResponseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        post(mContext, url, params, jsonHttpResponseHandler);
    }



    public static void jsonPostRequest(final Context mContext, JSONObject jsonObject, final RequestCallBackInterface callBackInterface,
                                       String url, String tag, final boolean showDialog) {
        if (showDialog)
            showLoading(mContext);
        JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (callBackInterface != null) {
                    if (response != null) {
                        try {
                            if ("success".equals(response.getString("result"))) {
                                callBackInterface.successCallBack(response);
                            } else {
//                                T.showShort(mContext, response.getString("errMsg"));
                                callBackInterface.errorCallBack(response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        T.showShort(mContext, mContext.getString(R.string.successCallBack_error));
                    }
                } else {
                    T.showShort(mContext, mContext.getString(R.string.callBack_error));
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                callBackInterface.errorCallBack(errorResponse);
                switch (statusCode){
                    case 401:
                        T.showShort(mContext,mContext.getString(R.string.request401));
                        break;
                    case 404:
                        T.showShort(mContext,mContext.getString(R.string.request404));
                        break;
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (showDialog)
                    hideLoading();
            }

        };
        jsonHttpResponseHandler.setTag(tag);
        try {
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            jsonPost(mContext, url, stringEntity, jsonHttpResponseHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        post(mContext, url, params, jsonHttpResponseHandler);
    }


    // 上传 upc 图片 url
    public static void upLoadUpc(Context context, RequestParams params, JsonHttpResponseHandler jsonHttpResponseHandler) {
//        MyApplication.ahc.post(context, "http://192.168.1.215:8012/bs-product/productUpc/newByApp", params, jsonHttpResponseHandler);
//        MyApplication.ahc.post(context, "http://bs-product.apiv2.a.com/productUpc/newByApp", params, jsonHttpResponseHandler);
        MyApplication.ahc.post(context, "http://bs-product.apiv2.birdex.cn/productUpc/newByApp", params, jsonHttpResponseHandler);
    }

    //清点接口
    //统计某商家某类型订单的数量,merchant=all listType=unCounting时统计待清点任务总数,
    // merchant=each listType=unCounting时统计每一个商家的未清点订单数以List 返回，
    // merchant=meitun listType=unTaking时统计美囤的未清点数
    public static void getCountingListCountMerchant(Context context, String params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        getRequest(context, callBackInterface, "counting/list/count/" + params, tag, showDialog);
    }

    //查询商家揽收单列表
    public static void getCountingMerchant(Context context, String params, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        getRequest(context, callBackInterface, "counting/list/" + params, tag, showDialog);
    }

    //清点单详情
    public static void getCountingOrderNoInfo(Context context, String orderNo, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        getRequest(context, callBackInterface, "counting/info/" + orderNo, tag, showDialog);
    }
    //清点合并结果
    public static void postCountingOrderNum(Context context, JSONObject jsonObject, RequestCallBackInterface callBackInterface, String tag, boolean showDialog) {
        jsonPostRequest(context, jsonObject, callBackInterface, "counting/merge", tag, showDialog);
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
        if (showDialog)
            showLoading(mContext);
        String encodedUR = "";
        try {
            String query = URLEncoder.encode(url, "utf-8");
            encodedUR = query;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if (showDialog)
                    hideLoading();
                super.onSuccess(statusCode, headers, response);
                if (callBackInterface != null) {
                    if (response != null) {
                        try {
                            if ("success".equals(response.getString("result"))) {
                                callBackInterface.successCallBack(response);
                            } else {
                                try {

                                    T.showShort(mContext, response.getString("errMsg"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                callBackInterface.errorCallBack(response);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        T.showShort(mContext, mContext.getString(R.string.successCallBack_error));
                    }
                } else {
                    T.showShort(mContext, mContext.getString(R.string.callBack_error));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (showDialog)
                    hideLoading();
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (MyApplication.ahc != null) {
                    MyApplication.ahc.addHeader("x-access-token", PreferenceUtils.getPrefString(mContext, "token", ""));
                }
                switch (statusCode){
                    case 401:
                        T.showShort(mContext,mContext.getString(R.string.request401));
                        break;
                    case 404:
                        T.showShort(mContext,mContext.getString(R.string.request404));
                        break;
                }
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
        get(mContext, encodedUR, jsonHttpResponseHandler);
    }
}
