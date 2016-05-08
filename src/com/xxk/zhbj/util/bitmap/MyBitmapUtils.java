package com.xxk.zhbj.util.bitmap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.ImageView;
import com.xxk.zhbj.R;

/**
 * 自定义图片缓存
 * Created by victorhengli on 2016/5/7.
 */
public class MyBitmapUtils {


    private Activity mActivity;
    private NetCacheUtils netCacheUtils;
    private LocalCacheUtils localCacheUtils;
    private MemoryCacheUtils memoryCacheUtils;

    public MyBitmapUtils(Activity activity) {
        this.mActivity = activity;
        memoryCacheUtils = new MemoryCacheUtils();
        localCacheUtils = new LocalCacheUtils(memoryCacheUtils);//保证只有一个实例
        netCacheUtils = new NetCacheUtils(localCacheUtils,memoryCacheUtils);//保证只有一个实例
    }

    private Bitmap bitmap = null;
    public void display(ImageView mPhotoImageView, String url) {
        mPhotoImageView.setImageResource(R.drawable.news_pic_default);//为图片设置默认
        //加载内存缓存
        bitmap = memoryCacheUtils.getBitMapFromMemory(url);
        if(bitmap != null){
            mPhotoImageView.setImageBitmap(bitmap);
            return;
        }
        //加载本地缓存
        bitmap = localCacheUtils.getBitMapFromLocal(url);
        if(bitmap != null){
            mPhotoImageView.setImageBitmap(bitmap);
            return;
        }
        //加载网络缓存
        netCacheUtils.getBitMapFromNet(mPhotoImageView,url);
    }
}
