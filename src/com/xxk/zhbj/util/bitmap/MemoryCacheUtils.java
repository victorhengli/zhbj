package com.xxk.zhbj.util.bitmap;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * 内存缓存
 * Created by victorhengli on 2016/5/7.
 */
public class MemoryCacheUtils {

    /**
     * 为LruCache设置最大缓存，即每个应用的最大缓存的1/8
     */
    private LruCache<String,Bitmap> lruCache = new LruCache<String,Bitmap>((int) (Runtime.getRuntime().maxMemory()/8)){
        /**
         * 为每个bitmap设置内存大小
         * @param key
         * @param value
         * @return
         */
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount();//获取每个bitmap的内存大小，api13以下使用bitmap.getRowbytes()*bitmap.getHeight()
        }
    };

    /**
     * 从内存缓存中获取bitmap
     * @param url
     * @return
     */
    public Bitmap getBitMapFromMemory(String url){

        return lruCache.get(url);
    }

    /**
     * 向内存缓存中存放bitmap
     * @param url
     * @param bitmap
     */
    public void setBitMapFromMemory(String url,Bitmap bitmap){
         lruCache.put(url,bitmap);
    }
}
