package com.example.hongaer.molinews.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hongaer.molinews.R;
import com.example.hongaer.molinews.bean.BannerBean;
import com.example.hongaer.molinews.bean.Data;
import com.facebook.drawee.view.SimpleDraweeView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.List;

/**
 * Created by hongaer on 2017/5/18.
 */

public class FirstPageAdapter extends RecyclerView.Adapter{
    private final int TYPE_HEAD=0; //表示首个位置，显示轮播视图
    private final int TYPE_NORMAL=1; //表示下面的itemview
    private final Context mcontext;
    private List<String> banner_url; //轮播图片路径
    private final int TYPE_FOOT=2;//表示底部刷新布局


    private List<Data> item_data;//item视图数据集合
    private BannerBean bean;




    //这个构造函数的用于把要展示的数据传进来
    public FirstPageAdapter(Context context, List<String> banner_url){
        this.mcontext=context;
        this.banner_url=banner_url;
    }
    public FirstPageAdapter(Context context, List<Data> item_data, BannerBean bean){
           this.mcontext=context;
           this.item_data=item_data;
           this.bean=bean;
    }
    //这个方法用于创建viewholder的实例
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder=null;
        if(viewType==TYPE_HEAD){
          //此处创建顶部banner的viewholder
            holder=new BannerViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.itemheader_banner,parent,false));
           //把最外层布局加载然后传到构造函数BannerViewHolder（）； 此句语句等同于：
//            View view=LayoutInflater.from(mcontext).inflate(R.layout.itemheader_banner,parent,false);
//            RecyclerView.ViewHolder holder=new BannerViewHolder(view) ;

        }else if(viewType==TYPE_NORMAL) {

            holder=new ItemViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.item_firstfragment,parent,false));
        }else if (viewType==TYPE_FOOT){

            holder=new FootViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.itemfooter,parent,false));
        }
        return holder;
    }

    @Override
    //把数据绑定到viewholder
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //判断holder是不是BannerViewHolder
        if (holder instanceof BannerViewHolder) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;  // 把指向子类对象的父类引用holder赋给子类的引用 bannerViewHolder，
            //属于向下转换，需要强制转换类型

            bannerViewHolder.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
            bannerViewHolder.banner.setBannerTitle(bean.getTitle());
            bannerViewHolder.banner.setImages(bean.getImg_url());
            // Log.i("tag","--------------------------onbindview");
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.simpleView.setImageURI(item_data.get(position - 1).getThumbnail());
            itemViewHolder.textView.setText(item_data.get(position - 1).getTitle());

            if (listenter != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listenter.onclick(v, position);  //给接口赋值（通过接口实现方法来赋值）
                    }
                });
            }
        } else if (holder instanceof FootViewHolder) {
            if (item_data.size() > 0) {
                ((FootViewHolder) holder).progress_lin.setVisibility(View.VISIBLE);//当有数据的时候开始显示上拉刷新
            }
        }


    }

    @Override
    //返回数据源长度
    public int getItemCount() {
        return item_data.size()+1+1;
        //item_data.size()+1+1;
    }

    //告诉创建什么类型viewholder
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_HEAD;

        }else if(position+1==getItemCount()){

            return TYPE_FOOT;
        }
        else {
            return  TYPE_NORMAL;
        }
    }
         //正常的item布局管理 内部类
    class ItemViewHolder extends RecyclerView.ViewHolder{
             SimpleDraweeView simpleView;
             TextView textView;
        public ItemViewHolder(View itemView) {
            super(itemView);
            simpleView= (SimpleDraweeView) itemView.findViewById(R.id.simpleView);
            textView= (TextView) itemView.findViewById(R.id.news_text);
        }
    }
         // 首位的banner
    class BannerViewHolder extends  RecyclerView.ViewHolder{
             Banner banner;
        public BannerViewHolder(View itemView) {
            super(itemView);
            banner= (Banner) itemView.findViewById(R.id.banner);
            //Log.i("tag","------------------66666");
        }
    }
    //底部刷新视图
    class FootViewHolder extends  RecyclerView.ViewHolder {

        LinearLayout  progress_lin;
        public FootViewHolder(View itemView) {
            super(itemView);
            progress_lin= (LinearLayout) itemView.findViewById(R.id.progress_lin);
        }
    }
    //recyview单击事件回调接口
    public interface MyItemClickListenter{
          void onclick (View itemView,int position);

    }
    //本来中保存一个接口的引用
    private MyItemClickListenter listenter;

     //接口类型初始化,监听类注册
    public void setMyItemClickListener(MyItemClickListenter listenter){
        this.listenter=listenter;
    }
}
