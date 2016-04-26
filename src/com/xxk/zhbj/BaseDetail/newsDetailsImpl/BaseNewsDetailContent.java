package com.xxk.zhbj.BaseDetail.newsDetailsImpl;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;
import com.xxk.zhbj.BaseDetail.BaseDetailContent;
import com.xxk.zhbj.R;
import com.xxk.zhbj.customview.RefreshListView;
import com.xxk.zhbj.global.GlobalContants;
import com.xxk.zhbj.model.CategoryModel;
import com.xxk.zhbj.model.NewsDetailsModel;

import java.util.ArrayList;

/**
 * Created by victorhengli on 2016/4/20.
 */
public class BaseNewsDetailContent extends BaseDetailContent implements ViewPager.OnPageChangeListener {

    private CategoryModel.NewsCategoryModel newsCategoryModel;
    @ViewInject(R.id.image_view_pager)
    private ViewPager mViewPager;
    @ViewInject(R.id.news_detail_listview)
    private RefreshListView mListView;
    @ViewInject(R.id.image_text)
    private TextView mImageText;
    @ViewInject(R.id.image_indicator)
    private CirclePageIndicator imageIndicator;
    private NewsDetailsModel model;
    private ArrayList<NewsDetailsModel.News> news;
    private ArrayList<NewsDetailsModel.Topic> topics;
    private ArrayList<NewsDetailsModel.TopicNews> topicnews;
    private BitmapUtils imageUtils;

    public BaseNewsDetailContent(Activity activity, CategoryModel.NewsCategoryModel newsCategoryModel) {
        super(activity);
        this.newsCategoryModel = newsCategoryModel;
        imageUtils = new BitmapUtils(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.news_image_view_pager,null);
        View headerView = View.inflate(mActivity,R.layout.news_detail_listview_header,null);
        ViewUtils.inject(this,view);
        ViewUtils.inject(this,headerView);
        mListView.addHeaderView(headerView);
        return view;
    }

    public void initData() {
        getData();

    }

    private void getData() {
        String dataUrl = GlobalContants.IP + newsCategoryModel.url;
        System.out.println("dataUrl=" + dataUrl);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, dataUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                parseData((String)responseInfo.result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    private void parseData(String result) {
        Gson gson = new Gson();
        model = gson.fromJson(result, NewsDetailsModel.class);
        news = model.data.news;
        topics = model.data.topic;
        topicnews = model.data.topnews;
        ImagePagerAdapter adapter = new ImagePagerAdapter();
        mViewPager.setAdapter(adapter);
        imageIndicator.setViewPager(mViewPager);
        imageIndicator.setOnPageChangeListener(this);
        imageIndicator.setSnap(true);
        imageIndicator.onPageSelected(0);
        mImageText.setText(topicnews.get(0).title);
        ListViewAdapter listViewAdapter = new ListViewAdapter();
        mListView.setAdapter(listViewAdapter);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        mImageText.setText(topicnews.get(i).title);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    class ImagePagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return topicnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mActivity);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
            imageUtils.display(imageView, topicnews.get(position).topimage);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class ListViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public NewsDetailsModel.News getItem(int position) {
            return news.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NewsHolder newsHolder = null;
            if(convertView == null){
                newsHolder = new NewsHolder();
                convertView = View.inflate(mActivity,R.layout.news_detail_listview_item,null);
                newsHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_news_detail);
                newsHolder.title = (TextView) convertView.findViewById(R.id.tv_news_detail_title);
                newsHolder.date = (TextView) convertView.findViewById(R.id.tv_news_detail_date);
                convertView.setTag(newsHolder);
            }else{
                newsHolder = (NewsHolder) convertView.getTag();
            }
            newsHolder.title.setText(news.get(position).title);
            newsHolder.date.setText(news.get(position).pubdate);
            imageUtils.configDefaultLoadingImage(R.drawable.pic_item_list_default);
            imageUtils.display(newsHolder.imageView, news.get(position).listimage);
            return convertView;
        }
    }

    static class NewsHolder{
        ImageView imageView;
        TextView title;
        TextView date;
    }

}
