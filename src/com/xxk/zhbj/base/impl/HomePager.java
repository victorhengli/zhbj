package com.xxk.zhbj.base.impl;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.xxk.zhbj.MainActivity;
import com.xxk.zhbj.base.BasePager;

/**
 * 首页
 * Created by victorhengli on 2016/4/16.
 */
public class HomePager extends BasePager {

    public HomePager(Activity activity) {
        super(activity);
        //initData();
    }

    public void initData(){
        mTextView.setText("智慧北京");
        mImageButton.setVisibility(View.INVISIBLE);
        TextView tv = new TextView(getActivity());
        tv.setText("智慧北京");
        mFrameLayout.addView(tv);
        setSlidingMenuMode(false);
    }
}
