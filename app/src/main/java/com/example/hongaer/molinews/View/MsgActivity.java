package com.example.hongaer.molinews.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.hongaer.molinews.R;

public class MsgActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg);
        webView= (WebView) findViewById(R.id.webview_msg);
        //打开页面，自适应屏幕
        WebSettings webSettings=webView.getSettings();
        webSettings.setUseWideViewPort(true);  //设置屏幕比例任意缩放

       int position=getIntent().getIntExtra("position",0);
        String url=getUrlByPosition(position);

        webView.loadUrl(url);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar_msg);

            //设置返回按键可用
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //设置返回按键触发事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
        });
    }

    private String getUrlByPosition(int position) {
        String oldUrl=getIntent().getStringExtra("url");
        StringBuilder url=new StringBuilder();
        String num=getMainNum(oldUrl,position);
        switch (position){
            //http://imil.ifeng.com/ispecial/40089/index.shtml
            case 0: //新闻
                Log.i("999","i.feng-------------------------->");
                url=url.append("http://inews.ifeng.com/").append(num).append("/news.shtml");
                break;
            case 1://财经
                Log.i("777","i.feng-------------------------->");
                url=url.append("http://istock.ifeng.com/").append(num).append("/news.shtml");

                break;
            case 2://体育
                url=url.append("http://isports.ifeng.com/").append(num).append("/news.shtml");
                break;
            case 3://军事
                url=url.append("http://imil.ifeng.com/").append(num).append("/news.shtml");
                break;
            case 4://科技
                url=url.append("http://itech.ifeng.com/").append(num).append("/news.shtml");
                break;
            case 5://历史
                url=url.append("http://ihistory.ifeng.com/").append(num).append("/news.shtml");

                break;
            case 6://凤凰要闻
                url=url.append("http://ifenghuanghao.ifeng.com/").append(num).append("/news.shtml");
                break;
        }
        return url.toString();
    }
     //截取中间数字的方法
    private String getMainNum(String oldUrl,int index) {
        if(index<6) {
            oldUrl = oldUrl.substring(oldUrl.lastIndexOf("/") + 1, oldUrl.lastIndexOf("."));
            if (oldUrl.contains("_")) {
                oldUrl = oldUrl.substring(0, oldUrl.lastIndexOf("_"));
            }
        }
        else{
            oldUrl = oldUrl.substring(0,oldUrl.lastIndexOf("/"));
            oldUrl = oldUrl.substring(oldUrl.lastIndexOf("/")+1,oldUrl.length());
        }
        return oldUrl;
    }
}
