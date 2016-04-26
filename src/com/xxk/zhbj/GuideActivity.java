package com.xxk.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.xxk.zhbj.util.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victorhengli on 2016/4/12.
 */
public class GuideActivity extends Activity {

    private ViewPager mViewPager;
    private Button mButton;
    private int[] resouces = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private List<ImageView> list = new ArrayList<>();
    private LinearLayout mLinearLayout;
    private View mRedPointView;
    private int pointDistance;//两个小黑点之间的距离

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.guide_vp);
        mButton = (Button) findViewById(R.id.guide_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.setGuideShowFlag(GuideActivity.this,true);
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
            }
        });
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_guide_point);
        mRedPointView = findViewById(R.id.guide_red_point);
        initView();
        GuideAdapter adapter = new GuideAdapter();
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new GuidePagerChangeListener());
    }

    private void initView(){
        for(int i = 0 ; i < resouces.length ; i++){
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(resouces[i]);
            list.add(imageView);
        }
        for(int i = 0 ; i < resouces.length ; i++){
            View view = new View(this);
            view.setBackgroundResource(R.drawable.guide_point_grey);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20,20);
            if(i > 0){
                params.leftMargin = 20;
            }
            view.setLayoutParams(params);
            mLinearLayout.addView(view);
        }
        calculatePointDistance();
    }

    /**
     * 计算两个小黑点之间的距离
     */
    private void calculatePointDistance(){
        mLinearLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pointDistance = mLinearLayout.getChildAt(1).getLeft() - mLinearLayout.getChildAt(0).getLeft();
                System.out.println("pointDistance:" + pointDistance);
                //由于每次监听都会调用该方法三次，因此为了节省资源，在得到距离后就移除该监听
                mLinearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return resouces.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == (View) o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 实现小红点随viewpager的滑动而滑动
     *   1/设置viewpager的监听事件 需要用到onpageScrolled方法中的三个参数，i代表当前滑动的位置
     *      v代表滑动的百分比，il代表滑动的距离
     *   2/计算小红点需要滑动的距离：首先计算两个小黑点之间的距离*viewpager滑动的百分比即可计算出小红点应滑动的距离
     *      计算两个小黑点的距离使用两个小黑点的getLeft方法计算差距，由于getLeft方法是放在Activity的onCreate方法中执行
     *      此时，可能还不能得到view的layout的值，这时需要借助该view父容器的ViewTreeObserver对象监视layout的获取，当layout
     *      后，会通过该对象的回调方法得到left值
     *   3/计算出小红点滑动距离后，可设置小红点的margin_left来实现小红点的实时滑动
     */
    class GuidePagerChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int i, float v, int i1) {
            //计算小红点应该滑动的距离
            int  redPointDistance = (int) (pointDistance * v + i*pointDistance);
            //设置小红点的leftMargin
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mRedPointView.getLayoutParams();
            params.leftMargin = redPointDistance;
            mRedPointView.setLayoutParams(params);
            //todo 此处不明白为什么不用调用invalidate()重绘
        }

        @Override
        public void onPageSelected(int i) {
            //设置引导页按钮显示与隐藏
            if(i == resouces.length-1){
                mButton.setVisibility(View.VISIBLE);
            }else{
                mButton.setVisibility(View.INVISIBLE);
            }


        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }
}
