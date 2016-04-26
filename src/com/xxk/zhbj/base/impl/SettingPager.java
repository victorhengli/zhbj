package com.xxk.zhbj.base.impl;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.xxk.zhbj.base.BasePager;

/**
 * 设置
 * Created by victorhengli on 2016/4/16.
 */
public class SettingPager extends BasePager {

    public SettingPager(Activity activity) {
        super(activity);
        //initData();
    }

    public void initData(){
        mTextView.setText("设置");
        mImageButton.setVisibility(View.INVISIBLE);
        TextView tv = new TextView(getActivity());
        tv.setText("设置");
        mFrameLayout.addView(tv);
        setSlidingMenuMode(false);
    }
}
