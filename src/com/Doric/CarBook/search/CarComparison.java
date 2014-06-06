package com.Doric.CarBook.search;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.Doric.CarBook.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/5/22.
 */

/**
 * 车辆对比的主页面。
 * 提供添加车辆和对比两种操作
 */
public class CarComparison extends Activity {
    public static CarComparison carComparison;
    private ProgressDialog progressDialog;
    private  ArrayList<CarInfor> cmplist ;
    private  ListView listView;
    public static ArrayList<CarInfor> carInfors ;

    public  void AddCmp(CarInfor ci){
        if(!cmplist.contains(ci)){

            ((CmpListAdapter)listView.getAdapter()).add(ci);

        }

    }
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
    protected void onResume() {
        super.onResume();
        CmpListAdapter cmpListAdapter  = (CmpListAdapter)listView.getAdapter();
        handler.post(new UpdateRunnable(cmpListAdapter));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carComparison= this;
        progressDialog = new ProgressDialog(this);
        cmplist = new ArrayList<CarInfor>();
        setContentView(R.layout.sea_cmplist);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Initialize();

    }

    @Override
    public void onBackPressed() {
        SearchMain.searchmain.setUseage(true);
        finish();
    }

    /**
     * 初始化页面
     */
    private void Initialize(){
            listView = (ListView)findViewById(R.id.cmp_list);
            CmpListAdapter cmpListAdapter = new CmpListAdapter(this,R.layout.sea_cmplistlayout,cmplist);
            listView.setAdapter(cmpListAdapter);
            Button addCar = (Button)findViewById(R.id.add_btn);
            addCar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it = new Intent();
                    it.setClass(CarComparison.this, SearchMain.class);
                    CarComparison.this.startActivity(it);
                    SearchMain.searchmain.setUseage(false);
                    System.out.println(SearchMain.searchmain.getUsage());

                }
            });
            Button cmp = (Button)findViewById(R.id.cmp_btn);
            cmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent it =new Intent();
                    it.setClass(CarComparison.this,CarCmpShow.class);
                    int listnum = listView.getChildCount();
                    CmpListAdapter adapter = (CmpListAdapter)listView.getAdapter();
                    carInfors = new ArrayList<CarInfor>();
                    for(int i=0;i<listnum;i++){
                        LinearLayout layout = (LinearLayout)listView.getChildAt(i);
                        CheckBox cb = (CheckBox)layout.findViewById(R.id.cmplist_text);
                        if(cb.isChecked()){
                            //选定该车辆
                            carInfors.add(adapter.getItem(i));
                        }
                    }
                    if(carInfors.size()!=2){
                        Toast.makeText(CarComparison.this,"请选择两辆车",Toast.LENGTH_LONG).show();
                        return;
                    }
                    it.putExtra("firstCar",carInfors.get(0));
                    it.putExtra("secondCar",carInfors.get(1));

                    startActivity(it);
                }
            });


    }

    /**
     * 更新数据的Handler
     */
    final Handler handler = new Handler();
    class UpdateRunnable implements  Runnable{
        CmpListAdapter cmpListAdapter = null;
        public UpdateRunnable(CmpListAdapter sa){
            cmpListAdapter = sa;
        }
        public void run() {
            cmpListAdapter.notifyDataSetChanged();
        }
    };


    /**
     * ArrayAdapter<CarInfor>
     *
     */
    class CmpListAdapter extends ArrayAdapter<CarInfor> {
        private int resource;
        public CmpListAdapter(Context context, int resourceId, List<CarInfor> objects) {
            super(context, resourceId, objects);
            resource = resourceId;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout linearLayout;
            CarInfor list = getItem(position);
            if(convertView == null) {
                linearLayout= new LinearLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
                vi.inflate(resource, linearLayout, true);
              
            } else {
                linearLayout = (LinearLayout)convertView;

            }
            //绑定数据
            CheckBox cb= (CheckBox)linearLayout.findViewById(R.id.cmplist_text);
            cb.setText(list.getCarSeable()+" " +list.getCarSerie()+" "+list.getCarName());



            return linearLayout;
        }


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

}