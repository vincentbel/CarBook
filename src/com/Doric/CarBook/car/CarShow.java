package com.Doric.CarBook.car;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import com.Doric.CarBook.R;

public class CarShow extends FragmentActivity implements android.app.ActionBar.TabListener {
    public static final int MAX_TAB_SIZE = 5;
    /*
    实现一个可以左右滑动的，包括“综述”“图片”“参数”“报价”“评论”的车辆信息展示页面
     */
    private ViewPager mViewPager;
    private TabFragmentPagerAdapter mAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_show);
        /*
         *    使用carId构造Car实例
         */
        Intent intent = getIntent();
        intent.getIntExtra("carId", 0);
        CarInfor car = new CarInfor();
        car.setCarId(89757);
        car.setBrandId("BMW");
        car.setCarName("Z4");
        car.setCarSize("Sports Car");
        car.setHighSpeed(200.0);
        car.setEngineType("好引擎");
        car.setHigPrice(50.0);
        car.setLowPrice(40.0);
        car.setTimeTo100Km(20.0);

        getActionBar().setTitle(car.getCarName());

        findViewById();
        initView();
    }

    private void findViewById() {

        mViewPager = (ViewPager) this.findViewById(R.id.pager);
    }

    private void initView() {
        final android.app.ActionBar mActionBar = getActionBar();

        mActionBar.setDisplayHomeAsUpEnabled(false);

        mActionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);

        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
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
            android.app.ActionBar.Tab tab = mActionBar.newTab();
            tab.setText(mAdapter.getPageTitle(i)).setTabListener(this);
            mActionBar.addTab(tab);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

    public static class TabFragmentPagerAdapter extends FragmentPagerAdapter {

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
