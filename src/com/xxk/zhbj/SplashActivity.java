package com.xxk.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import com.xxk.zhbj.util.SharedPreferencesUtils;

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
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean flag = SharedPreferencesUtils.getGuideShowFlag(SplashActivity.this,false);
                if(flag){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }else{
                    startActivity(new Intent(SplashActivity.this,GuideActivity.class));
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mSplashActivity.setAnimation(animationSet);
    }
}
