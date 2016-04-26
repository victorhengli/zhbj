package com.xxk.zhbj.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xxk.zhbj.BaseDetail.BaseDetailContent;
import com.xxk.zhbj.BaseDetail.impl.InteractDetailContent;
import com.xxk.zhbj.BaseDetail.impl.NewsDetailContent;
import com.xxk.zhbj.BaseDetail.impl.PhotoDetailContent;
import com.xxk.zhbj.BaseDetail.impl.TopicDetailContent;
import com.xxk.zhbj.MainActivity;
import com.xxk.zhbj.R;
import com.xxk.zhbj.model.CategoryModel;

import java.util.ArrayList;

/**
 * Created by victorhengli on 2016/4/15.
 */
public class LeftMenuFragment extends BaseFragment {

    @ViewInject(R.id.lv_left_content)
    private ListView mListView;
    private CategoryModel model;
    private ArrayList<CategoryModel.ContentModel> list ;
    private int mPostion = 0;//listview被点击的位置
    private ArrayList<BaseDetailContent> baseDetailContents = new ArrayList<>();

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.left_menu,null);
        ViewUtils.inject(this,view);
        return view;
    }

    @Override
    protected void initData() {

    }

    /**
     * 暴露该方法给外部，用于接收数据
     * @param model
     */
    public void setLeftMenuData(CategoryModel model){
          this.model = model;
          list = this.model.data;
          final LeftMenuAdapter adapter = new LeftMenuAdapter();
          mListView.setAdapter(adapter);
          mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  mPostion = position;
                  adapter.notifyDataSetChanged();
                  MainActivity mainActivity = (MainActivity) mActivity;
                  ContentFragment contentFragment = mainActivity.getContentFragment();
                  contentFragment.getNewsCenterPager().setContentView(position);
                  setSlidingMenuToggle();
              }
          });
    }

    public void setSlidingMenuToggle(){
        MainActivity activity = (MainActivity) mActivity;
        activity.slidingMenu.toggle();
    }

    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CategoryModel.ContentModel getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if(convertView == null){
                view = View.inflate(mActivity,R.layout.left_lv_item,null);
            }else{
                view = convertView;
            }
            TextView tv = (TextView) view.findViewById(R.id.left_lv_tv);
            if(mPostion == position){
                tv.setTextColor(Color.RED);
                tv.setEnabled(true);
            }else{
                tv.setTextColor(Color.WHITE);
                tv.setEnabled(false);
            }
            tv.setText(getItem(position).title);
            return view;
        }
    }
}
