package com.xxk.zhbj.BaseDetail;

import android.app.Activity;
import android.view.View;

/**
 * 侧边栏选项对应的内容基类
 * Created by victorhengli on 2016/4/19.
 */
public abstract class BaseDetailContent {

    public Activity mActivity;
    public View mContentView;

    public BaseDetailContent(Activity activity){
        this.mActivity = activity;
        mContentView = initView();
    }

    public abstract View initView();

    public void initData(){}




}
