package com.Doric.CarBook.search;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.Doric.CarBook.R;

import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Fragment;
import android.view.*;

/**
 * Created by Administrator on 2014/5/12.
 */
public class SearchMain extends Activity {
    public static SearchMain searchmain;
    public DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ProgressDialog progressDialog;


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Fragment mfFragment = new Search();

                //创建一个FragmentManager管理器，根据getFragmentManager的返回值可以了解到，此处是用来操作我们创建的PlanetFragment对象的。
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.drawerFrame, mfFragment, "Search");
                transaction.addToBackStack("Search");
                //根据android api所说，用当前的PlanetFragment来替换之前存在的Fragment。
                transaction.commit();
                return true;
            case R.id.action_conditionSearch:
                Fragment mfFragment1 = new ConditionSearch();

                //创建一个FragmentManager管理器，根据getFragmentManager的返回值可以了解到，此处是用来操作我们创建的PlanetFragment对象的。
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                transaction1.replace(R.id.drawerFrame, mfFragment1, "ConditionSearch:");
                transaction1.addToBackStack("ConditionSearch");
                transaction1.commit();
                //根据android api所说，用当前的PlanetFragment来替换之前存在的Fragment。

                return true;
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchmain = this;
        setContentView(R.layout.sea_main);
        //获取数据

        //获取控件
        getActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.drawerListView);
        InInitialize();


    }

    //打包车系
    private ArrayList<Map<String, Object>> getUniformDataSeries(ArrayList<CarSeries> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CarSeries cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", cs.getName());
            if (i_map.containsKey(cs.getName()) && i_map.get(cs.getName()) != null)
                map.put("img", (Bitmap) i_map.get(cs.getName()));
            else
                map.put("img", R.drawable.ic_launcher);
            //map.put("price", cs.getLowPrice() + " -- " + cs.getHighPrice());
            list.add(map);

        }

        return list;

    }


    private ArrayList<CarSeries> carSeriesArrayList = new ArrayList<CarSeries>();

    public void setListData(ArrayList<CarSeries> carSeriesList) {
        this.carSeriesArrayList.clear();
        this.carSeriesArrayList.addAll(carSeriesList);
        mDrawerList = (ListView) findViewById(R.id.drawerListView);

        SimpleAdapter adapter = new SimpleAdapter(this, getUniformDataSeries(carSeriesArrayList), R.layout.sea_row,
                new String[]{"title", "img"/*,"price"*/},
                new int[]{R.id.row_title, R.id.row_icon/*,R.id.row_price*/});
        mDrawerList.setDivider(getResources().getDrawable(R.drawable.list_divider));
        mDrawerList.setDividerHeight(1);
        mDrawerList.setAdapter(adapter);
        adapter.setViewBinder(new ListViewBinder());
        //点击车系
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListView lv = (ListView) parent;
                HashMap<String, Object> Info = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter返回Map
                mDrawerLayout.closeDrawer(mDrawerList);

                ToCarListShow(AlphaShow.carseable.getCarSeableName(), (String) Info.get("title"));


            }

        });
        OpenSliding();

    }


    public void ToCarListShow(String carbrand, String carSerie) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new CarListShow();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.drawerFrame, fragment, "CarListShow");
        transaction.addToBackStack("CarListShow");
        CarListShow.CarBrand = carbrand;
        CarListShow.CarSeries = carSerie;
        CarSeries carSeries = CarSeableData.find(carbrand).findCarSeries(carSerie);
        carSeries.loadCar(transaction);
    }

    public void InInitialize() {
        Fragment mfFragment = new AlphaShow();

        //创建一个FragmentManager管理器，根据getFragmentManager的返回值可以了解到，此处是用来操作我们创建的PlanetFragment对象的。
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.drawerFrame, mfFragment, "AlphaShow");

        //根据android api所说，用当前的PlanetFragment来替换之前存在的Fragment。
        CarSeableData.getData(ft);


    }

    public void OpenSliding() {

        mDrawerLayout.openDrawer(mDrawerList);

    }

    public void SearchToResult(Double lowprice, Double higprice, Grade grade) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = new Result();
        transaction.replace(R.id.drawerFrame, fragment, "Result");
        transaction.addToBackStack("Result");

        CSearchGetData.getCSearchData(transaction, higprice, lowprice, grade);


    }

    public void SearchToSearch(String sysmbol) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new Search();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.drawerFrame, fragment, "Search");
        SearchGetData.getSearchData(transaction, sysmbol);


    }

    public void loading() {
        if (!progressDialog.isShowing()) {

            progressDialog.setMessage("加载中..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }

    public void stopLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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

    private static Map<String, Bitmap> i_map = new HashMap<String, Bitmap>();


    public static class GetPicData extends AsyncTask<Void, Void, Void> {
        // private Set<LoadImage> taskSet;
        private ArrayList<CarSeries> carSeriesArrayList;

        public GetPicData(ArrayList<CarSeries> cs) {
            carSeriesArrayList = new ArrayList<CarSeries>();
            carSeriesArrayList.clear();
            carSeriesArrayList.addAll(cs);
            //taskSet = new HashSet<LoadImage>();
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

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection con = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            File imageFile = null;
            for (CarSeries cs : carSeriesArrayList) {

                Bitmap bitmap = null;

                String imageUrl = cs.getPicPath();
                System.out.println(imageUrl);

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

                    } else {
                        bitmap = BitmapFactory.decodeFile(getImagePath(imageUrl));
                    }
                    if (bitmap != null) {
                        i_map.put(cs.getName(), bitmap);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            searchmain.stopLoading();
            searchmain.setListData(carSeriesArrayList);
        }
    }
}

