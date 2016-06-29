package com.pda.birdex.pda.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.pda.birdex.pda.R;
import com.pda.birdex.pda.activity.BaseActivity;
import com.pda.birdex.pda.interfaces.BackHandledInterface;
import com.pda.birdex.pda.utils.HideSoftKeyboardUtil;
import com.pda.birdex.pda.utils.SafeProgressDialog;
import com.pda.birdex.pda.widget.RotateLoading;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;

///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link BaseFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link BaseFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public abstract class BaseFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    protected BackHandledInterface mBackHandledInterface;
    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
    private int mContentLayoutResId;
    ProgressDialog bar;
    Dialog loadingDialog;
    Bundle bundle;
    /**
     * 缓存content布局
     */
    protected View contentView;
    private OnFragmentInteractionListener mListener;

    protected EventBus bus;

    protected EditText search;

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment BaseFragment.
//     */
    // TODO: Rename and change types and number of parameters
//    public static BaseFragment newInstance(String param1, String param2) {
//        BaseFragment fragment = new BaseFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public void setUIArguments(final Bundle args) {
//        getActivity().runOnUiThread(new Runnable() {
//            public void run() {
            /* do your UI stuffs */
        bundle = args;
//            }
//        });
    }

    public Bundle getUIArguments(){
        return bundle;
    }

    public void showLoading() {
        if (loadingDialog == null)
            loadingDialog = new SafeProgressDialog(getActivity(), R.style.semester_dialog);// 创建自定义样式dialog
//        loadingDialog.setCancelable(false);// 不可以用“返回键”取消
//        loadingDialog.setCanceledOnTouchOutside(false);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading, null);
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

    public void hideLoading() {
        if (loadingDialog != null)
            loadingDialog.dismiss();
    }


    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onHiddenChanged(boolean hidden) {//用于hide时使用
        super.onHiddenChanged(hidden);
        if (mBackHandledInterface != null)
            mBackHandledInterface.setSelectedFragment(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mBackHandledInterface.setSelectedFragment(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus = EventBus.getDefault();
        bar = new ProgressDialog(getActivity());

        bar.setCanceledOnTouchOutside(false);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
        if (!(getActivity() instanceof BackHandledInterface)) {
            throw new ClassCastException("Hosting Activity must implement BackHandledInterface");
        } else {
            this.mBackHandledInterface = (BackHandledInterface) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (null != contentView) {
            ViewGroup parent = (ViewGroup) contentView.getParent();
            if (null != parent) {
                parent.removeViewInLayout(contentView);
            }
        } else {
            mContentLayoutResId = getContentLayoutResId();
            if (0 == mContentLayoutResId) {
                throw new IllegalArgumentException(
                        "mContentLayoutResId is 0, "
                                + "you must thought the method getContentLayoutResId() set the mContentLayoutResId's value"
                                + "when you used a fragment which implements the gta.dtp.fragment.BaseFragment.");
            }
            contentView = inflater.inflate(mContentLayoutResId, container, false);
            // 注解方式初始化控件
            ButterKnife.bind(this, contentView);
            HideSoftKeyboardUtil.setupAppCompatUI(contentView, (BaseActivity) getActivity());
            initializeContentViews();
            isPrepared = true;
            lazyLoad();
        }

        return contentView;
    }

    public View getView() {
        return contentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
//        MyApplication.ahc.cancelRequests(getActivity(), true);//如果this被销毁就关闭请求
        hideBar();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    /*
    * 系统按键捕捉
    * */
    protected abstract void key(int keyCode, KeyEvent event);

    public void onKeyDown(int keyCode, KeyEvent event) {
        key(keyCode, event);
    }


    /**
     * 获取子类fragment布局资源id（作用等同于activity的setContentView（int resId）中指定的resId）
     */
    public abstract int getContentLayoutResId();

    /**
     * 初始化具体子类布局资源里的views
     */
    public abstract void initializeContentViews();

    public void showBar() {
        bar.setMessage("加载中...");
        bar.show();
    }

    public void showBarCommit() {
        bar.setMessage("正在提交...");
        bar.show();
    }

    public void hideBar() {
        bar.dismiss();
    }

    /**
     * 在这里实现Fragment数据的缓加载.
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected boolean isVisible;//是否可见
    protected boolean isPrepared;//控件初始化完成

    protected abstract void lazyLoad();

    protected void onInvisible() {
    }
}
