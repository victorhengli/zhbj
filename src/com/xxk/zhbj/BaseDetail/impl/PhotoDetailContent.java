package com.xxk.zhbj.BaseDetail.impl;

import android.app.Activity;
import android.view.Gravity;
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
import com.xxk.zhbj.BaseDetail.BaseDetailContent;
import com.xxk.zhbj.R;
import com.xxk.zhbj.global.GlobalContants;
import com.xxk.zhbj.model.PhotoDetailsModel;
import com.xxk.zhbj.util.CacheUtils;
import com.xxk.zhbj.util.bitmap.MyBitmapUtils;

import java.util.ArrayList;

/**
 * 新闻组图模块（实现listview和gridview的切换）
 * Created by victorhengli on 2016/4/19.
 */
public class PhotoDetailContent extends BaseDetailContent {


    private ListView mListView;
    private GridView mGridView;
    private ArrayList<PhotoDetailsModel.NewsDetailModel> list = new ArrayList<>();
    private MyBitmapUtils imageUtils;
    private ImageButton mPhotoButton;//控制显示单列和多列的按钮
    private boolean isListFlag = true;//为单列和多列设置标记

    public PhotoDetailContent(Activity activity, ImageButton mPhotoButton) {
        super(activity);
        this.mPhotoButton = mPhotoButton;
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 changeDisplay();
            }
        });
        imageUtils = new MyBitmapUtils(activity);
    }

    private void changeDisplay() {
        if(isListFlag){//单列变为多列
            mGridView.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
            mPhotoButton.setImageResource(R.drawable.icon_pic_grid_type);
            isListFlag = false;
        }else{//多列变为单列
            mGridView.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            mPhotoButton.setImageResource(R.drawable.icon_pic_list_type);
            isListFlag = true;
        }
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.news_photo_list_grid_view, null);
        mListView = (ListView) view.findViewById(R.id.lv_news_photo);
        mGridView = (GridView) view.findViewById(R.id.gv_news_photo);
        return view;
    }

    @Override
    public void initData() {
        String result = CacheUtils.getCache(mActivity,GlobalContants.NEWSPHOTO_ADDRESS);
        if(result == null || result.isEmpty()){
            getData();
        }else{
            parseData(result);
        }
        PhotoAdapter adapter = new PhotoAdapter();
        mListView.setAdapter(adapter);
        mGridView.setAdapter(adapter);
    }

    private void getData() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, GlobalContants.NEWSPHOTO_ADDRESS, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CacheUtils.setCache(mActivity,GlobalContants.NEWSPHOTO_ADDRESS,result);
                parseData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Toast.makeText(mActivity,msg,Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

    }

    private void parseData(String result) {
        Gson gson = new Gson();
        PhotoDetailsModel photoDetailsModel = gson.fromJson(result, PhotoDetailsModel.class);
        list = photoDetailsModel.data.news;
    }


    class PhotoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public PhotoDetailsModel.NewsDetailModel getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PhotoCardHolder photoCardHolder = null;
            if(convertView == null){
                photoCardHolder = new PhotoCardHolder();
                convertView = View.inflate(mActivity,R.layout.news_photo_list_grid_item,null);
                photoCardHolder.mPhotoImageView = (ImageView) convertView.findViewById(R.id.iv_news_photo);
                photoCardHolder.mPhotoTextView = (TextView) convertView.findViewById(R.id.tv_news_photo_title);
                convertView.setTag(photoCardHolder);
            }else{
                photoCardHolder = (PhotoCardHolder) convertView.getTag();
            }
            //imageUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
            imageUtils.display(photoCardHolder.mPhotoImageView, list.get(position).listimage);
            photoCardHolder.mPhotoTextView.setText(list.get(position).title);
            return convertView;
        }
    }

    class PhotoCardHolder{
        public ImageView mPhotoImageView;
        public TextView mPhotoTextView;
    }
}
