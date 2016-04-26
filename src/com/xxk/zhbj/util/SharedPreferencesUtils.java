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

}
