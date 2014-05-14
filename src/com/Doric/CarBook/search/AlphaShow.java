package com.Doric.CarBook.search;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.Doric.CarBook.R;
import android.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AlphaShow extends Fragment {

    public static CarSeable carseable;
    private LinearLayout mLinearLayout;
    private ScrollView mScrollView;
    private ImageLoader imageLoader;
    public static boolean isok =false;
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            //响应每个菜单项(通过菜单项的ID)

            case R.id.action_search:
                //进入搜索界面
                Intent it = new Intent();
                it.setClass(AlphaShow.this, Search.class);
                AlphaShow.this.startActivity(it);
                break;
            case R.id.action_conditionSearch:
                //进入条件搜索界面
                Intent it1 = new Intent();
                it1.setClass(AlphaShow.this, ConditionSearch.class);
                AlphaShow.this.startActivity(it1);
                break;
            default:

                //对没有处理的事件，交给父类来处理
                return super.onOptionsItemSelected(item);
        }
        return true;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //获取车辆品牌信息


        initPage();
        SearchMain.searchmain.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        return mScrollView;

    }

    @Override
    public void onResume() {
        super.onResume();
        SearchMain.searchmain.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public void initPage() {

        //字母分组列表的实现

        mLinearLayout = new LinearLayout(SearchMain.searchmain);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(param1);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setBackgroundColor(Color.rgb(255, 255, 255));

        ArrayList<Pair<String, ArrayList<CarSeable>>> al = PinYinIndex.getIndex_CarSeable(CarSeableData.mCarSeable, SearchMain.searchmain);
        mScrollView = new ScrollView(SearchMain.searchmain);
        mScrollView.setEnabled(true);
        mScrollView.setBackgroundColor(Color.rgb(255, 255, 255));
        ScrollView.LayoutParams param2 = new ScrollView.LayoutParams(ScrollView.
                LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
        mScrollView.setLayoutParams(param2);


        for (Pair<String, ArrayList<CarSeable>> pair : al) {
            TextView text = new TextView(SearchMain.searchmain);
            text.setText(pair.first);
            text.setTextColor(Color.rgb(0, 0, 0));
            text.setBackgroundColor(Color.rgb(255, 255, 255));
            text.setTextSize(20);

            mLinearLayout.addView(text);
            MyListView listview = new MyListView(SearchMain.searchmain);

            SimpleAdapter adapter = new SimpleAdapter(SearchMain.searchmain, getUniformDataSeable(pair.second), R.layout.sea_list_layout,
                    new String[]{"title", "img"},
                    new int[]{R.id.title, R.id.img});
            listview.setDivider(getResources().getDrawable(R.drawable.list_divider));
            listview.setDividerHeight(1);
            listview.setAdapter(adapter);
            //点击车辆
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ListView lv = (ListView) parent;
                    HashMap<String, Object> Info = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter返回Map

                    carseable = CarSeableData.find((String) Info.get("title"));
                    carseable.LoadCarSeries();

                }

            });
            mLinearLayout.addView(listview);
        }


        mScrollView.addView(mLinearLayout);
        //this.setContentView(mScrollView);

        // getActionBar().setTitle("拼音索引");

        //初始化侧拉菜单
        //initSlidingDrawer();

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */

    //打包车辆品牌
    public ArrayList<Map<String, Object>> getUniformDataSeable(ArrayList<CarSeable> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CarSeable cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", cs.getCarSeableName());
            //Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(cs.getPicPath());
            map.put("img", R.drawable.ic_launcher);
            list.add(map);

        }

        return list;

    }


    //private SlidingMenu menu;


    //初始化侧拉菜单
    /*
    private void initSlidingDrawer() {

        ListView listView = (ListView) findViewById(R.id.drawerListView);
        SimpleAdapter adapter = new SimpleAdapter(this, null, R.layout.sea_list_layout,
                new String[]{"title", "img"},
                new int[]{R.id.title, R.id.img});
        listView.setAdapter(adapter);
    }

    */


}








