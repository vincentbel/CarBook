package com.Doric.CarBook.search;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.Doric.CarBook.R;
import android.content.res.*;
import com.Doric.CarBook.car.CarInfor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Search extends Activity {
    private EditText mEditText;
    private Button mButton;
    private LinearLayout mLinearLayout;
    private ScrollView mScrollView;
    private ArrayList<CarInfor> mCarList;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.sea_search);
        getActionBar().setTitle("搜索");
        mButton = (Button)findViewById(R.id.searchbutton);
        mEditText=(EditText)findViewById(R.id.searchkeyword);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View rl = inflater.inflate(R.layout.sea_search, null);
                LinearLayout l = (LinearLayout)rl.findViewById(R.id.searchreasult);
                l.removeAllViews();
                mLinearLayout = new LinearLayout(Search.this);
                LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.
                        LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                mLinearLayout.setLayoutParams(param1);
                mLinearLayout.setOrientation(LinearLayout.VERTICAL);
                mLinearLayout.setBackgroundColor(Color.rgb(255, 255, 255));
                String key = mEditText.getText().toString();


                if(!key.trim().equals(""))
                mCarList = PinyinSearch.search(key);
                else {Toast.makeText(Search.this,"关键字不可为空",Toast.LENGTH_LONG).show(); return;}


                ArrayList<Pair<String, ArrayList<CarInfor>>> al = PinYinIndex.getIndex_CarInfo(mCarList, Search.this);
                mScrollView = new ScrollView(Search.this);
                mScrollView.setEnabled(true);
                mScrollView.setBackgroundColor(Color.rgb(255, 255, 255));
                ScrollView.LayoutParams param2 = new ScrollView.LayoutParams(ScrollView.
                        LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
                mScrollView.setLayoutParams(param2);


                for (Pair<String, ArrayList<CarInfor>> pair : al) {
                    TextView text = new TextView(Search.this);
                    text.setText(pair.first);
                    text.setTextColor(Color.rgb(0, 0, 0));
                    text.setBackgroundColor(Color.rgb(230,230,230));
                    text.setTextSize(20);

                    mLinearLayout.addView(text);
                    MyListView listview = new MyListView(Search.this);

                    SimpleAdapter adapter = new SimpleAdapter(Search.this, getUniformData(pair.second), R.layout.sea_list_layout,
                            new String[]{"title", "img"},
                            new int[]{R.id.title, R.id.img});
                    listview.setDivider(getResources().getDrawable(R.drawable.list_divider));
                    listview.setDividerHeight(1);
                    listview.setAdapter(adapter);
                    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            ListView lv = (ListView) parent;
                            HashMap<String, Object> Info = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter返回Map
                            Intent it = new Intent();
                            it.putExtra("CarSeableCName", (String) Info.get("title"));
                            //it.setClass(Search.this, CarListShow.class);
                            Search.this.startActivity(it);
                            //Toast.makeText(getApplicationContext(),(String)Info.get("title"),Toast.LENGTH_LONG).show();

                        }

                    });
                    mLinearLayout.addView(listview);
                }
                mScrollView.addView(mLinearLayout);
                l.addView(mScrollView);
                Search.this.setContentView(rl);


            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
