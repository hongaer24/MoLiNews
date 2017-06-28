package com.example.hongaer.molinews.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hongaer.molinews.Adapter.FirstPageAdapter;
import com.example.hongaer.molinews.R;
import com.example.hongaer.molinews.Utils.JsonUtils;
import com.example.hongaer.molinews.View.MsgActivity;
import com.example.hongaer.molinews.bean.BannerBean;
import com.example.hongaer.molinews.bean.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hongaer on 2017/5/15.
 */

public class FirstpageFragment extends Fragment implements JsonUtils.CallBackListener,SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private FirstPageAdapter adapter;
    private List<String> banner_url;
    private JsonUtils jsonUtils;

    private List<Data>  mData_item=new ArrayList();
    private List<Data> [] msg_list;


    private final int LOADNUM=4;

    private List<Data> item_list;    //item视图数据集合
    private int mPosition;          //记录当前页位置
    private BannerBean mBannerBean; //存储轮播图数据对象
    private int now_num=7;      // 记录当前存放数据条数

    private SwipeRefreshLayout swipe;
    private int lastVisibleItem;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                    View v=getActivity().getLayoutInflater().inflate(R.layout.fragment_layout,
                                 (ViewGroup)getActivity().findViewById(R.id.view_pager),false);
          getData();


        mRecyclerView= (RecyclerView) v.findViewById(R.id.recycle_view);

        adapter=new FirstPageAdapter(getActivity(),item_list,mBannerBean);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);

        swipe= (SwipeRefreshLayout) v.findViewById(R.id.swipe);
        swipe.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light);
        swipe.setOnRefreshListener(this); // 注册本类监听，下拉后通知本类进行更新数据
      // new JsonUtils().getResult();
         initListener();
        return v;
    }
     //给上拉实现事件监听
    private void initListener() {
           mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
               @Override
               //状态发生变化是触发
               public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                   super.onScrollStateChanged(recyclerView, newState);
                   if(newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItem+1==adapter.getItemCount()){
                      mhandler.postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              now_num+=LOADNUM;
                              initData();
                          }
                      },1500);
                   }
               }
               @Override//滚动时监听
               public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                   super.onScrolled(recyclerView, dx, dy);
                   LinearLayoutManager Lm= (LinearLayoutManager) recyclerView.getLayoutManager();
                   lastVisibleItem=Lm.findLastVisibleItemPosition();
               }
             });
              adapter.setMyItemClickListener(new FirstPageAdapter.MyItemClickListenter() {
                  @Override
                  public void onclick(View v, int position) {
                      String url= item_list.get(position-1).getUrl();
                      Intent intent=new Intent(getActivity(), MsgActivity.class);
                      intent.putExtra("url",url);
                      intent.putExtra("position",mPosition);
                      startActivity(intent);
                      Log.i("888","onclick----------->position"+position);// 在这里获取数据，进行处理
                  }
              });
    }
      private void getData() {
          //轮播视图和item数据集合初始化
          item_list=new ArrayList<>();
          mBannerBean=new BannerBean();
           // 注册本类去监听数据加载变化
          jsonUtils =new JsonUtils(FirstpageFragment.this);//this表示 firstfragment的对象；当前类要监听数据更新的所在类；

          //加载数据
          jsonUtils.getResult();

    }

    @Override
    // 回调的更新方法
       public void upData(List<Data> [] msg_list) {
        this.msg_list = msg_list;
         initData();
        // adapter.notifyDataSetChanged();
    }
      //更新数据，分配数据，填充界面
      public void initData(){
        if(msg_list!=null){

            String[] img=new String[3];
            String[] title=new String[3];
            String[] toUrl=new String[3];
            item_list.clear();//清空缓存
            List<Data> data=msg_list[mPosition];
            for (int i=0;i<3;i++){
                img[i]=data.get(i).getThumbnail();
                title[i]=data.get(i).getTitle();
                toUrl[i]=data.get(i).getUrl();
            }
            mBannerBean.setTitle(title);
            mBannerBean.setImg_url(img);
            mBannerBean.setTourl(toUrl);
            for(int i=3;i<now_num;i++){
                item_list.add(data.get(i));
            }
            adapter.notifyDataSetChanged();
        }
}

    @Override//下拉刷新回调方法
    public void onRefresh() {
        now_num+=LOADNUM;//刷新后多显示几条数据
        initData();
        swipe.setRefreshing(false);


    }
    private Handler mhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

     //滑动时更新数据
    public void setposition(int position) {
          mPosition=position;
          initData();
    }
}
