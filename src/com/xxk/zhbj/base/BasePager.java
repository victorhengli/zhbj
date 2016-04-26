package com.xxk.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxk.zhbj.MainActivity;
import com.xxk.zhbj.R;

/**
 * content页中viewPager显示的view的基类
 * Created by victorhengli on 2016/4/16.
 */
public class BasePager {

    private Activity mActivity;
    private View mContentView;
    //@ViewInject(R.id.content_title)
    public TextView mTextView;
    //@ViewInject(R.id.content_button)
    public FrameLayout mFrameLayout;
    //@ViewInject(R.id.content_frame)
    public ImageButton mImageButton;

    public BasePager(Activity activity){
        this.mActivity = activity;
        initView();
    }

    private void initView(){
        mContentView = View.inflate(mActivity, R.layout.content_layout,null);
        mTextView = (TextView) mContentView.findViewById(R.id.content_title);
        mImageButton = (ImageButton) mContentView.findViewById(R.id.content_button);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSlidingMenuToggle();
            }
        });
        mFrameLayout = (FrameLayout) mContentView.findViewById(R.id.content_frame);
        //ViewUtils.inject(this, mContentView);
    }

    public void setSlidingMenuToggle(){
        MainActivity activity = (MainActivity) mActivity;
        activity.slidingMenu.toggle();
    }


    public void initData(){}

    public Activity getActivity(){
        return mActivity;
    }
    public View getContentView(){
        return mContentView;
    }

    /**
     * 设置slidingMenu侧边栏是否可以拉出
     * @param b
     */
    public void setSlidingMenuMode(boolean b){
        MainActivity mainActivity = (MainActivity) getActivity();
        if(b){
            mainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            mainActivity.slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }

}
