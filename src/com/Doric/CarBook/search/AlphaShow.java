package com.Doric.CarBook.search;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.Doric.CarBook.R;
import com.Doric.CarBook.car.CarInfor;
import com.Doric.CarBook.utility.JSONParser;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.Doric.CarBook.Static;

import android.support.v4.app.FragmentActivity;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;


public class AlphaShow extends FragmentActivity {

    private LinearLayout mLinearLayout;
    private ScrollView mScrollView;
    private ProgressDialog progressDialog;
    static JSONObject brandObj;
    List<NameValuePair> brandParams = new ArrayList<NameValuePair>();
    private static String url = Static.BASE_URL+"/search.php";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            //响应每个菜单项(通过菜单项的ID)

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

        //brandParams.add(new BasicNameValuePair("tag", "brand"));
        //new GetBrand().execute();

        //setContentView(R.layout.activity_main);
        mLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(param1);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setBackgroundColor(Color.rgb(255,255, 255));

        ArrayList<Pair<String, ArrayList<CarSeable>>> al = PinYinIndex.getIndex_CarSeable(CarSeableData.mCarSeable, this);
        mScrollView = new ScrollView(this);
        mScrollView.setEnabled(true);
        mScrollView.setBackgroundColor(Color.rgb(255, 255, 255));
        ScrollView.LayoutParams param2 = new ScrollView.LayoutParams(ScrollView.
                LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
        mScrollView.setLayoutParams(param2);


        for (Pair<String, ArrayList<CarSeable>> pair : al) {
            TextView text = new TextView(this);
            text.setText(pair.first);
            text.setTextColor(Color.rgb(0, 0, 0));
            text.setBackgroundColor(Color.rgb(230,230,230));
            text.setTextSize(20);

            mLinearLayout.addView(text);
            MyListView listview = new MyListView(this);

            SimpleAdapter adapter = new SimpleAdapter(this, getUniformDataSeable(pair.second), R.layout.sea_list_layout,
                    new String[]{"title", "img"},
                    new int[]{R.id.title, R.id.img});
            listview.setDivider(getResources().getDrawable(R.drawable.list_divider));
            listview.setDividerHeight(1);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ListView lv = (ListView) parent;
                    HashMap<String, Object> Info = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter返回Map
                    //Toast.makeText(getApplicationContext(),(String)Info.get("title"),Toast.LENGTH_LONG).show();
                    samplelist.carSeableName = (String)Info.get("title");
                    samplelist.setData(getUniformDataSystem(CarSeableData.find((String)Info.get("title")).getCarSystemList()));
                    menu.showMenu();
                }

            });
            mLinearLayout.addView(listview);
        }

        mScrollView.addView(mLinearLayout);
        this.setContentView(mScrollView);

        getActionBar().setTitle("拼音索引");
        init();
        /*int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView title = (TextView) findViewById(titleId);
        title.setTextColor(this.getResources().getColor(Color.WHITE));*/


		
		/*
		SimpleAdapter adapter = new SimpleAdapter(this,getData(),R.layout.sea_list_layout,
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


    public ArrayList<Map<String, Object>> getUniformDataSeable(ArrayList<CarSeable> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CarSeable cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", cs.getCarSeableName());
            map.put("img", R.drawable.ic_launcher);
            list.add(map);

        }

        return list;

    }

    public ArrayList<Map<String, Object>> getUniformDataSystem(ArrayList<CarSystem> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CarSystem cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", cs.getName());
            map.put("img", R.drawable.ic_launcher);
            map.put("price",cs.getLowPrice()+ " -- " +cs.getHighPrice());
            list.add(map);

        }

        return list;

    }

    private SlidingMenu menu;
    private SampleListFragment samplelist=new SampleListFragment();
    private void init()
    {
        samplelist.alphashow =this;
        menu = new SlidingMenu(this);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.sea_shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        // 设置滑动菜单的视图界面
        menu.setMenu(R.layout.sea_menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame,  samplelist).commit();


    }

    @Override
    public void onBackPressed() {
        //点击返回键关闭滑动菜单
        if (menu.isMenuShowing()) {
            menu.showContent();
        } else {
            super.onBackPressed();
        }
    }


    private class GetBrand extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //弹出"正在登录"框
            progressDialog = new ProgressDialog(AlphaShow.this);
            progressDialog.setMessage("加载中..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            brandObj = jsonParser.getJSONFromUrl(url, brandParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (brandObj!=null) {


            }
            else {
                Toast.makeText(AlphaShow.this.getApplicationContext(), "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
            }
        }
    }
}




