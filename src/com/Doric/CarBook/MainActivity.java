package com.Doric.CarBook;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.*;
import android.widget.*;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;
import com.Doric.CarBook.car.CarShow;
import com.Doric.CarBook.car.HotCarShow;
import com.Doric.CarBook.member.*;
import com.Doric.CarBook.push.CarShowUtil;
import com.Doric.CarBook.search.SearchMain;
import com.Doric.CarBook.utility.DatabaseHelper;
import android.view.View;

public class MainActivity extends InstrumentedActivity {

    // 本地SQLite数据库辅助类
    DatabaseHelper db;
    UserFunctions userFunctions;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence drawerTitle;
    private CharSequence title;
    private String[] leftDrawerTitles;
    public static boolean isForeground = false;
    private int[] leftDrawerIcons;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        JPushInterface.init(getApplicationContext());
        registerMessageReceiver();  // 用来接收推送


        db = new DatabaseHelper(getApplicationContext());
        userFunctions = new UserFunctions(getApplicationContext());


        title = drawerTitle = getTitle();
        leftDrawerTitles = getResources().getStringArray(R.array.left_drawer_array);
        leftDrawerIcons = new int[]{
                R.drawable.pc_default_head,
                R.drawable.ic_hot_car_show,
                R.drawable.ic_search,
                R.drawable.ic_collection,
                R.drawable.ic_collection,
                R.drawable.ic_settings
        };
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        LeftDrawerListAdapter adapter = new LeftDrawerListAdapter(this, leftDrawerTitles, leftDrawerIcons);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new DrawerItemSelectedListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_navigation_drawer,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                setTitle(drawerTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                setTitle(title);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        if (savedInstanceState == null) {
            selectItem(1);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = drawerLayout.isDrawerOpen(drawerList);
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(CharSequence title) {
        this.title = title;
        getActionBar().setTitle(title);
    }

    private void selectItem(int position) {
        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();
        Bundle args;
        switch (position) {
            case 0:  //若用户已登录，则为「个人中心」，否则为「注册登录」
                if (userFunctions.isUserLoggedIn()) {
                    fragment = new PersonalCenter();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                } else {
                    startActivity(new Intent(this, Login.class));
                }
                break;
            case 1:  //「热门汽车排行」模块
                fragment = new HotCarShow();
                args = new Bundle();
                args.putString("tag", "HotCarShow");
                fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 2: //「找车」模块
                startActivity(new Intent(getApplicationContext(), SearchMain.class));
                break;
            case 3: // [我的收藏]模块
                fragment = new UserCollection();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 4: //「汽车展示」测试模块
                Intent intent = new Intent();
                intent.putExtra("carID", "123");
                intent.setClass(MainActivity.this, CarShow.class);
                startActivity(intent);
                break;
            case 5: // [设置]模块
                fragment = new Setting();
                fragmentManager.beginTransaction().replace(R.id.content_frame,fragment).commit();
                break;
            default:
                fragment = new Fragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
        }
        drawerList.setItemChecked(position, true);
        setTitle(leftDrawerTitles[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    private class DrawerItemSelectedListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectItem(i);
        }
    }

    // 从Jpush服务器接收信息
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!CarShowUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                //setCostomMsg(showMsg.toString());
            }
            context.unregisterReceiver(this);
        }
    }
    /*
    private void setCostomMsg(String msg){
        if (null != msgText) {
            msgText.setText(msg);
            msgText.setVisibility(android.view.View.VISIBLE);
        }
    }
    */

    public class LeftDrawerListAdapter extends ArrayAdapter<String> {

        private Context context;
        private String[] values;
        private int[] icons;

        public LeftDrawerListAdapter(Context context, String[] values, int[] icons) {
            super(context, R.layout.drawer_list_item, values);
            this.context = context;
            this.values = values;
            this.icons = icons;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.drawer_list_item, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) (rowView != null ? rowView.findViewById(R.id.left_drawer_text) : null);
                viewHolder.imageView = (ImageView) (rowView != null ? rowView.findViewById(R.id.left_drawer_image) : null);
                rowView.setTag(viewHolder);
            }

            ViewHolder viewHolder = (ViewHolder) rowView.getTag();
            if (position == 0) {
                rowView.setBackgroundResource(R.drawable.left_drawer_user_bg);

                //判断用户是否登录
                if (userFunctions.isUserLoggedIn()) {
                    String username = userFunctions.getUsername();
                    viewHolder.textView.setText(username);
                } else {
                    viewHolder.textView.setText("注册登录");
                }
                viewHolder.textView.setTextColor(Color.WHITE);
                viewHolder.imageView.setImageResource(icons[position]);
            } else {
                viewHolder.textView.setText(values[position]);
                viewHolder.imageView.setImageResource(icons[position]);
            }
            return rowView;
        }
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }
    @Override
    protected void onPause() {
        isForeground = false;

        super.onPause();
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
    public void pushOnResume (){
        JPushInterface.resumePush(getApplicationContext());
    }
    public void pushOnPause(){
        JPushInterface.stopPush(getApplicationContext());
    }
}
