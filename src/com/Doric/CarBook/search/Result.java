package com.Doric.CarBook.search;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.Doric.CarBook.R;
import com.Doric.CarBook.car.CarInfor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Result extends Activity {

    private ArrayList<CarInfor> mCarList;
    private LinearLayout mLinearLayout;
    private ScrollView mScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent it = this.getIntent();
        String seable = it.getStringExtra("seablename");
        int low = it.getIntExtra("pricelow", -1);
        if (low == -1) return;
        int hig = it.getIntExtra("pricehig", -1);
        if (hig == -1) return;
        Grade g = (Grade)it.getSerializableExtra("grade");
        mCarList = PinyinSearch.conditionSearch(seable, g, low, hig);
        Collections.sort(mCarList, new ComparatorCarInfo());

        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(param1);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setBackgroundColor(Color.rgb(0, 0, 0));
        ArrayList<Pair<String, ArrayList<CarInfor>>> al = PinYinIndex.getIndex_CarInfo(mCarList, this);

        mScrollView = new ScrollView(this);
        mScrollView.setEnabled(true);
        mScrollView.setBackgroundColor(Color.rgb(0, 0, 0));
        ScrollView.LayoutParams param2 = new ScrollView.LayoutParams(ScrollView.
                LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
        mScrollView.setLayoutParams(param2);


        for (Pair<String, ArrayList<CarInfor>> pair : al) {
            TextView text = new TextView(this);
            text.setText(pair.first);
            text.setTextColor(Color.rgb(255, 255, 255));
            text.setBackgroundColor(Color.rgb(230,230,230));
            text.setTextSize(20);

            mLinearLayout.addView(text);
            MyListView listview = new MyListView(this);
            listview.setDivider(getResources().getDrawable(R.drawable.list_divider));
            listview.setDividerHeight(1);
            SimpleAdapter adapter = new SimpleAdapter(this, getUniformData(pair.second), R.layout.sea_list_layout,
                    new String[]{"title", "img"},
                    new int[]{R.id.title, R.id.img});

            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    CarInfor s = mCarList.get(position);
                    Toast.makeText(getApplicationContext(), s.getCarName(), Toast.LENGTH_LONG).show();

                }

            });
            mLinearLayout.addView(listview);
        }
        mScrollView.addView(mLinearLayout);
        this.setContentView(mScrollView);
        getActionBar().setTitle("ËÑË÷½á¹û");

    }


    public ArrayList<Map<String, Object>> getUniformData(ArrayList<CarInfor> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CarInfor cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", cs.getCarName());
            map.put("img", R.drawable.ic_launcher);
            list.add(map);

        }

        return list;
    }
}
