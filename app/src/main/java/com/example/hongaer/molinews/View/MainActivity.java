package com.example.hongaer.molinews.View;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.hongaer.molinews.Adapter.MainTabAdapter;
import com.example.hongaer.molinews.Fragment.FirstpageFragment;
import com.example.hongaer.molinews.R;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTablayout;  //顶部标题选项布局
    private ViewPager mViewpager;
    private FirstpageFragment mFragment;

    private List<FirstpageFragment> mFirstFragments;//存放fragment集合
    private String[] mList_title;  //存放标题

    private MainTabAdapter mAdapter_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        initData();
        initView();
        initListener();
    }

    private void initListener() {
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
              //  Log.i("777","ontabselected----------->"+tab.getPosition());
                int position=tab.getPosition();
                for(int i=0;i<mFirstFragments.size();i++){
                    mFirstFragments.get(position).setposition(position);

                }
               // mViewpager.setCurrentItem(position);

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void initData() {
        mList_title= getResources().getStringArray(R.array.tab_title);

       mFirstFragments=new ArrayList<>();


        //通过标题个数来创建fragmen；

        for (int i=0;i<mList_title.length;i++){
            FirstpageFragment first=new FirstpageFragment();
            mFirstFragments.add(first);
        }

    }

    // 初始化布局
        private void initView() {
        mTablayout= (TabLayout) findViewById(R.id.tab_title);
        mViewpager= (ViewPager) findViewById(R.id.view_pager);

        mAdapter_title=new MainTabAdapter(getSupportFragmentManager(),mFirstFragments,mList_title);//设置适配器，即将数据源与适配器绑定
       mViewpager.setAdapter(mAdapter_title);//关联适配器，即将适配器设置到view上

      //  TabLayout 绑定viewpager


       mTablayout.setupWithViewPager(mViewpager);



    }
}
