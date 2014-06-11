package com.Doric.CarBook;

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
import com.Doric.CarBook.settings.SettingsFragment;
import com.Doric.CarBook.car.HotCarShow;

import com.Doric.CarBook.member.*;

import com.Doric.CarBook.push.CarShowUtil;

import com.Doric.CarBook.search.SearchMain;
import com.Doric.CarBook.utility.DatabaseHelper;
import android.view.View;

public class MainActivity extends InstrumentedActivity {

    DatabaseHelper db; // 本地SQLite数据库辅助类
    UserFunctions userFunctions;  //用户功能函数辅助类
    private DrawerLayout drawerLayout;  //用于侧拉布局
    private ListView drawerList;      //左侧侧拉的listView
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence mTitle;  // Activity的标题
    private CharSequence mLeftDrawerTitle; //当前侧拉模块的标题
    private String[] leftDrawerTitles;  //侧拉各模块的标题
    private int[] leftDrawerIcons;   //侧拉各模块的icon

    public static boolean isForeground = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        JPushInterface.init(getApplicationContext());
        registerMessageReceiver();  // 用来接收推送



        db = new DatabaseHelper(getApplicationContext());
        userFunctions = new UserFunctions(getApplicationContext());

        mLeftDrawerTitle = mTitle = getTitle();

        //左侧侧拉标题存于 strings.xml 中的 left_drawer_array 数组中
        leftDrawerTitles = getResources().getStringArray(R.array.left_drawer_array);
        leftDrawerIcons = new int[]{
                R.drawable.pc_default_head,  //个人中心
                R.drawable.ic_hot_car_show,  //热门汽车排行
                R.drawable.ic_search,        //找车
                R.drawable.ic_collection,    //我的收藏
                R.drawable.ic_settings,       //设置
        };
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        //给左侧侧拉的 listView 设置自定义的adapter
        LeftDrawerListAdapter adapter = new LeftDrawerListAdapter(this, leftDrawerTitles, leftDrawerIcons);
        drawerList.setAdapter(adapter);

        //给左侧侧拉的 listView 添加监听器
        drawerList.setOnItemClickListener(new DrawerItemSelectedListener());

        //enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle 保证「左侧侧拉」和「Actionbar上的应用图标」之间的交互
        drawerToggle = new ActionBarDrawerToggle(
                this,                             // 所属的Activity
                drawerLayout,                     // DrawerLayout布局
                R.drawable.ic_navigation_drawer,  // nav drawer image to replace 'Up' caret
                R.string.drawer_open,         // 打开侧拉栏的说明文字
                R.string.drawer_close         // 关闭侧拉栏的说明文字
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();  //提示Actionbar更新，将会调用 onPrepareOptionsMenu() 函数
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(mLeftDrawerTitle);
                invalidateOptionsMenu();  //提示Actionbar更新，将会调用 onPrepareOptionsMenu() 函数
            }
        };
        //将drawerToggle设置为 DrawerLayout 的监听器
        drawerLayout.setDrawerListener(drawerToggle);
        if (savedInstanceState == null) {
            selectItem(1);  //设置默认的显示页面为「热门汽车排行」
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // 当调用invalidateOptionsMenu()函数时onPrepareOptionsMenu()将会被调用
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // 当左侧侧拉栏打开时，如果actionbar上有item,则将其隐藏
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
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectItem(int position) {
        Fragment fragment;  //每个模块的Fragment
        FragmentManager fragmentManager = getFragmentManager();
        Bundle args;        //给每个模块传递的参数
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
                Intent it  =new Intent();
                it.setClass(getApplicationContext(),SearchMain.class);
                startActivity(it);
                break;
            case 3: // [我的收藏]模块
                fragment = new MyCollection();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 4: // 「设置」模块
                fragment = new SettingsFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;

            default:
                break;
        }

        //更新选择的item和标题，然后关闭左侧侧拉栏
        drawerList.setItemChecked(position, true);
        mLeftDrawerTitle = leftDrawerTitles[position];
        getActionBar().setTitle(mLeftDrawerTitle);
        drawerLayout.closeDrawer(drawerList);
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

    //左侧侧拉栏的ListView的监听器
    private class DrawerItemSelectedListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectItem(i);
        }
    }

    /**
     * 用于提高ListView的性能
     *  @link http://www.vogella.com/tutorials/AndroidListView/article.html#adapterperformance_holder
     *  由于 findViewById() 方法较费时间，所以使用此内部类保存ListView中相应的views
     *  可以通过setTag()方法保存views到ViewHolder类中，再通过getTag()方法取得views，避免了重复使用findViewById()
     */
    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    //自定义的左侧侧拉栏的ListView的adapter
    public class LeftDrawerListAdapter extends ArrayAdapter<String> {

        private Context context;
        private String[] values;   //各模块标题
        private int[] icons;       //各模块icon

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

                viewHolder.textView = (TextView) rowView.findViewById(R.id.left_drawer_text);
                viewHolder.imageView = (ImageView) rowView.findViewById(R.id.left_drawer_image);
                rowView.setTag(viewHolder);  //保存views到viewHolder中

            }

            ViewHolder viewHolder = (ViewHolder) rowView.getTag();  //取得viewHolder中的views
            if (position == 0) {  //当模块为个人中心模块时
                rowView.setBackgroundResource(R.drawable.left_drawer_user_bg);  //设置个性化背景

                //判断用户是否登录
                if (userFunctions.isUserLoggedIn()) {
                    String username = userFunctions.getUsername(); //获取用户名
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
        drawerList.invalidateViews();
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
    public   void pushOnResume (){
        JPushInterface.resumePush(getApplicationContext());
    }
    public void pushOnPause(){
        JPushInterface.stopPush(getApplicationContext());
    }
}
