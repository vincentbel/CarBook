/*
 Copyright 2013 Tonic Artos

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.Doric.CarBook.search;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;
import com.Doric.CarBook.R;

import java.util.ArrayList;

/**
 * CarArgumentFragment所在的Activity
 */
public class CarCmpShow extends ActionBarActivity {
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public static CarCmpShow cmpShow ;
    private ProgressDialog progressDialog ;
    public static ArrayList<CarInfor> carInfors;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cmpShow= this;
        Intent it =getIntent();
        carInfors =new ArrayList<CarInfor>();
        carInfors.add((CarInfor)it.getSerializableExtra("firstCar"));
        carInfors.add((CarInfor)it.getSerializableExtra("secondCar"));
        System.out.println(carInfors.get(0).getCarSeable()+ carInfors.get(0).getCarSerie()+carInfors.get(0).getCarName());
        System.out.println(carInfors.get(1).getCarSeable()+ carInfors.get(1).getCarSerie()+carInfors.get(1).getCarName());
        progressDialog=new ProgressDialog(this);

        setContentView(R.layout.sea_cmpshow);
        TextView t1 = (TextView)findViewById(R.id.first_car);
        t1.setText(CarCmpShow.carInfors.get(0).getCarName());

        TextView t2 = (TextView)findViewById(R.id.second_car);
        t2.setText(CarCmpShow.carInfors.get(1).getCarName());
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: If exposing deep links into your app, handle intents here.
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
