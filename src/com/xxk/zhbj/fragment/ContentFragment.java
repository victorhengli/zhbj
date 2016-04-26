package com.xxk.zhbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioGroup;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxk.zhbj.R;
import com.xxk.zhbj.base.BasePager;
import com.xxk.zhbj.base.impl.*;

import java.util.ArrayList;

/**
 * Created by victorhengli on 2016/4/15.
 */
public class ContentFragment extends BaseFragment {

    @ViewInject(R.id.content_raidoGroup)
    private RadioGroup radioGroup;
    @ViewInject(R.id.content_viewPager)
    private ViewPager mViewPager;
    private int count;//RadioButton数量
    public ArrayList<BasePager> list = new ArrayList<>();

    
    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content,null);
        ViewUtils.inject(this,view);
        //RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.content_raidoGroup);
        count = radioGroup.getChildCount();
        radioGroup.check(R.id.home);
        return view;
    }


    @Override
    protected void initData() {
        list.add(new HomePager(mActivity));
        list.add(new NewsCenterPager(mActivity));
        list.add(new SmartServicePager(mActivity));
        list.add(new GovmentPager(mActivity));
        list.add(new SettingPager(mActivity));
        mViewPager.setAdapter(new ViewPagerAdapter());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        list.get(0).initData();
        //监听radiobutton事件，切换viewpager页面
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.home:
                        mViewPager.setCurrentItem(0,false);
                        break;
                    case R.id.news:
                        mViewPager.setCurrentItem(1, false);
                        break;
                    case R.id.gov:
                        mViewPager.setCurrentItem(2,false);
                        break;
                    case R.id.smartService:
                        mViewPager.setCurrentItem(3,false);
                        break;
                    case R.id.setting:
                        mViewPager.setCurrentItem(4,false);
                        break;
                }
            }
        });
    }


    class ViewPagerAdapter extends PagerAdapter{

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
            BasePager basePager = list.get(position);
            container.addView(basePager.getContentView());
            return basePager.getContentView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取viewpager的几个页面
     * @return
     */
    public NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager) list.get(1);
    }
}
