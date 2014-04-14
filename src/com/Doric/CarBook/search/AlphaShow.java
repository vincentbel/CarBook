package com.Doric.CarBook.search;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.Doric.CarBook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlphaShow extends Activity {

    private LinearLayout mLinearLayout;
    private ScrollView mScrollView;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            //响应每个菜单项(通过菜单项的ID)
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_LONG).show();
                break;
            case R.id.action_search:
                //Toast.makeText(getApplicationContext(), "Search", Toast.LENGTH_LONG).show();
                Intent it = new Intent();
                it.setClass(AlphaShow.this, Search.class);
                AlphaShow.this.startActivity(it);
                break;
            case R.id.action_conditionSearch:
                Intent it1 = new Intent();
                it1.setClass(AlphaShow.this, ConditionSearch.class);
                AlphaShow.this.startActivity(it1);
                break;
            default:

                //对没有处理的事件，交给父类来处理
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CarSeableData.getData();
        //setContentView(R.layout.activity_main);
        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(param1);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setBackgroundColor(Color.rgb(0, 0, 0));

        ArrayList<Pair<String, ArrayList<CarSeable>>> al = PinYinIndex.getIndex_CarSeable(CarSeableData.mCarSeable, this);
        mScrollView = new ScrollView(this);
        mScrollView.setEnabled(true);
        mScrollView.setBackgroundColor(Color.rgb(0, 0, 0));
        ScrollView.LayoutParams param2 = new ScrollView.LayoutParams(ScrollView.
                LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
        mScrollView.setLayoutParams(param2);


        for (Pair<String, ArrayList<CarSeable>> pair : al) {
            TextView text = new TextView(this);
            text.setText(pair.first);
            text.setTextColor(Color.rgb(255, 255, 255));
            text.setTextSize(20);

            mLinearLayout.addView(text);
            MyListView listview = new MyListView(this);

            SimpleAdapter adapter = new SimpleAdapter(this, getUniformData(pair.second), R.layout.list_layout,
                    new String[]{"title", "img"},
                    new int[]{R.id.title, R.id.img});

            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ListView lv = (ListView) parent;
                    HashMap<String, Object> Info = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter返回Map
                    Intent it = new Intent();
                    it.putExtra("CarSeableCName", (String) Info.get("title"));
                    it.setClass(AlphaShow.this, CarListShow.class);
                    AlphaShow.this.startActivity(it);
                    //Toast.makeText(getApplicationContext(),(String)Info.get("title"),Toast.LENGTH_LONG).show();

                }

            });
            mLinearLayout.addView(listview);
        }
        mScrollView.addView(mLinearLayout);
        this.setContentView(mScrollView);

		
		
		
		/*
		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.list_layout,
				new String[]{"title","img"},
				new int[]{R.id.title,R.id.img});
		
		mMainList.setAdapter(adapter);
		mMainList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CarSeable s= mCarSeable.get(position);
				Toast.makeText(getApplicationContext(),s.getcName(),Toast.LENGTH_LONG).show();
				
			}
			
		});
		*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public ArrayList<Map<String, Object>> getUniformData(ArrayList<CarSeable> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CarSeable cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", cs.getCarSeableName());
            map.put("img", R.drawable.ic_launcher);
            list.add(map);

        }

        return list;

    }


}




