package com.xxk.zhbj.base.impl;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.xxk.zhbj.BaseDetail.BaseDetailContent;
import com.xxk.zhbj.BaseDetail.impl.InteractDetailContent;
import com.xxk.zhbj.BaseDetail.impl.NewsDetailContent;
import com.xxk.zhbj.BaseDetail.impl.PhotoDetailContent;
import com.xxk.zhbj.BaseDetail.impl.TopicDetailContent;
import com.xxk.zhbj.MainActivity;
import com.xxk.zhbj.base.BasePager;
import com.xxk.zhbj.global.GlobalContants;
import com.xxk.zhbj.model.CategoryModel;
import com.xxk.zhbj.util.CacheUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * 新闻中心
 * Created by victorhengli on 2016/4/16.
 */
public class NewsCenterPager extends BasePager {

    private ArrayList<BaseDetailContent> baseDetailContents = new ArrayList<>();
    public NewsCenterPager(Activity activity) {
        super(activity);
        //initData();
    }


    public void initData(){
        mTextView.setText("新闻中心");
        /*TextView tv = new TextView(getActivity());
        tv.setText("新闻中心");
        mFrameLayout.addView(tv);*/
        setSlidingMenuMode(true);
        String result = CacheUtils.getCache(getActivity(),GlobalContants.SERVER_ADDRESS);//先从缓存中获取
        System.out.println("sharePreference:"+result);
        if(result == null || "".equals(result)){
            getData();
        }else{
            parseData(result);
        }
    }

    private void getData() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, GlobalContants.SERVER_ADDRESS, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                CacheUtils.setCache(getActivity(),GlobalContants.SERVER_ADDRESS,result);
                parseData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                System.out.println("errorMsg:" + msg);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }

    private void parseData(String result){
        Gson gson = new Gson();
        CategoryModel model = gson.fromJson(result, CategoryModel.class);
        //将获取到的数据传递给leftMenuFragment用于初始化数据
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.getLeftMenuFragment().setLeftMenuData(model);

        //添加侧边栏选项对应的内容
        //通过构造方法将数据传给侧边栏新闻选项对应的内容
        baseDetailContents.add(new NewsDetailContent(getActivity(),model.data.get(0).children));
        baseDetailContents.add(new TopicDetailContent(getActivity()));
        baseDetailContents.add(new PhotoDetailContent(getActivity(),mPhotoButton));
        baseDetailContents.add(new InteractDetailContent(getActivity()));

        //默认设置侧边栏第一个选项对应的标题
        mTextView.setText(model.data.get(0).title);
        //默认显示侧边栏第一个选项对应的内容
        setContentView(0);
    }

    /**
     * 该方法为侧边栏点击事件暴露的，点击对应的侧边栏时，更改对应的内容
     * @param position
     */
    public void setContentView(int position){
        BaseDetailContent baseDetailContent = baseDetailContents.get(position);
        if(baseDetailContent instanceof PhotoDetailContent){//如果该content是侧边栏选项组图的内容，则将标题栏右上角的图标显示，否则隐藏
            mPhotoButton.setVisibility(View.VISIBLE);
        }else{
            mPhotoButton.setVisibility(View.GONE);
        }
        mFrameLayout.removeAllViews();
        mFrameLayout.addView(baseDetailContent.mContentView);
        baseDetailContent.initData();

    }


    private void getData1(){

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(GlobalContants.SERVER_ADDRESS);
        try {
            HttpResponse response = client.execute(get);
            System.out.println(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getData2(){
        try {
            URL uRl = new URL(GlobalContants.SERVER_ADDRESS);
            HttpURLConnection connection = (HttpURLConnection) uRl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("connnection", "keep-Alive");
            String response = connection.getResponseMessage();
            System.out.println(response);
            connection.getContent();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
