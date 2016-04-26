package com.xxk.zhbj.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义不能滑动的viewPager
 * Created by victorhengli on 2016/4/17.
 */
public class CustomNoScollViewPager extends ViewPager {
    public CustomNoScollViewPager(Context context) {
        super(context);
    }

    public CustomNoScollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 这里设置不消耗事件，让其继续传递
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    /**
     * 这里设置不消耗事件，让其继续传递
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}
