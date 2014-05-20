package com.Doric.CarBook.search;


import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
    public static ArrayList<CarSeries> mCarSeriesList= new ArrayList<CarSeries>();
    private LinearLayout linearLayout;
    private ArrayList<Pair<String,MyListView>> listarray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        listarray= new ArrayList<Pair<String, MyListView>>();
        linearLayout = (LinearLayout) inflater.inflate(R.layout.sea_search, container, false);

        initPage();
        new GetPicData().run();
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


                if (key.trim().equals("")) {
                    Toast.makeText(SearchMain.searchmain, "关键字不可为空", Toast.LENGTH_LONG).show();
                    return;
                }
                   // mCarSeriesList = PinyinSearch.search(key);




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
                text.setText("  "+ pair.first);
                text.setTextColor(Color.rgb(0, 0, 0));
                text.setBackgroundColor(Color.rgb(255, 255, 255));
                text.setTextSize(20);

                mLinearLayout.addView(text);
                MyListView listview = new MyListView(SearchMain.searchmain);

                SimpleAdapter adapter = new SimpleAdapter(SearchMain.searchmain, getUniformData(pair.second), R.layout.sea_list_layout,
                        new String[]{"title", "img"},
                        new int[]{R.id.title, R.id.img});
                listview.setDivider(getResources().getDrawable(R.drawable.list_divider));
                listview.setDividerHeight(1);
                listview.setAdapter(adapter);
                adapter.setViewBinder(new ListViewBinder());
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        ListView lv = (ListView) parent;
                        HashMap<String, Object> Info = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter返回Map
                        String key = (String) Info.get("title");
                        String[] strarr = key.split(" ");
                        if (strarr.length != 2)
                            return;

                        SearchMain.searchmain.ToCarListShow(strarr[0], strarr[1]);


                        //Toast.makeText(getApplicationContext(),(String)Info.get("title"),Toast.LENGTH_LONG).show();

                    }

                });
                listarray.add(new Pair<String, MyListView>(pair.first,listview));
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



    private class ListViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {
            // TODO Auto-generated method stub
            if ((view instanceof ImageView) && (data instanceof Bitmap)) {
                ImageView imageView = (ImageView) view;
                Bitmap bmp = (Bitmap) data;
                imageView.setImageBitmap(bmp);
                return true;
            }
            return false;
        }

    }


    final Handler cwjHandler = new Handler();
    class UpdateRunnable implements  Runnable{
        SimpleAdapter simpleAdapter = null;
        public UpdateRunnable(SimpleAdapter sa){
            simpleAdapter = sa;
        }
        public void run() {
            simpleAdapter.notifyDataSetChanged();
        }
    };


    public class GetPicData extends Thread {
        // private Set<LoadImage> taskSet;

        public GetPicData() {

            //taskSet = new HashSet<LoadImage>();
        }

        public void run() {
            for (Pair<String, MyListView> pair : listarray) {
                //LoadImage i =  new LoadImage(cs.getCarSeableName(),cs.getPicPath());
                HttpURLConnection con = null;
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                BufferedInputStream bis = null;
                File imageFile = null;
                SimpleAdapter simpleAdapter = (SimpleAdapter) pair.second.getAdapter();
                for (int i = 0; i < simpleAdapter.getCount(); i++) {
                    Map<String, Object> map = (Map<String, Object>) simpleAdapter.getItem(i);
                    CarSeries cs= mCarSeriesList.get(i);
                    Bitmap bitmap =null;
                    String imageUrl = cs.getPicPath();
                    imageFile = new File(getImagePath(imageUrl));
                    try {
                        if (!imageFile.exists()) {
                            URL url = new URL(imageUrl);
                            con = (HttpURLConnection) url.openConnection();
                            con.setConnectTimeout(5 * 1000);
                            con.setReadTimeout(15 * 1000);
                            con.setDoInput(true);
                            con.setDoOutput(true);
                            bis = new BufferedInputStream(con.getInputStream());
                            imageFile = new File(getImagePath(imageUrl));
                            fos = new FileOutputStream(imageFile);
                            bos = new BufferedOutputStream(fos);
                            byte[] b = new byte[1024];
                            int length;
                            while ((length = bis.read(b)) != -1) {
                                bos.write(b, 0, length);
                                bos.flush();
                            }
                            if (bis != null) {
                                bis.close();
                            }
                            if (bos != null) {
                                bos.close();
                            }
                            bitmap = BitmapFactory.decodeFile(getImagePath(imageUrl));

                        }
                        else{
                            bitmap = BitmapFactory.decodeFile(getImagePath(imageUrl));
                        }
                        if (bitmap!= null) {
                            map.put("img", bitmap);
                            cwjHandler.post(new UpdateRunnable(simpleAdapter));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }

        private String getSDPath(){
            File sdDir = null;
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
            if   (sdCardExist)
            {
                sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            }
            return sdDir.toString();

        }


        private String getImagePath(String imageUrl) {
            int lastSlashIndex = imageUrl.lastIndexOf("/");
            String imageTPath = imageUrl.substring(0, lastSlashIndex);
            String extra = imageUrl.substring(imageUrl.lastIndexOf("."));
            lastSlashIndex = imageTPath.lastIndexOf("/");
            String imageSeries = imageTPath.substring(lastSlashIndex + 1);  //  Series
            imageTPath = imageTPath.substring(0, lastSlashIndex);
            String imageName = imageTPath.substring(imageTPath.lastIndexOf("/") + 1);
            imageName = imageName + imageSeries + extra;
            System.out.println(imageName);
            String imageDir = getSDPath()
                    + "/CarBook/Cache/";
            File file = new File(imageDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String imagePath = imageDir + imageName;

            return imagePath;
        }

    }




}

class SearchGetData {
    public static FragmentTransaction fragmentTransaction;
    public static JSONObject searchObj;
    public static List<NameValuePair> searchParams = new ArrayList<NameValuePair>();
    public static String url = Constant.BASE_URL + "/search.php";
    private static String key;

    public static void getSearchData(FragmentTransaction ft,String sysmbol){
            fragmentTransaction =ft;
            key = sysmbol;
            searchParams.add(new BasicNameValuePair("tag", "violent_search"));
            new GetSearchData().execute();



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
            searchObj = jsonParser.getJSONFromUrl(url, searchParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SearchMain.searchmain.stopLoading();

            if (searchObj != null) {
                try {

                    int success = searchObj.getInt("success");
                    if(success==0){
                        SearchMain.searchmain.stopLoading();
                        Toast.makeText(SearchMain.searchmain, "未找到符合的车辆", Toast.LENGTH_LONG).show();
                    }
                    else if (success == 1) {
                        ArrayList<CarSeries> carSerieses = new ArrayList<CarSeries>();
                        int num = searchObj.getInt("search_number");
                        for (int i = 1; i <= num; i++) {
                            CarSeries cs = new CarSeries();
                            JSONObject ja = searchObj.getJSONObject("car_" + i);
                            cs.setCarSeableName(ja.getString("brand"));
                            cs.setName(ja.getString("brand_series"));

                            cs.setPicPath(Constant.BASE_URL + "/" + ja.getString("pictures_url"));

                            carSerieses.add(cs);

                        }
                        if(carSerieses.size()>0) {
                            Collections.sort(carSerieses, new ComparatorCarSeries());
                            carSerieses = PinyinSearch.search(carSerieses, key);

                        }
                        Search.setData(carSerieses);
                        fragmentTransaction.commit();
                    }
                } catch (JSONException e) {

                    Toast.makeText(SearchMain.searchmain, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(SearchMain.searchmain, "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
            }
        }
    }
}
