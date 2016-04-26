package com.xxk.zhbj.BaseDetail.impl;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;
import com.xxk.zhbj.BaseDetail.BaseDetailContent;
import com.xxk.zhbj.BaseDetail.newsDetailsImpl.BaseNewsDetailContent;
import com.xxk.zhbj.MainActivity;
import com.xxk.zhbj.R;
import com.xxk.zhbj.model.CategoryModel;

import java.util.ArrayList;

/**
 * Created by victorhengli on 2016/4/19.
 */
public class NewsDetailContent extends BaseDetailContent implements ViewPager.OnPageChangeListener {

    private ViewPager  mViewPager;
    private TabPageIndicator mTitlePageIndicator;
    private ArrayList<BaseNewsDetailContent> list = new ArrayList();//用于存储viewpager中的数据
    private ArrayList<CategoryModel.NewsCategoryModel> datas;
    private ImageButton mImageButton;

    public NewsDetailContent(Activity activity, ArrayList<CategoryModel.NewsCategoryModel> children) {
        super(activity);
        datas = children;
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.news_detail_view_pager,null);
        mViewPager = (ViewPager) view.findViewById(R.id.news_detail_view_pager);
        mTitlePageIndicator = (TabPageIndicator) view.findViewById(R.id.news_detail_indicator_titles);
        mTitlePageIndicator.setOnPageChangeListener(this);
        mImageButton = (ImageButton) view.findViewById(R.id.news_cate_arr);

        return view;
    }


    public void initData(){
        for(int i = 0 ; i < datas.size() ; i++){
            BaseNewsDetailContent baseNewsDetailContent = new BaseNewsDetailContent(mActivity,datas.get(i));
            list.add(baseNewsDetailContent);
        }
        final NewsDetailAdapter adapter = new NewsDetailAdapter();
        mViewPager.setAdapter(adapter);
        mTitlePageIndicator.setViewPager(mViewPager);
        /*mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                list.get(i).initData();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        list.get(0).initData();*/
        //为箭头设置点击事件
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //得到当前的页面的位置
                int position = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem((++position) % adapter.getCount());
            }
        });
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        //判断当前页面是否为第一个页面，如果是第一个页面，则允许侧边栏拉出
        MainActivity activity = (MainActivity) mActivity;
        SlidingMenu slidingMenu = activity.slidingMenu;
        if(i == 0){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else{
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    class NewsDetailAdapter extends PagerAdapter{


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position).mContentView);
            list.get(position).initData();
            return list.get(position).mContentView;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return datas.get(position).title;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }



    }


}
