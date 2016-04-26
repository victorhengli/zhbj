package com.xxk.zhbj.model;

import java.util.ArrayList;

/**
 * Created by victorhengli on 2016/4/23.
 */
public class NewsDetailsModel {

    public String retcode;
    public NewsModel data;

    public class NewsModel{
        public String countcommenturl;
        public String more;
        public String title;
        public ArrayList<News> news = new ArrayList();
        public ArrayList<Topic> topic = new ArrayList();
        public ArrayList<TopicNews> topnews = new ArrayList();
    }

    public class News{
        //public boolean comment     ;
        public String commentlist ;
        public String commenturl  ;
        public String id          ;
        public String listimage   ;
        public String pubdate     ;
        public String title       ;
        public String type        ;
        public String url         ;
    }

    public class Topic{
        public String description  ;
        public String id           ;
        public String listimage    ;
        public String sort         ;
        public String title        ;
        public String url          ;
    }

    public class TopicNews{
        //public String comment       ;
        public String commentlist   ;
        public String commenturl    ;
        public String id            ;
        public String pubdate       ;
        public String title         ;
        public String topimage      ;
        public String type          ;
        public String url           ;
    }

}
