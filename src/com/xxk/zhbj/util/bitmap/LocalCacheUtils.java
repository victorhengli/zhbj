package com.xxk.zhbj.util.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import com.xxk.zhbj.util.MD5Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 本地缓存
 * Created by victorhengli on 2016/5/7.
 */
public class LocalCacheUtils {

    public final static String SDCARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhbj";
    private MemoryCacheUtils memoryCacheUtils;

    public LocalCacheUtils(MemoryCacheUtils memoryCacheUtils) {
        memoryCacheUtils = memoryCacheUtils;
    }


    /**
     * 从SDCard获取
     * @param url
     * @return
     */
    public Bitmap getBitMapFromLocal(String url){
        Bitmap bitmap = null;
        try {
            String fileName = MD5Encoder.encode(url);
            File file = new File(SDCARD_PATH,fileName);
            if(file.exists()){//如果该文件存在
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                memoryCacheUtils.setBitMapFromMemory(url,bitmap);//向内存缓存中存入bitmap
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向本地SDCard中存入bitmap
     * @param url
     * @param bitmap
     */
    public void setBitMapToLocal(String url,Bitmap bitmap){
        try {
            String fileName = MD5Encoder.encode(url);
            File file = new File(SDCARD_PATH,fileName);
            File parentFile = file.getParentFile();
            if(!parentFile.exists()){
                parentFile.mkdirs();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
