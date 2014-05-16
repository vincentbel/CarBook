package com.Doric.CarBook.search;


import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;


import java.util.*;

import android.app.Fragment;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class Search extends Fragment {

    private EditText mEditText;
    private ImageButton mButton;
    private LinearLayout mLinearLayout;
    private ScrollView mScrollView;
    private static ArrayList<CarSeries> mCarSeriesList;
    private LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        linearLayout = (LinearLayout) inflater.inflate(R.layout.sea_search, container, false);

        mCarSeriesList=new ArrayList<CarSeries>();

        initPage();
        return linearLayout;

    }

    @Override
    public void onResume() {
        super.onResume();
        SearchMain.searchmain.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    public ArrayList<Map<String, Object>> getUniformData(ArrayList<CarSeries> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CarSeries cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", cs.getCarSeableName()+" "+cs.getName());
            map.put("img", R.drawable.ic_launcher);
            list.add(map);

        }

        return list;

    }

    public void initPage() {
        mButton = (ImageButton) linearLayout.findViewById(R.id.searchbutton);

        mEditText = (EditText) linearLayout.findViewById(R.id.searchkeyword);
        mButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                String key = mEditText.getText().toString();


                if (!key.trim().equals(""))

                    mCarSeriesList = PinyinSearch.search(key);
                else {
                    Toast.makeText(SearchMain.searchmain, "关键字不可为空", Toast.LENGTH_LONG).show();
                    return;

                }
                SearchMain.searchmain.SearchToSearch(key);
            }
        });
        if(mCarSeriesList.size()>0){

            LinearLayout l = (LinearLayout) linearLayout.findViewById(R.id.searchreasult);
            l.removeAllViews();
            mLinearLayout = new LinearLayout(SearchMain.searchmain);
            LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.
                    LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mLinearLayout.setLayoutParams(param1);
            mLinearLayout.setOrientation(LinearLayout.VERTICAL);
            mLinearLayout.setBackgroundColor(Color.rgb(255, 255, 255));



            ArrayList<Pair<String, ArrayList<CarSeries>>> al = PinYinIndex.getIndex_CarSeries(mCarSeriesList, SearchMain.searchmain);


            mScrollView = new ScrollView(SearchMain.searchmain);
            mScrollView.setEnabled(true);
            mScrollView.setBackgroundColor(Color.rgb(255, 255, 255));
            ScrollView.LayoutParams param2 = new ScrollView.LayoutParams(ScrollView.
                    LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
            mScrollView.setLayoutParams(param2);


            for (Pair<String, ArrayList<CarSeries>> pair : al) {
                TextView text = new TextView(SearchMain.searchmain);
                text.setText(pair.first);
                text.setTextColor(Color.rgb(0, 0, 0));
                text.setBackgroundColor(Color.rgb(230, 230, 230));
                text.setTextSize(20);

                mLinearLayout.addView(text);
                MyListView listview = new MyListView(SearchMain.searchmain);

                SimpleAdapter adapter = new SimpleAdapter(SearchMain.searchmain, getUniformData(pair.second), R.layout.sea_list_layout,
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
                        String key = (String)Info.get("title");
                        String [] strarr = key.split(" ");
                        if(strarr.length!=2)
                            return ;

                        SearchMain.searchmain.ToCarListShow(strarr[0],strarr[1]);
                        //it.setClass(Search.this, CarListShow.class);

                        //Toast.makeText(getApplicationContext(),(String)Info.get("title"),Toast.LENGTH_LONG).show();

                    }

                });
                mLinearLayout.addView(listview);
            }
            mScrollView.addView(mLinearLayout);
            l.addView(mScrollView);

        }
    }
    public static void  setData(ArrayList<CarSeries> al){
        mCarSeriesList= new ArrayList<CarSeries>();
        mCarSeriesList.addAll(al);

    }

}

class SearchGetData {
    public static FragmentTransaction fragmentTransaction;
    public static JSONObject brandObj;
    public static List<NameValuePair> searchParams = new ArrayList<NameValuePair>();
    public static String url = Constant.BASE_URL + "/search.php";
    public static boolean isload = false;
    public static void getSearchData(FragmentTransaction ft,String sysmbol){
        fragmentTransaction =ft;
        if(isload==false) {
            searchParams.add(new BasicNameValuePair("tag", "search"));
            new GetSearchData().execute();
        }
        else{
            Search.setData(PinyinSearch.search(sysmbol));
        }

    }
    private static class GetSearchData extends AsyncTask<Void, Void, Void> {


        protected void onPreExecute() {
            super.onPreExecute();
            //弹出"正在登录"框
            SearchMain.searchmain.loading();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            brandObj = jsonParser.getJSONFromUrl(url, searchParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SearchMain.searchmain.stopLoading();
            /*
            if (brandObj != null) {
                try {

                    int success = brandObj.getInt("success");
                    if (success == 1) {
                        int num = brandObj.getInt("brand_number");
                        for (int i = 1; i <= num; i++) {
                            CarSeable cs = new CarSeable();
                            cs.setCarSeableName(brandObj.getString("brand_" + i));
                            cs.setPicPath(Static.BASE_URL + "/" + brandObj.getString("brand_" + i + "_url"));

                            mCarSeable.add(cs);
                        }
                        isload = true;
                        if (mCarSeable.size() > 0)
                            Collections.sort(mCarSeable, new ComparatorCarSeable());
                        Search.setData(PinyinSearch.search(sysmbol));
                        fragmentTransaction.commit();
                        //context.initPage();
                    }
                } catch (JSONException e) {

                    Toast.makeText(SearchMain.searchmain, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(SearchMain.searchmain, "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
            }*/
        }
    }
}
