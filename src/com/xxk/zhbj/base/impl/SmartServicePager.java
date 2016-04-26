package com.xxk.zhbj.base.impl;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.xxk.zhbj.base.BasePager;

/**
 * 智慧服务
 * Created by victorhengli on 2016/4/16.
 */
public class SmartServicePager extends BasePager {

    public SmartServicePager(Activity activity) {
        super(activity);
        //initData();
    }

    public void initData(){
        mTextView.setText("智慧服务");
        mImageButton.setVisibility(View.INVISIBLE);
        TextView tv = new TextView(getActivity());
        tv.setText("智慧服务");
        mFrameLayout.addView(tv);
        setSlidingMenuMode(true);
    }
}
