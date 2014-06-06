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

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
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

/**
 * 条件搜索结果界面
 */
public class Result extends Activity {
    public static ArrayList<CarInfor> mCarList;


    private ListView listView;


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
    protected void onCreate(Bundle saveInstance){
        super.onCreate(saveInstance);
        //获取信息
        getActionBar().setDisplayHomeAsUpEnabled(true);
        initPage();
        new GetPicData().start();


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
            if(!cs.getCarId().equals("0"))
            map.put("img", R.drawable.ic_launcher);
            list.add(map);

        }

        return list;
    }

    public void initPage() {


//
//        mScrollView = new ScrollView(SearchMain.searchmain);
//        mScrollView.setEnabled(true);
//        mScrollView.setBackgroundColor(Color.rgb(255, 255, 255));
//        ScrollView.LayoutParams param2 = new ScrollView.LayoutParams(ScrollView.
//                LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
//        mScrollView.setLayoutParams(param2);

        if(mCarList.size()==0){
            CarInfor cs =new CarInfor() ;
            cs.setCarName("很抱歉，没有找到您想要的车");
            cs.setCarId("0");
            cs.setCarPicPath("");
            mCarList.add(cs);
        }

          setContentView(R.layout.sea_result);

          listView = (ListView)findViewById(R.id.resultlist);
          SimpleAdapter adapter = new SimpleAdapter(SearchMain.searchmain, getUniformData(mCarList), R.layout.sea_list_layout,
                    new String[]{"title", "img"},
                    new int[]{R.id.title, R.id.img});
          listView.setDivider(getResources().getDrawable(R.drawable.list_divider));
          listView.setDividerHeight(1);
          listView.setAdapter(adapter);
          adapter.setViewBinder(new ListViewBinder());
          listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Intent it = new Intent();
                    CarInfor ci = mCarList.get(position);
                    Log.d("CarInfo", ci.getCarId());
                    if(ci.getCarId().equals("0"))
                        return ;
                    Bundle bundle = new Bundle();
                    bundle.putString("car_id", ci.getCarId());
                    bundle.putString("series", ci.getCarSerie());
                    bundle.putString("model_number", ci.getCarName());
                    it.putExtras(bundle);
                    it.setClass(Result.this, CarShow.class);
                    Result.this.startActivity(it);


              }

            });




    }

    public static void  setCarList(ArrayList<CarInfor> cl){
        mCarList= new ArrayList<CarInfor>();
        mCarList.addAll(cl);
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


    /**
     * 更新数据的Handler
     */
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


                HttpURLConnection con = null;
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                BufferedInputStream bis = null;
                File imageFile = null;
                SimpleAdapter simpleAdapter = (SimpleAdapter)listView.getAdapter();
                for (int i = 0; i < simpleAdapter.getCount(); i++) {
                    Map<String, Object> map = (Map<String, Object>) simpleAdapter.getItem(i);
                    CarInfor ci = mCarList.get(i);
                    if(ci.getCarId().equals("0"))
                        break;
                    Bitmap bitmap = null;
                    String imageUrl = ci.getCarPicPath();
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

                        } else {
                            bitmap = BitmapFactory.decodeFile(getImagePath(imageUrl));
                        }
                        if (bitmap != null) {
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
        private String getSDPath() {
            File sdDir = null;
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
            if (sdCardExist) {
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




/**
 * 获取搜索结果的异步类
 */
class CSearchGetData{
    public  static  JSONObject carInfoObj;
    public  static List<NameValuePair> carInfoParams = new ArrayList<NameValuePair>();


    public   static String url = Constant.BASE_URL + "/search.php";
    private static Grade grade;
    public static void getCSearchData(Double hig,Double low,Grade gra){

        grade= gra;
        ArrayList<String> stringArrayList = grade.getSelected();
        carInfoParams.add(new BasicNameValuePair("tag", "conditional_search"));
        carInfoParams.add(new BasicNameValuePair("low_price", low.toString()));
        carInfoParams.add(new BasicNameValuePair("high_price",hig.toString()));
        carInfoParams.add(new BasicNameValuePair("grade_num",new Integer(stringArrayList.size()).toString()));
        for(int i=1;i<=stringArrayList.size();i++){
            carInfoParams.add(new BasicNameValuePair("grade_"+i,GBK2UTF.Transform(stringArrayList.get(i-1).replace(" ","%20"))));
        }

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
            //加载时弹出
            SearchMain.searchmain.loading();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            carInfoObj = jsonParser.getJSONFromUrl(url, carInfoParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (carInfoObj != null) {
                ArrayList<CarInfor> carlist =new ArrayList<CarInfor>();
                try {
                    int success = carInfoObj.getInt("success");
                    if(success==0){
                        SearchMain.searchmain.stopLoading();
                        Toast.makeText(SearchMain.searchmain, "未找到符合的车辆", Toast.LENGTH_LONG).show();
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
                            cs.setCarId(ja.getString("car_id"));
                            cs.setCarGrade(ja.getString("grade"));
                            carlist.add(cs);

                        }


                        if (carlist.size() > 0) {
                            Collections.sort(carlist, new ComparatorCarInfo());

                        }
                        Result.setCarList(carlist);
                        SearchMain.searchmain.stopLoading();
                        SearchMain.searchmain.startActivity(new Intent(SearchMain.searchmain,Result.class));
                        //context.initPage();
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
