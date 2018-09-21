package com.example.sean.talkrobot;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Enty.ChatData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv_chat_list;
    private EditText sendText;
    private Button sendMessage;
    private List<ChatData> mList = new ArrayList<>();
    private ChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initView();
    }

    private void initView() {
        lv_chat_list = (ListView) findViewById(R.id.lv_chat_list);
        sendText = (EditText) findViewById(R.id.ed_send);
        sendMessage = (Button) findViewById(R.id.btn_send);

        //设置适配器
        adapter = new ChatListAdapter(this, mList);
        lv_chat_list.setAdapter(adapter);

        //设置发送信息按钮监听
        sendMessage.setOnClickListener(this);

        //设置欢迎语
        addlefttext("你好");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
               final String message = sendText.getText().toString().trim();
                if (!TextUtils.isEmpty(message)) {
                    //点击发送后清空数据框

                    sendText.setText("");
                    addrighttext(message);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String url = "http://www.tuling123.com/openapi/api?" +
                                    "key=" + "c31bb0219cd3488dbf01bb10939384f4" + "&info=" + message;
                            try {
                                OkHttpClient client = new OkHttpClient();
                                Request request = new Request.Builder()
                                        .url(url)
                                        .build();

                                Response response = client.newCall(request).execute();
                                parseJson(response.body().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    //定义URL
                    //图灵机器人接口地址：http://www.tuling123.com/openapi/api
                    //key=后接在图灵官网申请到的apikey
                    //info后接输入的内容

               /*     client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this,"网络请求失败",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            parseJson(response.body().string());
                        }
                    });
                } else {
                    return ;
                }
                break;*/


                }
                break;
        }
    }

    private void parseJson(String response) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            //通过key (text)值获取value
            final String text = jsonObject.getString("text");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    addlefttext(text);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //添加右侧消息
    private void addrighttext(String message) {
        ChatData chatData = new ChatData();
        chatData.setType(ChatListAdapter.chat_right);
        chatData.setText(message);
        mList.add(chatData);
        //通知adapter刷新消息
        adapter.notifyDataSetChanged();
        lv_chat_list.setSelection(lv_chat_list.getBottom());
    }

    //添加左侧消息
    private void addlefttext(String message) {
        ChatData chatData = new ChatData();
        chatData.setType(ChatListAdapter.chat_left);
        chatData.setText(message);
        mList.add(chatData);
        //通知adapter刷新消息
        adapter.notifyDataSetChanged();
        lv_chat_list.setSelection(lv_chat_list.getBottom());
    }
}
