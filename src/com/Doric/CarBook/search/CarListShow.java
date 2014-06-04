package com.Doric.CarBook.search;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.Doric.CarBook.R;

import java.io.*;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Fragment;
import com.Doric.CarBook.car.CarShow;

/**
 *
 */
public class CarListShow extends Fragment {

    private LinearLayout mLinearLayout;
    public static String CarBrand;
    public static String CarSeries;
    private static ArrayList<CarInfor> carlist;
    private static ListView listView;

    public static void setCarList(ArrayList<CarInfor> cl){
        carlist =new ArrayList<CarInfor>();
        carlist.addAll(cl);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        initPage();
        new GetPicData().start();
        return mLinearLayout;

    }


    @Override
    public void onResume() {
        super.onResume();
        SearchMain.searchmain.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    /**
     * 获取格式化的数据
     * @param al_cs
     * @return
     */
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


    /**
     * 初始化界面
     */
    public void initPage() {



        //界面显示
        mLinearLayout = new LinearLayout(SearchMain.searchmain);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(param1);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setBackgroundColor(Color.rgb(255, 255, 255));

//        mScrollView = new ScrollView(SearchMain.searchmain);
//        mScrollView.setEnabled(true);
//        mScrollView.setBackgroundColor(Color.rgb(255, 255, 255));
//        ScrollView.LayoutParams param2 = new ScrollView.LayoutParams(ScrollView.
//                LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
//        mScrollView.setLayoutParams(param2);



             listView = new ListView(SearchMain.searchmain);

            SimpleAdapter adapter = new SimpleAdapter(SearchMain.searchmain, getUniformData(carlist), R.layout.sea_list_layout,
                    new String[]{"title", "img"},
                    new int[]{R.id.title, R.id.img});

            listView.setAdapter(adapter);
            adapter.setViewBinder(new ListViewBinder());
            listView.setDivider(getResources().getDrawable(R.drawable.list_divider));
            listView.setDividerHeight(1);
            listView.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ListView lv = (ListView) parent;
                    HashMap<String, Object> Info = (HashMap<String, Object>) lv.getItemAtPosition(position);
                    System.out.println(SearchMain.searchmain.getUsage());
                    if(SearchMain.searchmain.getUsage()) {
                        Intent it = new Intent();
                        CarInfor cs = CarSeableData.find(CarBrand).findCarSeries(CarSeries).findCarInfo((String)Info.get("title"));
                        Bundle bundle =new Bundle();
                        bundle.putString("car_id", cs.getCarId());
                        bundle.putString("series", cs.getCarSerie());
                        bundle.putString("model_number", cs.getCarName());

                        it.putExtras(bundle);
                        it.setClass(SearchMain.searchmain, CarShow.class);
                        SearchMain.searchmain.startActivity(it);
                    }
                    else{
                        CarInfor ci = CarSeableData.find(CarBrand).findCarSeries(CarSeries).findCarInfo((String)Info.get("title"));
                        CarComparison.carComparison.AddCmp(ci);
                        Intent it = new Intent();
                        it.setClass(SearchMain.searchmain, CarComparison.class);
                        SearchMain.searchmain.startActivity(it);


                    }


                }

            });

            mLinearLayout.addView(listView);

//        mScrollView.addView(mLinearLayout);
//        mScrollView.setX(0);
//        mScrollView.setY(0);

    }


    /**
     * 数据绑定类
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
     * 更新数据的handler
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


        /**
         * run
         */
        public void run() {

                //LoadImage i =  new LoadImage(cs.getCarSeableName(),cs.getPicPath());
                HttpURLConnection con = null;
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                BufferedInputStream bis = null;
                File imageFile = null;
                SimpleAdapter simpleAdapter = (SimpleAdapter) listView.getAdapter();
                for (int i = 0; i < simpleAdapter.getCount(); i++) {
                    Map<String, Object> map = (Map<String, Object>) simpleAdapter.getItem(i);
                    Bitmap bitmap =null;
                    String imageUrl=CarSeableData.find(CarBrand).findCarSeries(CarSeries).getPicPath();
                    imageFile = new File(getImagePath(imageUrl));
                    try {
                        if (!imageFile.exists()) {
                            URL url = new URL(GBK2UTF.Transform(imageUrl.replace(" ","%20")));
                            con = (HttpURLConnection) url.openConnection();
                            con.setConnectTimeout(5 * 1000);
                            con.setReadTimeout(15 * 1000);
                            con.setDoInput(true);
                            con.setDoOutput(true);
                            bitmap =  BitmapFactory.decodeStream(con.getInputStream());
                            Bitmap otherbitmap = Bitmap.createScaledBitmap(bitmap,50,50,true);
                            bitmap.recycle();
                            System.gc();
                            bitmap= otherbitmap;
                            saveMyBitmap(getImagePath(imageUrl),bitmap);
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


        /**
         * 获取SD卡路径
         * @return
         */
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



        /**
         * 根据Url获取唯一的图片路径
         * @param imageUrl
         * @return
         */
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
            String imagePath = imageDir +"small"+ imageName;

            return imagePath;
        }




        /**
         * 保存图片
         * @param path
         * @param bitmap
         * @throws IOException
         */
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




