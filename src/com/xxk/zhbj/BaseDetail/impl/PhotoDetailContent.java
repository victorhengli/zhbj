package com.xxk.zhbj.BaseDetail.impl;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.xxk.zhbj.BaseDetail.BaseDetailContent;

/**
 * Created by victorhengli on 2016/4/19.
 */
public class PhotoDetailContent extends BaseDetailContent {


    public PhotoDetailContent(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView tv = new TextView(mActivity);
        tv.setText("组图");
        tv.setGravity(Gravity.CENTER);
        return tv;
    }
}
