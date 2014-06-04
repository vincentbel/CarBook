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
import com.Doric.CarBook.Settings.SettingsFragment;
import com.Doric.CarBook.car.CarShow;
import com.Doric.CarBook.car.HotCarShow;
import com.Doric.CarBook.member.*;
import com.Doric.CarBook.push.CarShowUtil;
import com.Doric.CarBook.search.SearchMain;
import com.Doric.CarBook.utility.DatabaseHelper;
import android.view.View;

public class MainActivity extends InstrumentedActivity {

    DatabaseHelper db; // ����SQLite���ݿ⸨����
    UserFunctions userFunctions;  //�û����ܺ���������
    private DrawerLayout drawerLayout;  //���ڲ�������
    private ListView drawerList;      //��������listView
    private ActionBarDrawerToggle drawerToggle;
    private CharSequence mTitle;  // Activity�ı���
    private CharSequence mLeftDrawerTitle; //��ǰ����ģ��ı���
    private String[] leftDrawerTitles;  //������ģ��ı���
    private int[] leftDrawerIcons;   //������ģ���icon

    public static boolean isForeground = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        JPushInterface.init(getApplicationContext());
        registerMessageReceiver();  // ������������


        db = new DatabaseHelper(getApplicationContext());
        userFunctions = new UserFunctions(getApplicationContext());

        mLeftDrawerTitle = mTitle = getTitle();

        //������������� strings.xml �е� left_drawer_array ������
        leftDrawerTitles = getResources().getStringArray(R.array.left_drawer_array);
        leftDrawerIcons = new int[]{
                R.drawable.pc_default_head,  //��������
                R.drawable.ic_hot_car_show,  //������������
                R.drawable.ic_search,        //�ҳ�
                R.drawable.ic_collection,    //�ҵ��ղ�
                R.drawable.ic_settings,       //����
                R.drawable.ic_collection    //����չʾ�������á�
        };
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        //���������� listView �����Զ����adapter
        LeftDrawerListAdapter adapter = new LeftDrawerListAdapter(this, leftDrawerTitles, leftDrawerIcons);
        drawerList.setAdapter(adapter);

        //���������� listView ��Ӽ�����
        drawerList.setOnItemClickListener(new DrawerItemSelectedListener());

        //enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ��֤�����������͡�Actionbar�ϵ�Ӧ��ͼ�꡹֮��Ľ���
        drawerToggle = new ActionBarDrawerToggle(
                this,                             // ������Activity
                drawerLayout,                     // DrawerLayout����
                R.drawable.ic_navigation_drawer,  // nav drawer image to replace 'Up' caret
                R.string.drawer_open,         // �򿪲�������˵������
                R.string.drawer_close         // �رղ�������˵������
        ) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();  //��ʾActionbar���£�������� onPrepareOptionsMenu() ����
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(mLeftDrawerTitle);
                invalidateOptionsMenu();  //��ʾActionbar���£�������� onPrepareOptionsMenu() ����
            }
        };
        //��drawerToggle����Ϊ DrawerLayout �ļ�����
        drawerLayout.setDrawerListener(drawerToggle);
        if (savedInstanceState == null) {
            selectItem(1);  //����Ĭ�ϵ���ʾҳ��Ϊ�������������С�
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // ������invalidateOptionsMenu()����ʱonPrepareOptionsMenu()���ᱻ����
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // ������������ʱ�����actionbar����item,��������
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
        Fragment fragment;  //ÿ��ģ���Fragment
        FragmentManager fragmentManager = getFragmentManager();
        Bundle args;        //��ÿ��ģ�鴫�ݵĲ���
        switch (position) {
            case 0:  //���û��ѵ�¼����Ϊ���������ġ�������Ϊ��ע���¼��
                if (userFunctions.isUserLoggedIn()) {
                    fragment = new PersonalCenter();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                } else {
                    startActivity(new Intent(this, Login.class));
                }
                break;
            case 1:  //�������������С�ģ��
                fragment = new HotCarShow();
                args = new Bundle();
                args.putString("tag", "HotCarShow");
                fragment.setArguments(args);
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 2: //���ҳ���ģ��
                startActivity(new Intent(getApplicationContext(), SearchMain.class));
                break;
            case 3: // [�ҵ��ղ�]ģ��
                fragment = new UserCollection();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 4: // �����á�ģ��
                fragment = new SettingsFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            case 5: //������չʾ������ģ��
                Intent intent = new Intent();
                intent.putExtra("carID", "123");
                intent.setClass(MainActivity.this, CarShow.class);
                startActivity(intent);
                break;
            default:
                break;
        }

        //����ѡ���item�ͱ��⣬Ȼ��ر���������
        drawerList.setItemChecked(position, true);
        mLeftDrawerTitle = leftDrawerTitles[position];
        getActionBar().setTitle(mLeftDrawerTitle);
        drawerLayout.closeDrawer(drawerList);
    }

    // ��Jpush������������Ϣ
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

    //����������ListView�ļ�����
    private class DrawerItemSelectedListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectItem(i);
        }
    }

    /**
     * �������ListView������
     *  @link http://www.vogella.com/tutorials/AndroidListView/article.html#adapterperformance_holder
     *  ���� findViewById() �����Ϸ�ʱ�䣬����ʹ�ô��ڲ��ౣ��ListView����Ӧ��views
     *  ����ͨ��setTag()��������views��ViewHolder���У���ͨ��getTag()����ȡ��views���������ظ�ʹ��findViewById()
     */
    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    //�Զ��������������ListView��adapter
    public class LeftDrawerListAdapter extends ArrayAdapter<String> {

        private Context context;
        private String[] values;   //��ģ�����
        private int[] icons;       //��ģ��icon

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
                rowView.setTag(viewHolder);  //����views��viewHolder��

            }

            ViewHolder viewHolder = (ViewHolder) rowView.getTag();  //ȡ��viewHolder�е�views
            if (position == 0) {  //��ģ��Ϊ��������ģ��ʱ
                rowView.setBackgroundResource(R.drawable.left_drawer_user_bg);  //���ø��Ի�����

                //�ж��û��Ƿ��¼
                if (userFunctions.isUserLoggedIn()) {
                    String username = userFunctions.getUsername(); //��ȡ�û���
                    viewHolder.textView.setText(username);
                } else {
                    viewHolder.textView.setText("ע���¼");
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
        JPushInterface.onResume(this);
        super.onResume();
    }
    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
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
