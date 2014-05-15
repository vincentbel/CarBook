package com.Doric.CarBook.search;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.Doric.CarBook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import android.app.Fragment;
import android.view.*;
import android.content.Intent;
/**
 * Created by Administrator on 2014/5/12.
 */
public class SearchMain extends Activity {
    public static SearchMain searchmain;
    public DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private  ProgressDialog progressDialog;


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Fragment mfFragment = new Search();

                //����һ��FragmentManager������������getFragmentManager�ķ���ֵ�����˽⵽���˴��������������Ǵ�����PlanetFragment����ġ�
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction =fragmentManager.beginTransaction();

                transaction.replace(R.id.drawerFrame, mfFragment, "Search");
                transaction.addToBackStack("Search");
                //����android api��˵���õ�ǰ��PlanetFragment���滻֮ǰ���ڵ�Fragment��
                transaction .commit();
                return true;
            case R.id.action_conditionSearch:
                Fragment mfFragment1 = new ConditionSearch();

                //����һ��FragmentManager������������getFragmentManager�ķ���ֵ�����˽⵽���˴��������������Ǵ�����PlanetFragment����ġ�
                FragmentManager fragmentManager1 = getFragmentManager();
                FragmentTransaction transaction1 =fragmentManager1.beginTransaction();
                transaction1.replace(R.id.drawerFrame, mfFragment1, "ConditionSearch:");
                transaction1.addToBackStack("ConditionSearch");
                transaction1.commit();
                //����android api��˵���õ�ǰ��PlanetFragment���滻֮ǰ���ڵ�Fragment��

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
        //��ȡ����

        //��ȡ�ؼ�
        progressDialog =new ProgressDialog(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerList = (ListView) findViewById(R.id.drawerListView);
        InInitialize();


    }


    //�����ϵ
    private ArrayList<Map<String, Object>> getUniformDataSeries(ArrayList<CarSeries> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CarSeries cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", cs.getName());
            map.put("img", R.drawable.ic_launcher);
            //map.put("price", cs.getLowPrice() + " -- " + cs.getHighPrice());
            list.add(map);

        }

        return list;

    }

    public void setListData(ArrayList<CarSeries> serieslist) {

        mDrawerList = (ListView) findViewById(R.id.drawerListView);

        SimpleAdapter adapter = new SimpleAdapter(this, getUniformDataSeries(serieslist), R.layout.sea_row,
                new String[]{"title", "img"/*,"price"*/},
                new int[]{R.id.row_title, R.id.row_icon/*,R.id.row_price*/});
        mDrawerList.setDivider(getResources().getDrawable(R.drawable.list_divider));
        mDrawerList.setDividerHeight(1);
        mDrawerList.setAdapter(adapter);
        //�����ϵ
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ListView lv = (ListView) parent;
                HashMap<String, Object> Info = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter����Map
                mDrawerLayout.closeDrawer(mDrawerList);

                ToCarListShow(AlphaShow.carseable.getCarSeableName(),(String)Info.get("title"));




            }

        });

    }


    public void ToCarListShow(String carbrand,String carSerie){
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new CarListShow();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.drawerFrame, fragment, "CarListShow");
        transaction.addToBackStack("CarListShow");
        CarListShow.CarBrand = carbrand;
        CarListShow.CarSeries=carSerie;
        CarSeries carSeries = CarSeableData.find(carbrand).findCarSeries(carSerie);
        carSeries.loadCar(transaction);
    }

    public void InInitialize() {
        Fragment mfFragment = new AlphaShow();

        //����һ��FragmentManager������������getFragmentManager�ķ���ֵ�����˽⵽���˴��������������Ǵ�����PlanetFragment����ġ�
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft =fragmentManager.beginTransaction();
        ft.replace(R.id.drawerFrame,mfFragment,"AlphaShow");

        //����android api��˵���õ�ǰ��PlanetFragment���滻֮ǰ���ڵ�Fragment��
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

        CSearchGetData.getCSearchData(transaction,higprice,lowprice,grade);


    }
    public void SearchToSearch(String sysmbol) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new Search();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.drawerFrame, fragment, "Search");
        SearchGetData.getSearchData(transaction,sysmbol);


    }

    public  void loading() {
        if (!progressDialog.isShowing()) {

            progressDialog.setMessage("������..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }

    public  void stopLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
