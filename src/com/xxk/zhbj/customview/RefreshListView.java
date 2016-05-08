package com.xxk.zhbj.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.*;
import com.xxk.zhbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义下拉刷新，上拉加载更多
 * Created by victorhengli on 2016/4/25.
 */
public class RefreshListView extends ListView {

    private static final int PULL_TO_REFRESH = 0;//下拉刷新
    private static final int RELEASE_TO_REFRESH = 1;//松开刷新
    private static final int REFRESHING = 2;//正在刷新
    private int currentState = -1;
    private View mHeadView;
    private int startY = -1;
    private int dy = -1;
    private int disY;
    private int headHeight;
    private ImageView imageView;
    private ProgressBar pb;
    private TextView pullTextView;
    private TextView date;
    private RotateAnimation upAnim;
    private RotateAnimation downAnim;
    private View mFooterView;
    private int mFooterHeight;

    public RefreshListView(Context context) {
        super(context);
        initView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView(){
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化listview头布局
     */
    private void initHeaderView(){
        mHeadView = View.inflate(getContext(), R.layout.news_detail_listview_pull_item,null);
        addHeaderView(mHeadView);
        imageView = (ImageView) mHeadView.findViewById(R.id.iv_pull);
        pb = (ProgressBar) findViewById(R.id.pb_pull);
        pullTextView = (TextView) findViewById(R.id.tv_pull);
        date = (TextView) findViewById(R.id.tv_pull_date);
        //初始化时，先将pullview隐藏
        mHeadView.measure(0, 0);
        headHeight = mHeadView.getMeasuredHeight();
        mHeadView.setPadding(0, -headHeight, 0, 0);
        initAnim();
    }

    boolean isLodingMore = false;//是否正在加载标识，防止重复加载数据
    private void initFooterView(){
        mFooterView = View.inflate(getContext(),R.layout.news_detail_listview_push_item,null);
        addFooterView(mFooterView);
        mFooterView.measure(0,0);
        mFooterHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0,-mFooterHeight,0,0);
        //为listview设置滑动监听事件
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //如果listview处于滑动暂停状态或者快速滑动状态时，进行捕获
                if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
                    if (getLastVisiblePosition() == getCount() - 1 && !isLodingMore) { //如果是显示最后一条数据
                        System.out.println("到底了");
                        mFooterView.setPadding(0, 0, 0, 0);
                        setSelection(getCount());//使加载更多能够显示出来
                        isLodingMore = true;
                        if(listener != null){
                            listener.pushToRefresh();//调用监听的抽象方法，让调用者实现
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 1/首先根据滑动距离判断状态
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            //todo 当前显示是第一条时这种情况需要考虑
            case MotionEvent.ACTION_DOWN://按下
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE://移动
                 if(startY == -1){
                     startY = (int) ev.getRawY();
                 }
                 //如果当前状态为正在刷新，则不做任何处理
                 if(currentState == REFRESHING){
                     break;
                 }
                 int currentY = (int) ev.getRawY();
                 dy = currentY - startY;
                 if(dy > 0 && getFirstVisiblePosition() == 0){//只有在下拉的情况下，并且当前显示item为第一个才做处理
                     //与headerview的height进行比较

                     disY = dy - headHeight;
                     mHeadView.setPadding(0,disY,0,0);
                     //判断是上拉还是下拉,如果disY大于0，表示下拉刷新状态，
                     if(disY > 0 && currentState != RELEASE_TO_REFRESH){//松开刷新
                         currentState = RELEASE_TO_REFRESH;
                         refreshState();
                     }else if(disY<0 && currentState != PULL_TO_REFRESH){//下拉
                         currentState = PULL_TO_REFRESH;
                         refreshState();
                     }
                 }
                break;
            case MotionEvent.ACTION_UP://抬起
                startY = -1;
                //抬起时，判断disY与padding，当disY大于padding时，当前状态改为正在刷新，反之，收起headerview
                if(disY > 0 && currentState != REFRESHING){
                    currentState = REFRESHING;
                    refreshState();
                    mHeadView.setPadding(0,0, 0, 0);
                }else{
                    mHeadView.setPadding(0, -headHeight, 0, 0);
                }
                dy = -1;
                disY = -1;
                currentState = PULL_TO_REFRESH;
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshState(){
        switch (currentState){
            case PULL_TO_REFRESH://下拉
                imageView.setVisibility(View.VISIBLE);
                imageView.startAnimation(upAnim);
                pb.setVisibility(View.INVISIBLE);
                pullTextView.setText("下拉刷新");
                break;
            case RELEASE_TO_REFRESH://松开刷新
                imageView.setVisibility(View.VISIBLE);
                imageView.startAnimation(downAnim);
                pb.setVisibility(View.INVISIBLE);
                pullTextView.setText("松开刷新");
                break;
            case REFRESHING:
                imageView.clearAnimation();
                imageView.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);
                pullTextView.setText("正在刷新....");
                if(listener != null){
                    listener.pullToRefresh();
                }
                break;
        }
    }

    private void initAnim(){

        upAnim = new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        upAnim.setDuration(200);
        upAnim.setFillAfter(true);

        downAnim = new RotateAnimation(180,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        downAnim.setDuration(200);
        downAnim.setFillAfter(true);
    }

    /**
     * 自定义下拉，上拉刷新，让外部去实现
     */
    OnRefreshListener listener;

    public interface OnRefreshListener{
        public void pullToRefresh();
        public void pushToRefresh();
    }

    public void setOnRefreshListener(OnRefreshListener listener){
        this.listener = listener;
    }

    /**
     * 将下拉刷新头布局收起
     */
    public void onRefreshComplete(){
        if(isLodingMore){//将脚布局收起
            isLodingMore = false;
            mFooterView.setPadding(0,-mFooterHeight,0,0);
        }else{
            imageView.setVisibility(View.VISIBLE);
            pb.setVisibility(View.INVISIBLE);
            pullTextView.setText("下拉刷新");
            mHeadView.setPadding(0, -headHeight, 0, 0);
            date.setText("最后刷新时间"+getCurrent());
        }
    }

    private String getCurrent(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

    @Override
    public void setOnItemClickListener(final OnItemClickListener listener) {
        super.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemClick(parent,view,position - getHeaderViewsCount(),id);
            }
        });
    }
}
