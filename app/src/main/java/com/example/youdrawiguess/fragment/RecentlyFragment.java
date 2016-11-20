package com.example.youdrawiguess.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.youdrawiguess.R;
import com.example.youdrawiguess.activity.ChatActivity;
import com.example.youdrawiguess.adapter.RecentlyAdapter;
import com.example.youdrawiguess.bean.Coordinate;
import com.example.youdrawiguess.db.MessageDao;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentlyFragment extends Fragment implements OnClickListener {

    private View messageLayout;

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.lv_friends)
    ListView lv_friends;

    private RecentlyAdapter recentlyAdapter;

    private List<String> recentlyFriends;

    private MessageDao messageDao;

    private MyBroadcastReceiver broadcastReceiver;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        messageLayout = inflater.inflate(R.layout.tab_recently_layout,
                container, false);

        ButterKnife.bind(this, messageLayout);

        init();

        return messageLayout;
    }

    @Override
    public void onResume() {
        super.onResume();

        registerBoradcastReceiver();

        messageDao = new MessageDao();
        recentlyFriends = messageDao.get_communication_last();

        recentlyAdapter = new RecentlyAdapter(recentlyFriends, getActivity());
        lv_friends.setAdapter(recentlyAdapter);
        lv_friends.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // 跳转聊天界面
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                String friend = recentlyFriends.get(position);
                intent.putExtra("friend", friend);
                startActivity(intent);
                getActivity().overridePendingTransition(android.R.anim.fade_in,
                        android.R.anim.fade_out);// 实现淡入浅出的效果
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private void init() {
//        lv_friends = (ListView) messageLayout.findViewById(R.id.lv_friends);
//        tv_title = (TextView) messageLayout.findViewById(R.id.tv_title);

        tv_title.setText("最近联系人");
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 注册广播
     */
    private void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.paint");
        broadcastReceiver = new MyBroadcastReceiver();
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 广播接收器
     */
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Coordinate coordinate = (Coordinate) intent.getExtras().get(
                    "coordinate");
            if ("message".equals(coordinate.getCommand())) {
                messageDao = new MessageDao();
                recentlyFriends = messageDao.get_communication_last();

                recentlyAdapter = new RecentlyAdapter(recentlyFriends,
                        getActivity());
                lv_friends.setAdapter(recentlyAdapter);
                lv_friends.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int position, long arg3) {
                        // 跳转聊天界面
                        Intent intent = new Intent(getActivity(),
                                ChatActivity.class);
                        String friend = recentlyFriends.get(position);
                        intent.putExtra("friend", friend);
                        startActivity(intent);
                        getActivity()
                                .overridePendingTransition(
                                        android.R.anim.fade_in,
                                        android.R.anim.fade_out);// 实现淡入浅出的效果
                    }
                });
            }
        }
    }

}
