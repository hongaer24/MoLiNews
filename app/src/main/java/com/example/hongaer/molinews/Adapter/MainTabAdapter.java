package com.example.hongaer.molinews.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.hongaer.molinews.Fragment.FirstpageFragment;

import java.util.List;

/**
 * tablayout 的适配器
 */

public class MainTabAdapter extends FragmentPagerAdapter {

    private   List<FirstpageFragment> mList_fragment;
    private   String[] mList_title;


    public MainTabAdapter(FragmentManager fm,List<FirstpageFragment> list_fragment,String[] list_title) {
        super(fm);
        mList_fragment= list_fragment;
        mList_title=list_title;
    }

    @Override
    //通过position返回要显示的fragment
    public Fragment getItem(int position) {
        return mList_fragment.get(position);
    }

    @Override
    //返回总滑动的fragment总数
    public int getCount() {
        return mList_fragment.size();

    }
    //根据不同的位置返回不同的标题
    public CharSequence getPageTitle(int position){

       return  mList_title[position];
     }

}
