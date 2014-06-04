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
    // ���ñ�ǩ����
    public static final int MAX_TAB_SIZE = 5;
    // �����������ݵ�JSONObject
    static JSONObject carInfo;
    // ��ȡ��bundle
    static Bundle bundle;
    // ��ʼ��carId
    int carId = 0;
    // ��ȡ������Ϣ��Url
    String CarInfoUrl = Constant.BASE_URL + "/showcar.php";
    // �������������������Ĳ����б�
    List<NameValuePair> carParamsRequest = new ArrayList<NameValuePair>();
    // ������
    ProgressDialog progressDialog;
    // ʵ����������Ϣ
    CarInfor car = new CarInfor();
    /*
    ʵ��һ���������һ����ģ���������������ͼƬ���������������ۡ������ۡ��ĳ�����Ϣչʾҳ��
     */
    private ViewPager mViewPager;

    UserFunctions userFunctions;   //�û����ܺ��� ������Ӻ�ȡ�����ղ�

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_show);

        userFunctions = new UserFunctions(getApplicationContext());

        //�������󲢻�ȡJson��
        bundle = getIntent().getExtras();
        JSONObject jo = null;
        /*try {
            jo = new JSONObject(bundle.getString("cn.jpush.android.EXTRA"));

            // Ϊ�����������������׼��
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
        carParamsRequest.add(new BasicNameValuePair("brand", HotCarShow.Transform("�µ�")));
        carParamsRequest.add(new BasicNameValuePair("series", HotCarShow.Transform("�µ�A6L")));
        carParamsRequest.add(new BasicNameValuePair("model_number", HotCarShow.Transform("2014�� TFSI �ֶ�������")));*/



        String carName = bundle.getString("series")+" "+bundle.getString("model_number");
        System.out.println("sd" + carName);
        car.setCarName(carName);

        if (getActionBar() != null) {
            getActionBar().setTitle(car.getCarName());
        }

        //ͨ�����̹߳���carʵ������ʼ��Activity
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

        //��ʼ�� ActionBar
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
            //����ʱ����
            progressDialog = new ProgressDialog(CarShow.this);
            progressDialog.setMessage("������..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //���������������
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
                        Toast.makeText(CarShow.this.getApplicationContext(), "û�����Ƴ�", Toast.LENGTH_LONG).show();
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
                // ��ʼ��fragment
                initView();
            } else {
                Toast.makeText(CarShow.this.getApplicationContext(), "�޷��������磬���������ֻ���������", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class CollectAsync extends AsyncTask<Void, Void, Integer> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Integer doInBackground(Void... params) {
            //userFunctions.getMyCollection();   //������
            if (!userFunctions.isCollected(carId)) {
                if (userFunctions.addToCollection(carId)) {
                    return 1;  //�ղسɹ�������1
                }
            } else {
                if (userFunctions.cancelCollect(carId)) {
                    return 2;  //ȡ���ղسɹ�������2
                }
            }
            return 0;  //ʧ�ܣ�����0
        }

        protected void onPostExecute(Integer result) {
            if (result == 1) {
                invalidateOptionsMenu();
                Toast.makeText(getApplicationContext(), "���ղ�", Toast.LENGTH_SHORT).show();
            } else if (result == 2) {
                invalidateOptionsMenu();
                Toast.makeText(getApplicationContext(), "��ȡ���ղ�", Toast.LENGTH_SHORT).show();
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
