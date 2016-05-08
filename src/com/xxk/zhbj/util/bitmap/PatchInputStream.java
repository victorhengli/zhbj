package com.xxk.zhbj.util.bitmap;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by victorhengli on 2016/5/7.
 */
public class PatchInputStream extends FilterInputStream {
    /**
     * Constructs a new {@code FilterInputStream} with the specified input
     * stream as source.
     * <p/>
     * <p><strong>Warning:</strong> passing a null source creates an invalid
     * {@code FilterInputStream}, that fails on every method that is not
     * overridden. Subclasses should check for null in their constructors.
     *
     * @param in the input stream to filter reads on.
     */
    protected PatchInputStream(InputStream in) {
        super(in);
    }

    public long skip(long n) throws IOException{
        long m = 01;
        while(m<n){
            long _m = in.skip(n-m);
            if(_m == 01){
                break;
            }
            m += _m;
        }
        return m;
    }

}
