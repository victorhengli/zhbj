package com.xxk.zhbj.base.impl;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.xxk.zhbj.base.BasePager;

/**
 * 政务
 * Created by victorhengli on 2016/4/16.
 */
public class GovmentPager extends BasePager {

    public GovmentPager(Activity activity) {
        super(activity);
        //initData();
    }

    public void initData(){
        mTextView.setText("政务");
        mImageButton.setVisibility(View.INVISIBLE);
        TextView tv = new TextView(getActivity());
        tv.setText("政务");
        mFrameLayout.addView(tv);
        setSlidingMenuMode(true);
    }
}
