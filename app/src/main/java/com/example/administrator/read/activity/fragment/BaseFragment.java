package com.example.administrator.read.activity.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/11/16.
 */

public abstract class BaseFragment extends Fragment{

    private boolean isViewPrepared;//标识fragment视图已经初始化完毕
    private  boolean hasFetchData;//标识已经触发过懒加载数据
    protected ViewDataBinding binding;

    protected abstract
    @LayoutRes
    int getLayoutId();

    public ViewDataBinding getBinding(){
        return binding;
    }
    //初始化View
    protected abstract void initViews();

    //懒加载
    protected abstract void lazyFetchData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        initViews();
        return binding.getRoot();

    }

    /**
     * 判断Fragment懒加载是否准备完成
     */
    private void lazyFetchDataIfPrepared() {
        if (getUserVisibleHint() && !hasFetchData && isViewPrepared) {
            hasFetchData = true;
            lazyFetchData();
        }
    }
    /**
     * 用户可见
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            lazyFetchDataIfPrepared();
        }
    }
    /**
     * View创建完成
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isViewPrepared = true;
        lazyFetchDataIfPrepared();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hasFetchData = false;
        isViewPrepared = false;
    }

}
