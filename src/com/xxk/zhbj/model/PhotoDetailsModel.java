package com.xxk.zhbj.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victorhengli on 2016/5/6.
 */
public class PhotoDetailsModel {

    public PhotoDetailModel data;
    public String retcode;

    public class PhotoDetailModel{
        public String countcommenturl;
        public String more;
        public ArrayList<NewsDetailModel> news = new ArrayList<>();
        public String title;
        public List topic;
    }

    public class NewsDetailModel{
        public String comment    ;
        public String commentlist;
        public String commenturl ;
        public String id         ;
        public String largeimage ;
        public String listimage  ;
        public String pubdate    ;
        public String smallimage ;
        public String title      ;
        public String type       ;
        public String url        ;
    }

}
