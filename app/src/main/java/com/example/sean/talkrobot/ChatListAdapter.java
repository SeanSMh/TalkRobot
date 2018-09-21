package com.example.sean.talkrobot;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Enty.ChatData;

public class ChatListAdapter extends BaseAdapter {

    private List<ChatData> mList = new ArrayList<>();
    private ChatData chatData;
    private Context context;
    private LayoutInflater inflater;

    //定以常量，区分收发消息
    public static final int chat_left = 1; //接收消息
    public static final int chat_right = 2; //发送消息

    //构造器
    public ChatListAdapter(Context mContext, List<ChatData> mList) {
        this.mList = mList;
        this.context = mContext;
        inflater = (LayoutInflater) mContext.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        ViewHolderLeft viewHolderLeft = null;
        ViewHolderRight viewHolderRight = null;

        int type = getItemViewType(i);

        if(view == null) {
            //加载布局
            //判断左右信息，即是收到还是发出
            switch (type) {
                case chat_left:
                    viewHolderLeft = new ViewHolderLeft();
                    view = inflater.inflate(R.layout.left_item,null);
                    viewHolderLeft.textView_left = (TextView) view.findViewById(R.id.tv_left_text);
                    view.setTag(viewHolderLeft);
                    break;
                case chat_right:
                    viewHolderRight = new ViewHolderRight();
                    view = inflater.inflate(R.layout.right_item,null);
                    viewHolderRight.textView_right = (TextView) view.findViewById(R.id.tv_right_text);
                    view.setTag(viewHolderRight);
                    break;

            }
        } else {
            //判断左右信息，即是收到还是发出
            switch (type) {
                case chat_left:
                    viewHolderLeft = (ViewHolderLeft) view.getTag();
                    break;
                case chat_right:
                    viewHolderRight = (ViewHolderRight) view.getTag();
                    break;
            }
        }

        //赋值
         chatData = mList.get(i);
        //判断左右消息，即是收到还是发出
        switch (type) {
            case chat_left:
                viewHolderLeft.textView_left.setText(chatData.getText());
                break;
            case chat_right:
                viewHolderRight.textView_right.setText(chatData.getText());
                break;
        }
        return view;
    }

    //获取当前item类型
        @Override
    public int getItemViewType(int position) {
        ChatData chatData = mList.get(position);
        int type = chatData.getType();
        return type;
        }

    //左边消息缓存
    class ViewHolderLeft{
        private TextView textView_left;
    }

    //右边消息缓存
    class ViewHolderRight{
        private TextView textView_right;
    }

    //返回所有layout数据
    @Override
    public int getViewTypeCount(){
        return 3;
    }
}
