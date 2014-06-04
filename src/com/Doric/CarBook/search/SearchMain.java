package com.Doric.CarBook.search;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import android.support.v4.widget.DrawerLayout;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.Doric.CarBook.R;

import java.io.*;

import java.net.HttpURLConnection;

import java.net.URL;
import java.util.*;

import android.app.Fragment;
import android.view.*;

/**
 * Created by Administrator on 2014/5/12.
 */
public class SearchMain extends Activity  {
    /**
     * static instance
     */
    public static SearchMain searchmain;


    /**
     * nothing to say
     */
    public DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private boolean findcar=true;
    private ProgressDialog progressDialog;

    /**
     * make sure the usage of this activity
     * it may be find car or may be carcmp
     * @return is find car?
     */
    public boolean getUsage(){return findcar;}

    /**
     * menu selected
     * 1\search 2\conditionSearch 3\Carcmp
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Fragment mfFragment = new Search();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.drawerFrame, mfFragment, "Search");
                transaction.addToBackStack("Search");

                transaction.commit();
                return true;
            case R.id.action_conditionSearch:
                Fragment mfFragment1 = new ConditionSearch();
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction transaction1 = fragmentManager1.beginTransaction();
                transaction1.replace(R.id.drawerFrame, mfFragment1, "ConditionSearch:");
                transaction1.addToBackStack("ConditionSearch");
                transaction1.commit();


                return true;

            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.action_cmp:
                CarSeable.CarBrand="";
                SearchMain.this.startActivity(new Intent(SearchMain.searchmain,CarComparison.class));

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * nothing to say
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sea_search, menu);
        if(!findcar){
            menu.findItem(R.id.action_conditionSearch).setVisible(false);
            menu.findItem(R.id.action_search).setVisible(false);
        }


        return true;
    }

    /**
     * make sure which usage it is
     * and start initialize
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CarSeable.CarBrand="";
        System.out.println("Create");
        searchmain = this;
        setContentView(R.layout.sea_main);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.drawerListView);

        Initialize();



    }

    @Override
    public void finish() {
        moveTaskToBack(true);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    public  void setUseage(boolean isFindCar){
        findcar = isFindCar;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(true);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    /**
     * pack the carseries info into Map<String,Object>
     * @param al_cs carseries list
     * @return a map of Map<String,Object>
     */
    private ArrayList<Map<String, Object>> getUniformDataSeries(ArrayList<CarSeries> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CarSeries cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", cs.getName());
            if (i_map.containsKey(cs.getName()) && i_map.get(cs.getName()) != null)
                map.put("img",  i_map.get(cs.getName()));

            else
                map.put("img", R.drawable.ic_launcher);

            list.add(map);



        }

        return list;

    }

    /**
     * saved carSeries data
     * for user select a item twice (don't need to load data again)
     */
    private ArrayList<CarSeries> carSeriesArrayList = new ArrayList<CarSeries>();

    /**
     *  it is used when CarSeriesData has been load
     *  to fill the listview
     * @param carSeriesList data
     *
     */

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
        /**
         * used when user select a carserie
         * jump to carlistshow fragment
         */
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListView lv = (ListView) parent;
                HashMap<String, Object> Info = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter返回Map
                mDrawerLayout.closeDrawer(mDrawerList);

                ToCarListShow(AlphaShowData.carseable.getCarSeableName(), (String) Info.get("title"));


            }

        });
        OpenSliding();

    }


    /**
     * do something to jump to carlistshow fragment
     * @param carbrand
     * @param carSerie
     */
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


    /**
     * initialize the alphashow
     */
    public void Initialize() {
        Fragment mfFragment = new AlphaShow();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.drawerFrame, mfFragment, "AlphaShow");
        CarSeableData.getData(ft);


    }

    /**
     * open the sliding..
     * I say nothing ah.
     */
    public void OpenSliding() {

        mDrawerLayout.openDrawer(mDrawerList);

    }

    /**
     *  it is used to jump from ConditionSearch to Result
     *  i should not do this way .
     *  but i don't want to fix it anymore. ^-^
     * @param lowprice
     * @param higprice
     * @param grade
     */
    public void SearchToResult(Double lowprice, Double higprice, Grade grade) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = new Result();
        transaction.replace(R.id.drawerFrame, fragment, "Result");
        transaction.addToBackStack("Result");

        CSearchGetData.getCSearchData(transaction, higprice, lowprice, grade);


    }

    /**
     *  it is used to jump from Search to Search
     *  means restart the Search
     *  i should not do this way .
     *  but i don't want to fix it anymore. ^-^
     * @param sysmbol
     */

    public void SearchToSearch(String sysmbol) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new Search();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.drawerFrame, fragment, "Search");
        transaction.addToBackStack("Search");
        SearchGetData.getSearchData(transaction, sysmbol);


    }

    /**
     * make the progressDialog load
     */
    public void loading() {
        if (!progressDialog.isShowing()) {

            progressDialog.setMessage("加载中..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }
    /**
     * make the progressDialog stop
     */
    public void stopLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }



    /**
     * the class to make Adapter can adapt the bitmap
     */
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
     * image_ map
     */
    private static Map<String, Bitmap> i_map = new HashMap<String, Bitmap>();


    /**
     * AsyncTask to get the CarSeries data
     */
    public static class GetPicData extends AsyncTask<Void, Void, Void> {

        private ArrayList<CarSeries> carSeriesArrayList;

        public GetPicData(ArrayList<CarSeries> cs) {
            carSeriesArrayList = new ArrayList<CarSeries>();
            carSeriesArrayList.clear();
            carSeriesArrayList.addAll(cs);

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

        @Override
        protected Void doInBackground(Void... params) {

            HttpURLConnection con = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            File imageFile = null;
            if(i_map.size()>=200)
                i_map.clear();
            for (CarSeries cs : carSeriesArrayList) {

                Bitmap bitmap = null;

                String imageUrl = cs.getPicPath();
                Log.d("DragList",imageUrl);
                imageFile = new File(getImagePath(imageUrl));
                try {

                    if(!i_map.containsKey(cs.getName())) {

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
                                i_map.put(cs.getName(), otherbitmap);

                            }

                        } else {
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.outWidth = 80;
                            options.outHeight = 80;
                            bitmap = BitmapFactory.decodeFile(getImagePath(imageUrl), options);
                            if (bitmap != null) {
                                i_map.put(cs.getName(), bitmap);

                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            searchmain.setListData(carSeriesArrayList);
            searchmain.stopLoading();

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

    }


}

