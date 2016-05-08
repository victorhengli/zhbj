package com.xxk.zhbj.util;

import android.content.Context;

/**
 * 缓存工具类
 * Created by victorhengli on 2016/5/5.
 */
public class CacheUtils {

    public static void setCache(Context context,String key,String value){
          SharedPreferencesUtils.setString(context,key,value);
    }

    public static String getCache(Context context,String key){
        return SharedPreferencesUtils.getString(context,key);
    }

}
