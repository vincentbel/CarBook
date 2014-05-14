package com.Doric.CarBook.car;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.Toast;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CarShow extends FragmentActivity implements android.app.ActionBar.TabListener {
    public static final int MAX_TAB_SIZE = 5;
    static JSONObject carInfo;
    String url = Constant.BASE_URL + "/showcar.php";
    List<NameValuePair> carParamsRequest = new ArrayList<NameValuePair>();
    ProgressDialog progressDialog;
    /*
    ʵ��һ���������һ����ģ���������������ͼƬ���������������ۡ������ۡ��ĳ�����Ϣչʾҳ��
     */
    private ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_show);

        //�������󲢻�ȡJson��

        carParamsRequest.add(new BasicNameValuePair("tag", "showcar"));
        carParamsRequest.add(new BasicNameValuePair("brand", "BMW"));
        carParamsRequest.add(new BasicNameValuePair("series", "7series"));
        carParamsRequest.add(new BasicNameValuePair("model_number", "2013 740Li grand"));

        //ͨ�����̹߳���carʵ������ʼ��Activity
        new GetCarInfo().execute();
    }

    private void findViewById() {

        mViewPager = (ViewPager) this.findViewById(R.id.pager);
    }

    private void initView() {
        final android.app.ActionBar mActionBar = getActionBar();

        if (mActionBar != null) {
            mActionBar.setDisplayHomeAsUpEnabled(false);
        }

        if (mActionBar != null) {
            mActionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);
        }

        TabFragmentPagerAdapter mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                if (mActionBar != null) {
                    mActionBar.setSelectedNavigationItem(arg0);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        //��ʼ�� ActionBar
        for (int i = 0; i < MAX_TAB_SIZE; i++) {
            android.app.ActionBar.Tab tab = null;
            if (mActionBar != null) {
                tab = mActionBar.newTab();
            }
            if (tab != null) {
                tab.setText(mAdapter.getPageTitle(i)).setTabListener(this);
            }
            if (mActionBar != null) {
                mActionBar.addTab(tab);
            }
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

    private class GetCarInfo extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //����ʱ����
            progressDialog = new ProgressDialog(CarShow.this);
            progressDialog.setMessage("������..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //���������������
            JSONParser jsonParser = new JSONParser();
            carInfo = jsonParser.getJSONFromUrl(url, carParamsRequest);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (carInfo != null) {
                CarInfor car = new CarInfor();
                car.setCarName("BMW 7series 2013 740Li grand");
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
                if (getActionBar() != null) {
                    getActionBar().setTitle(car.getCarName());
                }
                findViewById();
                initView();
            } else {
                Toast.makeText(CarShow.this.getApplicationContext(), "�޷��������磬���������ֻ���������", Toast.LENGTH_LONG).show();
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
                    return "����";
                case 1:
                    return "ͼƬ";
                case 2:
                    return "����";
                case 3:
                    return "����";
                case 4:
                    return "����";
                default:
                    return "";
            }
        }
    }
}
