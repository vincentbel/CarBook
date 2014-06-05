package com.Doric.CarBook.car;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
    // 获取的car_id
    static String car_id = null;
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
    //实现一个可以左右滑动的，包括“综述”“图片”“参数”“报价”“评论”的车辆信息展示页面
    private ViewPager mViewPager;
    //用户功能函数 用于添加和取消到收藏
    UserFunctions userFunctions;
    /*
    *   获取bundle，并开启异步线程获取JSON包
    */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_show);

        userFunctions = new UserFunctions(getApplicationContext());

        //发送请求并获取Json包
        bundle = getIntent().getExtras();
        // 判断是否从推送服务中获取信息
        if (bundle.get("cn.jpush.android.EXTRA")!=null) {
            Log.d("CarShow","bundle.get(\"cn.jpush.android.EXTRA\")!=null");
            JSONObject jo = null;
            try {
                jo = new JSONObject(bundle.getString("cn.jpush.android.EXTRA"));
                Log.d("CarShow",jo.getString("car_id"));

                // 添加向服务器发送的数据
                carParamsRequest.add(new BasicNameValuePair("tag", ("showcar")));
                carParamsRequest.add(new BasicNameValuePair("car_id", (jo.getString("car_id"))));
                car_id = jo.getString("car_id");
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }else{
            // 添加向服务器发送的数据
            Log.d("CarShow","bundle.get(\"cn.jpush.android.EXTRA\")==null");
            carParamsRequest.add(new BasicNameValuePair("tag", HotCarShow.Transform("showcar")));
            carParamsRequest.add(new BasicNameValuePair("car_id", HotCarShow.Transform(bundle.getString("car_id"))));
            car_id = bundle.getString("car_id");
        }


        //通过新线程构造car实例并初始化Activity
        new GetCarInfo().execute();
    }

    /*
    * 初始化ViewPager
    */
    private void findViewById() {
        mViewPager = (ViewPager) this.findViewById(R.id.pager);
    }
    /*
    * 初始化mune，收藏按钮
    */
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
    /*
    * 点击收藏按钮后，切换图标，并开启异步线程，将实现添加收藏
    */
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
    /*
    * 初始化View
    */
    private void initView() {
        // 显示actionBar中菜单
        final android.app.ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        if (mActionBar != null) {
            mActionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);
        }


        // 添加fragment的适配器
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
    /*
    *   获取车辆信息的异步线程工具类
    */
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
                    // 判断服务器上是否存在这辆车的信息
                    if (carInfo.getInt("success")==0){
                        Toast.makeText(CarShow.this.getApplicationContext(), "数据库异常", Toast.LENGTH_LONG).show();
                    }else{
                        // 通过series，model_number,构造车辆名
                        String carName = carInfo.getString("series")+" "+carInfo.getString("model_number");
                        System.out.println("sd" + carName);
                        car.setCarName(carName);
                        // 以车辆名作为actionBar的title
                        if (getActionBar() != null) {
                            getActionBar().setTitle(car.getCarName());
                        }
                        carId = Integer.parseInt(carInfo.getString("car_id"));
                        invalidateOptionsMenu();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                    //收藏成功，返回1
                    return 1;
                }
            } else {
                if (userFunctions.cancelCollect(carId)) {
                    //取消收藏成功，返回2
                    return 2;
                }
            }
            //失败，返回0
            return 0;
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
    /*
    * fragment的适配器
    */
    public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

        public TabFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /*
        * 确定需要构造的fragment
        */
        @Override
        public Fragment getItem(int arg0) {
            Fragment ft = null;
            switch (arg0) {
                case 0:
                    // 车辆展示综述界面
                    ft = new SummaryFragment();
                    break;
                case 1:
                    // 车辆展示图片界面
                    ft = new PictureFragment();
                    break;
                case 2:
                    // 车辆展示参数界面
                    ft = new ParameterFragment();
                    break;
                case 3:
                    // 车辆展示报价界面
                    ft = new PriceFragment();
                    break;
                case 4:
                    // 车辆展示评论界面
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
        /*
        * 为fragment 设置title
        */
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
