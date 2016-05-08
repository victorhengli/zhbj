package com.xxk.zhbj.util.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 从网络中获取数据
 * Created by victorhengli on 2016/5/7.
 */
public class NetCacheUtils {

    private GetBitMapTask getBitMapTask = null;
    private ImageView imageView;
    private String url;
    private LocalCacheUtils localCacheUtils;
    private MemoryCacheUtils memoryCacheUtils;

    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.localCacheUtils = localCacheUtils;
        this.memoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 从网络中获取数据
     */
    public void getBitMapFromNet(ImageView mPhotoImageView, String url){
        this.imageView = mPhotoImageView;
        getBitMapTask = new GetBitMapTask();
        getBitMapTask.execute(mPhotoImageView,url);
    }

    class GetBitMapTask extends AsyncTask<Object,Void,Bitmap>{

        /**
         * 执行耗时任务（子线程）
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground(Object... params) {
            ImageView imageView = (ImageView) params[0];
            String url = (String) params[1];
            imageView.setTag(url);
            Bitmap bitmap = downBitMap(imageView,url);
            return bitmap;
        }

        /**
         * 更新进度(主线程)
         * @param values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /**
         * 耗时方法执行后执行(主线程)
         * @param bitmap
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null){
                String tag = (String) imageView.getTag();
                if(tag != null && tag.equals(url)){
                    imageView.setImageBitmap(bitmap);
                    localCacheUtils.setBitMapToLocal(url,bitmap);
                    memoryCacheUtils.setBitMapFromMemory(url,bitmap);
                }
            }
        }
    }

    private Bitmap downBitMap(ImageView imageView, String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("POST");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode == 200){
                InputStream in = conn.getInputStream();
                /*byte[] data = readStream(in);//解决bug方法1
                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);*/
                //Bitmap bitmap = BitmapFactory.decodeStream(new PatchInputStream(in));//解决bug方法2
                //解决bug方法3，使用图片压缩
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;//将原来的bitmap的宽高都压缩为原来的1/2
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;//对bitmap的像素值进行处理
                Bitmap bitmap = BitmapFactory.decodeStream(in,null,options);
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return null;
    }

    /**
     * 得到图片字节流 解决不能转换为bitmap的bug
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] readStream(InputStream inputStream) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,len);
        }
        outputStream.close();
        inputStream.close();
        return outputStream.toByteArray();
    }
}
