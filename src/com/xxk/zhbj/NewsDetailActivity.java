package com.xxk.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 新闻详情页，使用webview展示
 * Created by victorhengli on 2016/4/28.
 */
public class NewsDetailActivity extends Activity implements View.OnClickListener {

    private ImageButton mBack;
    private ImageButton mShare;
    private ImageButton mTextSize;
    private WebView     mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news_detail_webview);
        mBack = (ImageButton) findViewById(R.id.ib_news_detail_back);
        mShare = (ImageButton) findViewById(R.id.ib_news_detail_share);
        mTextSize = (ImageButton) findViewById(R.id.ib_news_detail_textsize);
        mWebView = (WebView) findViewById(R.id.news_detail_webview);
        mBack.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mTextSize.setOnClickListener(this);
        getData();
    }

    private void getData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        mWebView.loadUrl("http://www.xixingke.cn");
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);//开启js
        settings.setBuiltInZoomControls(true);//显示放大缩小
        settings.setUseWideViewPort(true);//支持双击缩放
        mWebView.setWebViewClient(new WebViewClient() {//点击链接时拦截事件
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享朋友圈");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.ib_news_detail_back:
                finish();
                break;
            case R.id.ib_news_detail_share:
                showShare();
                break;
            case R.id.ib_news_detail_textsize:
                break;
        }

    }
}
