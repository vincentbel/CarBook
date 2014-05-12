package com.Doric.CarBook.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Pair;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.Doric.CarBook.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CarListShow extends MyFragment {

    private LinearLayout mLinearLayout;
    private ScrollView mScrollView;
    private ImageLoader imageLoader;

    private CarSeries carSeries;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //获取车辆品牌信息
        Bundle bundle = getArguments();
        if (bundle == null)
            return null;
        String carBrand = bundle.getString("carBrand");
        String carSerie = bundle.getString("carSerie");
        carSeries = CarSeableData.find(carBrand).findCarSeries(carSerie);
        initPage();
        return mScrollView;

    }


    @Override
    public void onResume(){
        super.onResume();
        SearchMain.searchmain.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public ArrayList<Map<String, Object>> getUniformData(ArrayList<CarInfor> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CarInfor cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", cs.getCarName());
            //Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(cs.getCarPicPath());
            map.put("img", R.drawable.ic_launcher);
            list.add(map);

        }

        return list;

    }

    public void initPage() {
        Context context = getActivity().getApplicationContext();
        if(context==null)return ;
        ArrayList<Pair<String, ArrayList<CarInfor>>> al = PinYinIndex.getIndex_CarInfo(carSeries.getCarList(),context );
        //界面显示
        mLinearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(param1);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setBackgroundColor(Color.rgb(255, 255, 255));

        mScrollView = new ScrollView(context);
        mScrollView.setEnabled(true);
        mScrollView.setBackgroundColor(Color.rgb(255, 255, 255));
        ScrollView.LayoutParams param2 = new ScrollView.LayoutParams(ScrollView.
                LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
        mScrollView.setLayoutParams(param2);
        for (Pair<String, ArrayList<CarInfor>> pair : al) {
            TextView text = new TextView(context);
            text.setText(pair.first);
            text.setTextColor(Color.rgb(0, 0, 0));
            text.setBackgroundColor(Color.rgb(230,230,230));
            text.setTextSize(20);

            mLinearLayout.addView(text);
            MyListView listview = new MyListView(context);

            SimpleAdapter adapter = new SimpleAdapter(context, getUniformData(pair.second), R.layout.sea_list_layout,
                    new String[]{"title", "img"},
                    new int[]{R.id.title, R.id.img});

            listview.setAdapter(adapter);
            listview.setDivider(getResources().getDrawable(R.drawable.list_divider));
            listview.setDividerHeight(1);
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ListView lv = (ListView) parent;
                    HashMap<String, Object> Info = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter杩斿洖Map
                   //
                   //
                    //跳转至车辆展示、
                    //
                    //
                    //
                    Context context = getActivity().getApplicationContext();
                    if(context==null)
                        return ;
                    Toast.makeText(context,(String)Info.get("title"),Toast.LENGTH_LONG).show();

                }

            });
            mLinearLayout.addView(listview);
        }
        mScrollView.addView(mLinearLayout);
        mScrollView.setX(0);
        mScrollView.setY(0);

    }


}




