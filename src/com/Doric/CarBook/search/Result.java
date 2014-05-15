package com.Doric.CarBook.search;


import android.app.FragmentTransaction;
import android.app.ProgressDialog;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;


import java.util.*;

import android.app.Fragment;

import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class Result extends Fragment {
    private static ArrayList<CarInfor> mCarList;
    private LinearLayout mLinearLayout;
    private ScrollView mScrollView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //��ȡ��Ϣ
        initPage();
        return mScrollView;

    }

    @Override
    public void onResume() {
        super.onResume();
        SearchMain.searchmain.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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

    public void initPage() {
        mLinearLayout = new LinearLayout(SearchMain.searchmain);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(param1);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setBackgroundColor(Color.rgb(255, 255, 255));
        ArrayList<Pair<String, ArrayList<CarInfor>>> al = PinYinIndex.getIndex_CarInfo(mCarList, SearchMain.searchmain);

        mScrollView = new ScrollView(SearchMain.searchmain);
        mScrollView.setEnabled(true);
        mScrollView.setBackgroundColor(Color.rgb(255, 255, 255));
        ScrollView.LayoutParams param2 = new ScrollView.LayoutParams(ScrollView.
                LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
        mScrollView.setLayoutParams(param2);


        for (Pair<String, ArrayList<CarInfor>> pair : al) {
            TextView text = new TextView(SearchMain.searchmain);
            text.setText(pair.first);

            text.setTextColor(Color.rgb(0, 0, 0));

            text.setBackgroundColor(Color.rgb(230, 230, 230));
            text.setTextSize(20);

            mLinearLayout.addView(text);
            MyListView listview = new MyListView(SearchMain.searchmain);
            listview.setDivider(getResources().getDrawable(R.drawable.list_divider));
            listview.setDividerHeight(1);
            SimpleAdapter adapter = new SimpleAdapter(SearchMain.searchmain, getUniformData(pair.second), R.layout.sea_list_layout,
                    new String[]{"title", "img"},
                    new int[]{R.id.title, R.id.img});

            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    CarInfor s = mCarList.get(position);

                    Toast.makeText(SearchMain.searchmain, s.getCarName(), Toast.LENGTH_LONG).show();

                }

            });
            mLinearLayout.addView(listview);
        }
        mScrollView.addView(mLinearLayout);

    }

    public static void  setCarList(ArrayList<CarInfor> cl){
        mCarList= new ArrayList<CarInfor>();
        mCarList.addAll(cl);
    }


}

class CSearchGetData{
    public  static  JSONObject carInfoObj;
    public  static List<NameValuePair> carInfoParams = new ArrayList<NameValuePair>();
    static FragmentTransaction fragmentTransaction;

    public   static String url = Constant.BASE_URL + "/search.php";
    private static Grade grade;
    public static void getCSearchData(FragmentTransaction ft,Double hig,Double low,Grade gra){

        grade= gra;
        carInfoParams.add(new BasicNameValuePair("tag", "conditional_search"));
        carInfoParams.add(new BasicNameValuePair("low_price",low.toString()));
        carInfoParams.add(new BasicNameValuePair("high_price",hig.toString()));
        carInfoParams.add(new BasicNameValuePair("grade",""));
        fragmentTransaction=ft;
        //Undone..
        new GetCarInfo().execute();

    }

    static  class GetCarInfo extends AsyncTask<Void, Void, Void> {

    public GetCarInfo(){
        super();
        //SearchMain.searchmain.InInitialize();

    }

    protected void onPreExecute() {
        super.onPreExecute();
        //����ʱ����
        SearchMain.searchmain.loading();
    }

    protected Void doInBackground(Void... params) {
        //���������������
        JSONParser jsonParser = new JSONParser();
        carInfoObj = jsonParser.getJSONFromUrl(url, carInfoParams);
        return null;
    }

    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        SearchMain.searchmain.stopLoading();
        if (carInfoObj != null) {
            ArrayList<CarInfor> carlist =new ArrayList<CarInfor>();
            try {
                int success = carInfoObj.getInt("success");
                if(success==0){

                    Toast.makeText(SearchMain.searchmain, "δ�ҵ����ϵĳ���", Toast.LENGTH_LONG).show();


                }
                else if (success == 1) {
                    int num = carInfoObj.getInt("search_number");
                    for (int i = 1; i <= num; i++) {
                        CarInfor cs = new CarInfor();
                        JSONObject ja= carInfoObj.getJSONObject("car_" + i);
                        cs.setCarSeable(ja.getString("brand"));
                        cs.setCarSerie(ja.getString("brand_series"));
                        cs.setCarName(ja.getString("model_number"));
                        cs.setCarPicPath(Constant.BASE_URL + "/" + ja.getString("pictures_url"));
                        cs.setCarGrade(ja.getString("grade"));
                        carlist.add(cs);

                    }
                    //Ʒ��ɸѡ
                    ArrayList<CarInfor> delete = new ArrayList<CarInfor>();
                    for (CarInfor ci : carlist) {
                        if(grade.getValue(ci.getCarGrade())==false){
                            delete.add(ci);
                        }
                    }
                    carlist.removeAll(delete);


                    if (carlist.size() > 0) {
                        Collections.sort(carlist, new ComparatorCarInfo());
                        Result.setCarList(carlist);
                    }

                    fragmentTransaction.commit();
                    //context.initPage();
                }
            } catch (JSONException e) {

                Toast.makeText(SearchMain.searchmain, e.toString(), Toast.LENGTH_LONG).show();
            }



        } else {
            Toast.makeText(SearchMain.searchmain, "�޷��������磬���������ֻ���������", Toast.LENGTH_LONG).show();
        }
    }
    }
}
