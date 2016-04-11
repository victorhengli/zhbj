package com.xxk.zhbj;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

/**
 * Created by victorhengli on 2016/4/11.
 */
public class SplashActivity extends Activity {

    private RelativeLayout mSplashActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSplashActivity = (RelativeLayout) findViewById(R.id.splash_rr);
        startAnimation();
    }

    private void startAnimation(){
        final AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(this,R.anim.splash);
        mSplashActivity.setAnimation(animationSet);
    }
}
