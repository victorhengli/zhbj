package com.xxk.zhbj.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义处理事件的viewPager
 * 当前页面是第一个，允许拉出侧边栏，当前页面不是第一个，不允许拉出侧边栏
 * Created by victorhengli on 2016/4/23.
 */
public class CustomNewsContentViewPager extends ViewPager {
    public CustomNewsContentViewPager(Context context) {
        super(context);
    }

    public CustomNewsContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 这里设置不消耗事件，让其继续传递
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getCurrentItem() != 0){
            getParent().requestDisallowInterceptTouchEvent(true);
        }else{
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.dispatchTouchEvent(ev);
    }
}
