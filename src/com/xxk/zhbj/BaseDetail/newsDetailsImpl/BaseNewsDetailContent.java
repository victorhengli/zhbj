package com.xxk.zhbj.BaseDetail.newsDetailsImpl;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
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
import com.xxk.zhbj.NewsDetailActivity;
import com.xxk.zhbj.R;
import com.xxk.zhbj.customview.RefreshListView;
import com.xxk.zhbj.global.GlobalContants;
import com.xxk.zhbj.model.CategoryModel;
import com.xxk.zhbj.model.NewsDetailsModel;
import com.xxk.zhbj.util.CacheUtils;
import com.xxk.zhbj.util.SharedPreferencesUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
    private String moreUrl;//加载更多url
    private Handler mHandler;

    public BaseNewsDetailContent(Activity activity, CategoryModel.NewsCategoryModel newsCategoryModel) {
        super(activity);
        this.newsCategoryModel = newsCategoryModel;
        imageUtils = new BitmapUtils(activity);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.news_image_view_pager, null);
        View headerView = View.inflate(mActivity,R.layout.news_detail_listview_header,null);
        ViewUtils.inject(this,view);
        ViewUtils.inject(this, headerView);
        mListView.addHeaderView(headerView);
        mListView.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            //下拉刷新
            @Override
            public void pullToRefresh() {
                Toast.makeText(mActivity, "正在刷新数据", Toast.LENGTH_SHORT).show();
                getData(true);
            }

            //下拉刷新
            @Override
            public void pushToRefresh() {
                if (moreUrl != null) {
                    getMoreData(true);
                } else {
                    Toast.makeText(mActivity, "已经到最后一页了", Toast.LENGTH_SHORT).show();
                    mListView.onRefreshComplete();
                }

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position+"项被点击了");
                String newsId = news.get(position).id;
                String ids = SharedPreferencesUtils.getNewsIdFlag(mActivity,"0");
                if(!ids.contains(newsId)){
                    ids = ids + newsId + ",";
                }
                TextView tv = (TextView) view.findViewById(R.id.tv_news_detail_title);
                tv.setTextColor(Color.GRAY);
                SharedPreferencesUtils.setNewsIdFlag(mActivity, ids);
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url",news.get(position).commenturl);
                mActivity.startActivity(intent);
            }
        });
        return view;
    }

    public void initData() {
        String result = CacheUtils.getCache(mActivity,GlobalContants.IP + newsCategoryModel.url);
        if(result == null || "".equals(result)){
            getData(false);
        }else{
            parseData(result,false);
        }

    }

    private void getData(boolean isRefreshDate) {
        final String dataUrl = GlobalContants.IP + newsCategoryModel.url;
        System.out.println("dataUrl=" + dataUrl);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, dataUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                CacheUtils.setCache(mActivity,dataUrl,responseInfo.result);
                parseData((String) responseInfo.result,false);
                mListView.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                mListView.onRefreshComplete();
            }
        });
    }

    private void getMoreData(boolean isRefreshDate) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, moreUrl, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                parseData((String)responseInfo.result,true);
                mListView.onRefreshComplete();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                mListView.onRefreshComplete();
            }
        });
    }


    private void parseData(String result,boolean isLodingMore) {
        Gson gson = new Gson();
        model = gson.fromJson(result, NewsDetailsModel.class);
        moreUrl = model.data.more;
        if(!TextUtils.isEmpty(moreUrl)){
            moreUrl = GlobalContants.IP + moreUrl;
            System.out.println("moreUrl:"+moreUrl);
        }else{
            moreUrl = null;
        }
        ListViewAdapter listViewAdapter = new ListViewAdapter();
        if(!isLodingMore){
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
            mListView.setAdapter(listViewAdapter);
            if(mHandler == null){
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        System.out.println("msg:"+msg);
                        int currentItem = mViewPager.getCurrentItem();
                        if(currentItem < topicnews.size()-1){
                            currentItem ++;
                        }else{
                            currentItem = 0;
                        }
                        mViewPager.setCurrentItem(currentItem);
                        mHandler.sendEmptyMessageDelayed(0,2000);//图片切换后，再次发送消息，实现循环递归
                    }
                };
                mHandler.sendEmptyMessageDelayed(0,2000);//向handle发送消息，延迟两秒
            }
        }else{//加载更多
            news.addAll(model.data.news);
            topics.addAll(model.data.topic);
            topicnews.addAll(model.data.topnews);
            listViewAdapter.notifyDataSetChanged();
        }

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
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            mHandler.removeCallbacksAndMessages(null);//按下时取消handle消息发送
                            break;
                        case MotionEvent.ACTION_UP://抬起时重新发送消息
                            mHandler.sendEmptyMessageDelayed(0,2000);
                            break;
                        case MotionEvent.ACTION_CANCEL://避免被该事件消耗，无法自动轮播
                            mHandler.sendEmptyMessageDelayed(0,2000);
                            break;
                    }
                    return true;
                }
            });
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
            String ids = SharedPreferencesUtils.getNewsIdFlag(mActivity,"0");
            if(ids.contains(news.get(position).id)){
                newsHolder.title.setTextColor(Color.GRAY);
            }
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
