package com.example.hongaer.molinews.Utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.hongaer.molinews.bean.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
   加载数据获取模块
 */

public class JsonUtils
{
    private List<Data>[] msg_list;

    private CallBackListener mlistener ; //声明一个接口类型变量

    //监听数据类通过此方法注册
    public  JsonUtils(CallBackListener listener)
    {
        this.mlistener = listener;//实现类
    }


    public void getResult() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                //1.创建一个okhttp对象
                OkHttpClient client = new OkHttpClient();

                //2.新建一个请求
                Request request = new Request.Builder().url("http://news.ifeng.com/").build();

                //3.执行请求，获得响应数据
                Call call = client.newCall(request);

                //4.加入调度，执行回调
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //Log.i("json","onresponse"+response.body().string());
                        if (response.isSuccessful()) {
                            String str = response.body().string();
                            Message msg = new Message();
                            msg.obj = str;
                            msg.arg1 = 0x1;
                            mhandler.sendMessage(msg);
                        }
                    }
                });
            }
        }).start();
    }

    private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 == 0x1) {
                getJson(msg.obj.toString());//处理返回的字符串
            }
        }
    };

    //截取返回的数据的json格式
    public void getJson(String msg) {
        String json = null;
        if (msg != null) {
            json =msg.substring(msg.indexOf("[[{"), msg.indexOf("}]]") + 3);
            Log.d("666", "get------------->json"+ json);
        }
        initMessageList(json);
    }
     //解析json字符串获得有效数据
    public void initMessageList(String json) {
        try {
            JSONArray array = new JSONArray(json);
            msg_list = new ArrayList[array.length()];

            for (int i = 0; i < array.length(); i++) {
                JSONArray arr = array.getJSONArray(i);
                msg_list[i] = new ArrayList<>();
                for (int j = 0; j < arr.length(); j++) {
                    JSONObject obj = arr.getJSONObject(j);
                    Data data = new Data();
                    data.setTitle(obj.getString("title"));
                    data.setUrl(obj.getString("url"));
                    data.setThumbnail(obj.getString("thumbnail"));
                    msg_list[i].add(data);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //通知注册进来的类更新数据
        mlistener.upData(msg_list);
    }

     //监听数据加载类的父类接口（即firstpagefragment是监听数据者）
    public interface CallBackListener {
        void upData(List<Data>[] msg_list);
    }

}
