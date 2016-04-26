package com.xxk.zhbj.model;

import java.util.ArrayList;

/**
 * 实体类，包括：新闻，专题，组图等
 * Created by victorhengli on 2016/4/18.
 */
public class CategoryModel {

    public String retcode;

    public ArrayList<ContentModel> data = new ArrayList();

    public class ContentModel{
         public String id;
         public String title;
         public String type;
         public String url;
         public ArrayList<NewsCategoryModel> children = new ArrayList();
    }

    public class NewsCategoryModel{
        public String id;
        public String title;
        public String type;
        public String url;
    }
}
