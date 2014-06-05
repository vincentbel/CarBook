package com.Doric.CarBook.search;



import android.app.Activity;
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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;


import java.io.*;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.*;

import android.app.Fragment;
import com.Doric.CarBook.car.CarShow;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


public class Search extends Activity {

    private EditText mEditText;
    private ImageButton mButton;
    public static Search  search=null;

    public static ArrayList<CarInfor> mCarInfoList= new ArrayList<CarInfor>();

    private ListView listView=null;

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        search= this;
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initPage();
        new GetPicData().start();

    }

    /**
     * make sure the slidelist is locked closed
     */
    @Override
    public void onResume() {
        super.onResume();
        SearchMain.searchmain.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    /**
     * pack the data ...
     * @param al_cs
     * @return
     */
    public ArrayList<Map<String, Object>> getUniformData(ArrayList<CarInfor> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CarInfor cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", cs.getCarSeable()+" "+cs.getCarSerie()+" "+cs.getCarName());
            map.put("img", R.drawable.ic_launcher);
            list.add(map);

        }

        return list;

    }

    /**
     * init
     */
    public void initPage() {
        setContentView(R.layout.sea_search);
        mButton = (ImageButton) findViewById(R.id.searchbutton);

        mEditText = (EditText)findViewById(R.id.searchkeyword);
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


            LinearLayout l = (LinearLayout) findViewById(R.id.searchreasultlayout);



            listView =(ListView)findViewById(R.id.searchreasult);

            SimpleAdapter adapter = new SimpleAdapter(SearchMain.searchmain, getUniformData(mCarInfoList), R.layout.sea_list_layout,
                        new String[]{"title", "img"},
                        new int[]{R.id.title, R.id.img});
            listView.setDivider(getResources().getDrawable(R.drawable.list_divider));
            listView.setDividerHeight(1);
            listView.setAdapter(adapter);
            adapter.setViewBinder(new ListViewBinder());
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent it= new Intent();
                        CarInfor ci  =mCarInfoList.get(position);
                        Bundle bundle = new Bundle();
                        bundle.putString("car_id",ci.getCarId());
                        bundle.putString("series",ci.getCarSerie());
                        bundle.putString("car_id",ci.getCarId());

                        it.setClass(Search.this,CarShow.class);
                        Search.this.startActivity(it);
                        //Toast.makeText(getApplicationContext(),(String)Info.get("title"),Toast.LENGTH_LONG).show();

                    }

                });



    }


    public static void  setData(ArrayList<CarInfor> al){
        mCarInfoList= new ArrayList<CarInfor>();
        mCarInfoList.addAll(al);

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

        public GetPicData() {

        }

        public void run() {

                //LoadImage i =  new LoadImage(cs.getCarSeableName(),cs.getPicPath());
                HttpURLConnection con = null;
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                BufferedInputStream bis = null;
                File imageFile = null;
                if(listView==null)
                    return ;
                SimpleAdapter simpleAdapter = (SimpleAdapter) listView.getAdapter();
                for (int i = 0; i < simpleAdapter.getCount(); i++) {
                    Map<String, Object> map = (Map<String, Object>) simpleAdapter.getItem(i);
                    CarInfor cs= mCarInfoList.get(i);
                    Bitmap bitmap =null;
                    String imageUrl = cs.getCarPicPath();
                    imageFile = new File(getImagePath(imageUrl));
                    try {
                        if (!imageFile.exists()) {
                            URL url = new URL(GBK2UTF.Transform(imageUrl.replace(" ","%20")));
                            con = (HttpURLConnection) url.openConnection();
                            con.setConnectTimeout(5 * 1000);
                            con.setReadTimeout(15 * 1000);
                            con.setDoInput(true);
                            con.setDoOutput(true);
                            bitmap = BitmapFactory.decodeStream(con.getInputStream());
                            int height = bitmap.getHeight()/(bitmap.getWidth()/ 80);
                            Bitmap otherbitmap = Bitmap.createScaledBitmap(bitmap,80,height,true);
                            bitmap.recycle();
                            System.gc();
                            if (otherbitmap != null) {
                                saveMyBitmap(getImagePath(imageUrl),otherbitmap);


                            }
                            bitmap = otherbitmap;
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


        public void saveMyBitmap(String path,Bitmap bitmap) throws IOException {
            File f = new File(path);
            f.createNewFile();
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(f);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            try {
                fOut.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
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

            String imageDir = getSDPath()
                    + "/CarBook/Cache/";
            File file = new File(imageDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String imagePath = imageDir + "small"+imageName;

            return imagePath;
        }

    }






class SearchGetData {

    public static JSONObject searchObj;
    public static List<NameValuePair> searchParams = new ArrayList<NameValuePair>();
    public static String url = Constant.BASE_URL + "/search.php";
    private static String key;

    public static void getSearchData(String sysmbol){

        key = sysmbol;
        searchParams.add(new BasicNameValuePair("tag", "keywords_search"));
        searchParams.add(new BasicNameValuePair("keywords",GBK2UTF.Transform(sysmbol.replace(" ","%20"))));
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

                        ArrayList<CarInfor> carInfors = new ArrayList<CarInfor>();
                        System.out.println(searchObj.toString());
                        int num = searchObj.getInt("search_number");
                        for (int i = 1; i <= num; i++) {
                            CarInfor cs = new CarInfor();
                            JSONObject ja = searchObj.getJSONObject("car_" + i);
                            cs.setCarSeable(ja.getString("brand"));
                            cs.setCarSerie(ja.getString("brand_series"));
                            cs.setCarGrade(ja.getString("grade"));
                            cs.setCarPicPath(Constant.BASE_URL + "/" + ja.getString("pictures_url"));
                            cs.setCarName(ja.getString("model_number"));
                            cs.setCarId(ja.getString("car_id"));
                            carInfors.add(cs);

                        }
                        if(carInfors.size()>0) {
                            Collections.sort(carInfors, new ComparatorCarInfo());


                        }
                        Search.setData(carInfors);
                        SearchMain.searchmain.stopLoading();
                        Search.search.finish();
                        SearchMain.searchmain.startActivity(new Intent(SearchMain.searchmain,Search.class));
                    }
                } catch (JSONException e) {
                    SearchMain.searchmain.stopLoading();
                    Toast.makeText(SearchMain.searchmain, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                SearchMain.searchmain.stopLoading();
                Toast.makeText(SearchMain.searchmain, "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
            }
        }
    }
}
