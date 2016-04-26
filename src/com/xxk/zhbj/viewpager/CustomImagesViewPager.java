package com.xxk.zhbj.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义轮播图片的viewPager
 * 设置父控件和祖先控件不响应事件
 * Created by victorhengli on 2016/4/23.
 */
public class CustomImagesViewPager extends ViewPager {

    private int x;
    private int y;

    public CustomImagesViewPager(Context context) {
        super(context);
    }

    public CustomImagesViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**
     * 1/向右滑动时，而且是第一页时，请求父控件拦截事件
     * 2/向左滑动时，而且是最后一页时，请求父控件拦截事件
     * 3/上下滑动时，请求父控件拦截事件
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);//请求父控件不拦截事件
                x = (int) ev.getRawX();//相对于屏幕得到当前位置
                y = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int currX = (int) ev.getRawX();
                int currY = (int) ev.getRawY();
                //判断是左右滑动还是上下滑动
                if(Math.abs(currX - x) > Math.abs(currY - y)){//左右滑动
                    if(currX - x > 0){//判断向右滑动
                        if(getCurrentItem() == 0){ //第一页
                            getParent().requestDisallowInterceptTouchEvent(false);//请求父控件拦截事件
                        }
                    }else{ //向左滑动
                        if(getCurrentItem() == getAdapter().getCount() - 1){//最后一页
                            getParent().requestDisallowInterceptTouchEvent(false);//请求父控件拦截事件
                        }
                    }
                }else{ //上下滑动
                    getParent().requestDisallowInterceptTouchEvent(false);//请求父控件拦截事件
                }
                break;
        }

        return super.dispatchTouchEvent(ev);
    }
}
