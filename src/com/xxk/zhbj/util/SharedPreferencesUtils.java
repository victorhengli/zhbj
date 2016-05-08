package com.xxk.zhbj.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by victorhengli on 2016/4/14.
 */
public class SharedPreferencesUtils {

    private final static String CONFIG = "config";

    public static boolean getGuideShowFlag(Context context,boolean defaultFlag){
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sp.getBoolean("is_guide_show",defaultFlag);
    }

    public static void setGuideShowFlag(Context context,boolean flag){
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        sp.edit().putBoolean("is_guide_show",flag).commit();
    }

    public static String getNewsIdFlag(Context context,String defaultId){
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sp.getString("read_newsId", defaultId);
    }

    public static void setNewsIdFlag(Context context,String id){
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        sp.edit().putString("read_newsId", id).commit();
    }

    public static void setString(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context context,String key){
        SharedPreferences sp = context.getSharedPreferences(CONFIG, Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

}
