package com.Doric.CarBook.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.Doric.CarBook.R;


public class ConditionSearch extends Activity {
    EditText carSeable;
    EditText carPricelow;
    EditText carPricehig;
    EditText carSize;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_condition_search);
        carSeable = (EditText) findViewById(R.id.carseable_name);
        carSize = (EditText) findViewById(R.id.car_size);
        carPricelow = (EditText) findViewById(R.id.carprice_low);
        carPricehig = (EditText) findViewById(R.id.carprice_hig);
        search = (Button) findViewById(R.id.csearchbutton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable carseable = carSeable.getText();
                Editable carpricelow = carPricelow.getText();
                Editable carpricehig = carPricehig.getText();
                Editable carsize = carSize.getText();
                String lp = carpricelow.toString();
                String hp = carpricehig.toString();
                lp = lp.trim();
                hp = hp.trim();
                String size = carsize.toString();
                size = size.trim();
                size = size.toUpperCase();
                if (lp.equals("")) lp = "0";
                if (hp.equals("")) hp = "999999999";
                if (!carseable.equals("") && !carpricelow.equals("") &&
                        !carpricehig.equals("") && !carsize.equals("")) {
                    if (!isInt(lp) || !isInt(hp)) {
                        Toast.makeText(getApplicationContext(), "价格应该是数值", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!size.equals("S") && !size.equals("M") && !size.equals("L")) {
                        Toast.makeText(getApplicationContext(), "大小应该是S M L", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        int pricel = Integer.parseInt(lp);
                        int priceh = Integer.parseInt(hp);
                        Intent it = new Intent();
                        it.putExtra("type", "CondititionSearch");
                        it.putExtra("seablename", carseable.toString());
                        it.putExtra("pricelow", pricel);
                        it.putExtra("pricehig", priceh);
                        it.putExtra("size", size);
                        it.setClass(ConditionSearch.this, Result.class);
                        ConditionSearch.this.startActivity(it);
                        ConditionSearch.this.finish();
                    }
                }
            }
        });


    }

    private boolean isInt(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {

            } else {
                return false;
            }
        }
        return true;
    }

}
