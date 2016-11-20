package com.example.youdrawiguess.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youdrawiguess.MyApplication;
import com.example.youdrawiguess.R;
import com.example.youdrawiguess.bean.Coordinate;
import com.example.youdrawiguess.bean.Coordinate.Type;
import com.example.youdrawiguess.db.MessageDao;
import com.example.youdrawiguess.fragment.CircleFragment;
import com.example.youdrawiguess.fragment.FriendsFragment;
import com.example.youdrawiguess.fragment.MyFragment;
import com.example.youdrawiguess.fragment.RecentlyFragment;
import com.example.youdrawiguess.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentActivity extends Activity implements OnClickListener {

    // 四个Fragment
    private RecentlyFragment recentlyFragment;// 最近联系人
    private FriendsFragment friendsFragment;// 好友Fragment
    private CircleFragment circleFragment;// 朋友圈Fragment
    private MyFragment myFragment;// 我的Fragment

    // 四个Tab，每个Tab包含一个按钮
    @BindView(R.id.ll_tab_home)
    LinearLayout ll_tab_recently;
    @BindView(R.id.ll_tab_friend)
    LinearLayout ll_tab_friend;
    @BindView(R.id.ll_tab_circle)
    LinearLayout ll_tab_circle;
    @BindView(R.id.ll_tab_my)
    LinearLayout ll_tab_my;

    @BindView(R.id.ib_tab_recently_img)
    ImageButton mRecentlyImg;
    @BindView(R.id.ib_tab_friends_img)
    ImageButton mFriendsImg;
    @BindView(R.id.ib_tab_circle_img)
    ImageButton mCircleImg;
    @BindView(R.id.ib_tab_my_img)
    ImageButton mMyImg;

    @BindView(R.id.tv_unread_messages)
    TextView tv_unread_messages;
    @BindView(R.id.tv_unread)
    TextView tv_unread;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_main);

        ButterKnife.bind(this);

        MyApplication.addActivity(this);

        ToastUtil.showToast(this, "登陆成功", Toast.LENGTH_LONG);

        // 初始化布局元素
        fragmentManager = getFragmentManager();
        init();

        registerBoradcastReceiver();

        // 第一次启动时选中第0个tab
        setTabSelection(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        getUnreadMessages();
        getUnread();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        getUnreadMessages();
    }

    @Override
    public void onClick(View v) {
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (v.getId()) {
            case R.id.ll_tab_home:
                setTabSelection(0);
                break;
            case R.id.ll_tab_friend:
                setTabSelection(1);
                break;
            case R.id.ll_tab_circle:
                setTabSelection(2);
                break;
            case R.id.ll_tab_my:
                setTabSelection(3);
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void init() {
//        ll_tab_recently = (LinearLayout) findViewById(R.id.ll_tab_home);
//        ll_tab_friend = (LinearLayout) findViewById(R.id.ll_tab_friend);
//        ll_tab_circle = (LinearLayout) findViewById(R.id.ll_tab_circle);
//        ll_tab_my = (LinearLayout) findViewById(R.id.ll_tab_my);
//
//        mRecentlyImg = (ImageButton) findViewById(R.id.ib_tab_recently_img);
//        mFriendsImg = (ImageButton) findViewById(R.id.ib_tab_friends_img);
//        mCircleImg = (ImageButton) findViewById(R.id.ib_tab_circle_img);
//        mMyImg = (ImageButton) findViewById(R.id.ib_tab_my_img);
//
//        tv_unread_messages = (TextView) findViewById(R.id.tv_unread_messages);
//        tv_unread = (TextView) findViewById(R.id.tv_unread);

        ll_tab_recently.setOnClickListener(this);
        ll_tab_friend.setOnClickListener(this);
        ll_tab_circle.setOnClickListener(this);
        ll_tab_my.setOnClickListener(this);
    }

    public void setTabSelection(int index) {
        resetImg();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        switch (index) {
            case 0:
                hideFragments(transaction);
                mRecentlyImg.setImageResource(R.drawable.tab_chat_pressed);
                if (recentlyFragment == null) {
                    // 如果MessageFragment为空，则创建一个并添加到界面上
                    recentlyFragment = new RecentlyFragment();
                    transaction.add(R.id.content, recentlyFragment);
                } else {
                    // 如果MessageFragment不为空，则直接将它显示出来
                    transaction.show(recentlyFragment);
                }
                break;
            case 1:
                mFriendsImg.setImageResource(R.drawable.tab_excoo_pressed);
                if (friendsFragment == null) {
                    // 如果NewsFragment为空，则创建一个并添加到界面上
                    friendsFragment = new FriendsFragment();
                    transaction.add(R.id.content, friendsFragment);
                } else {
                    // 如果NewsFragment不为空，则直接将它显示出来
                    transaction.show(friendsFragment);
                }
                break;
            case 2:
                mCircleImg.setImageResource(R.drawable.tab_home_pressed);
                if (circleFragment == null) {
                    // 如果ContactsFragment为空，则创建一个并添加到界面上
                    circleFragment = new CircleFragment();
                    transaction.add(R.id.content, circleFragment);
                } else {
                    // 如果ContactsFragment不为空，则直接将它显示出来
                    transaction.show(circleFragment);
                }
                break;
            case 3:
            default:
                mMyImg.setImageResource(R.drawable.tab_my_pressed);
                if (myFragment == null) {
                    // 如果SettingFragment为空，则创建一个并添加到界面上
                    myFragment = new MyFragment();
                    transaction.add(R.id.content, myFragment);
                } else {
                    // 如果SettingFragment不为空，则直接将它显示出来
                    transaction.show(myFragment);
                }
                break;
        }
        transaction.commit();
    }

    /**
     * 把所有图片变暗
     */
    private void resetImg() {
        mRecentlyImg.setImageResource(R.drawable.tab_chat_normal);
        mFriendsImg.setImageResource(R.drawable.tab_excoo_normal);
        mCircleImg.setImageResource(R.drawable.tab_home_normal);
        mMyImg.setImageResource(R.drawable.tab_my_normal);
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     */
    public void hideFragments(FragmentTransaction transaction) {
        if (recentlyFragment != null) {
            transaction.hide(recentlyFragment);
        }
        if (circleFragment != null) {
            transaction.hide(circleFragment);
        }
        if (friendsFragment != null) {
            transaction.hide(friendsFragment);
        }
        if (myFragment != null) {
            transaction.hide(myFragment);
        }
    }

    /**
     * 注册广播
     */
    private void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.paint");
        MyBroadcastReceiver broadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * 广播接收器
     */
    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Coordinate message = (Coordinate) intent.getExtras().get(
                    "coordinate");
            message.setType(Type.INPUT);
            getUnreadMessages();
            getUnread();
        }
    }

    /**
     * 查询未读消息个数
     */
    private void getUnreadMessages() {
        MessageDao messageDao = new MessageDao();
        int count = messageDao.get_unread_message_count();
        if (count == 0) {
            tv_unread_messages.setVisibility(View.INVISIBLE);
        } else {
            tv_unread_messages.setText("" + count);
            tv_unread_messages.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 朋友圈未读
     */
    private void getUnread() {
        tv_unread.setVisibility(View.INVISIBLE);
    }

    /**
     * 返回键后台运行
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
