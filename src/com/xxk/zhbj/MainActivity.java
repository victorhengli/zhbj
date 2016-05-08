package com.xxk.zhbj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.xxk.zhbj.fragment.BaseFragment;
import com.xxk.zhbj.fragment.ContentFragment;
import com.xxk.zhbj.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {


    public SlidingMenu slidingMenu;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        setBehindContentView(R.layout.frame_left_menu);
        slidingMenu = getSlidingMenu();//获取SlidingMenu
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//设置全屏可触摸
        slidingMenu.setBehindOffset(700);//设置预留距离
        initFragment();
        

    }

    private void initFragment(){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.add(R.id.frame_content, new ContentFragment(), ContentFragment.class.getName());
        tr.add(R.id.frame_left_menu, new LeftMenuFragment(), LeftMenuFragment.class.getName());
        tr.commit();
    }

    /**
     * 获取侧边栏fragment，暴露给外部使用
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment(){
        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(LeftMenuFragment.class.getName());
    }

    /**
     * 获取contentfragment，暴露给外部使用
     * @return
     */
    public ContentFragment getContentFragment(){
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(ContentFragment.class.getName());
    }
}
