package com.Doric.CarBook.car;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;
import com.Doric.CarBook.member.UserFunctions;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CarShow extends FragmentActivity implements android.app.ActionBar.TabListener {
    // 设置标签个数
    public static final int MAX_TAB_SIZE = 5;
    // 用来保存数据的JSONObject
    static JSONObject carInfo;
    // 获取的bundle
    static Bundle bundle;
    // 初始化carId
    int carId = 0;
    // 获取车辆信息的Url
    String CarInfoUrl = Constant.BASE_URL + "/showcar.php";
    // 用来向服务器发送请求的参数列表
    List<NameValuePair> carParamsRequest = new ArrayList<NameValuePair>();
    // 进度条
    ProgressDialog progressDialog;
    // 实例化车辆信息
    CarInfor car = new CarInfor();
    /*
    实现一个可以左右滑动的，包括“综述”“图片”“参数”“报价”“评论”的车辆信息展示页面
     */
    private ViewPager mViewPager;

    UserFunctions userFunctions;   //用户功能函数 用于添加和取消到收藏

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_show);

        userFunctions = new UserFunctions(getApplicationContext());

        //发送请求并获取Json包
        bundle = getIntent().getExtras();
        JSONObject jo = null;
        /*try {
            jo = new JSONObject(bundle.getString("cn.jpush.android.EXTRA"));


            // 为向服务器发送请求做准备
            carParamsRequest.add(new BasicNameValuePair("tag", "showcar"));
            carParamsRequest.add(new BasicNameValuePair("brand", jo.getString("brand")));
            carParamsRequest.add(new BasicNameValuePair("series", jo.getString("series")));
            carParamsRequest.add(new BasicNameValuePair("model_number", jo.getString("model_number")));
        } catch (JSONException e) {
            e.printStackTrace();

        }*/


        carParamsRequest.add(new BasicNameValuePair("tag", HotCarShow.Transform("showcar")));
        carParamsRequest.add(new BasicNameValuePair("car_id", HotCarShow.Transform(bundle.getString("car_id"))));
        /*carParamsRequest.add(new BasicNameValuePair("series", HotCarShow.Transform(bundle.getString("series"))));
        carParamsRequest.add(new BasicNameValuePair("model_number", HotCarShow.Transform(bundle.getString("model_number"))));
*/
        /*carParamsRequest.add(new BasicNameValuePair("tag", HotCarShow.Transform("showcar")));
        carParamsRequest.add(new BasicNameValuePair("brand", HotCarShow.Transform("奥迪")));
        carParamsRequest.add(new BasicNameValuePair("series", HotCarShow.Transform("奥迪A6L")));
        carParamsRequest.add(new BasicNameValuePair("model_number", HotCarShow.Transform("2014款 TFSI 手动基本型")));*/



        String carName = bundle.getString("series")+" "+bundle.getString("model_number");
        System.out.println("sd" + carName);
        car.setCarName(carName);

        if (getActionBar() != null) {
            getActionBar().setTitle(car.getCarName());
        }


        //通过新线程构造car实例并初始化Activity
        new GetCarInfo().execute();
    }

    private void findViewById() {
        mViewPager = (ViewPager) this.findViewById(R.id.pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.car_show, menu)   ;
        MenuItem collectItem = menu.findItem(R.id.action_add_to_collection);
        if (userFunctions.isCollected(carId)) {
            collectItem.setIcon(R.drawable.ic_action_collected);
        } else {
            collectItem.setIcon(R.drawable.ic_action_add_to_collection);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                this.finish();
                return true;
            case R.id.action_add_to_collection:
                new CollectAsync().execute();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        final android.app.ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        if (mActionBar != null) {
            mActionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);
        }

        TabFragmentPagerAdapter mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                mActionBar.setSelectedNavigationItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        //初始化 ActionBar
        for (int i = 0; i < MAX_TAB_SIZE; i++) {
            mActionBar.addTab(mActionBar.newTab().
                    setText(mAdapter.getPageTitle(i)).
                    setTabListener(this));
        }
    }


    @Override
    public void onTabSelected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    private class GetCarInfo extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //加载时弹出
            progressDialog = new ProgressDialog(CarShow.this);
            progressDialog.setMessage("加载中..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            carInfo = jsonParser.getJSONFromUrl(CarInfoUrl, carParamsRequest);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (carInfo != null) {
                try {
                    if (carInfo.getInt("success")==0){
                        Toast.makeText(CarShow.this.getApplicationContext(), "没有这破车", Toast.LENGTH_LONG).show();
                    }else{
                        carId = Integer.parseInt(carInfo.getString("car_id"));
                        invalidateOptionsMenu();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                 /*
                try {
                    car.setCarName("BMW 7series 2013 740Li grand");
                    car.setCarGrade(carInfo.getString("car_grade"));
                    car.setCarBodyStructure(carInfo.getString("car_body_structure"));
                    car.setPrice(carInfo.getString("price"));
                    car.setTransmission(carInfo.getString("transmission"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                */

                findViewById();
                // 初始化fragment
                initView();
            } else {
                Toast.makeText(CarShow.this.getApplicationContext(), "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class CollectAsync extends AsyncTask<Void, Void, Integer> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Integer doInBackground(Void... params) {
            //userFunctions.getMyCollection();   //测试用
            if (!userFunctions.isCollected(carId)) {
                if (userFunctions.addToCollection(carId)) {
                    return 1;  //收藏成功，返回1
                }
            } else {
                if (userFunctions.cancelCollect(carId)) {
                    return 2;  //取消收藏成功，返回2
                }
            }
            return 0;  //失败，返回0
        }

        protected void onPostExecute(Integer result) {
            if (result == 1) {
                invalidateOptionsMenu();
                Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_SHORT).show();
            } else if (result == 2) {
                invalidateOptionsMenu();
                Toast.makeText(getApplicationContext(), "已取消收藏", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            Fragment ft = null;
            switch (arg0) {
                case 0:
                    ft = new SummaryFragment();
                    break;
                case 1:
                    ft = new PictureFragment();
                    break;
                case 2:
                    ft = new ParameterFragment();
                    break;
                case 3:
                    ft = new PriceFragment();
                    break;
                case 4:
                    ft = new CommentFragment();
                    break;
                default:
                    break;
            }
            return ft;
        }

        @Override
        public int getCount() {

            return MAX_TAB_SIZE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "综述";
                case 1:
                    return "图片";
                case 2:
                    return "参数";
                case 3:
                    return "报价";
                case 4:
                    return "评论";
                default:
                    return "";
            }
        }
    }
}
