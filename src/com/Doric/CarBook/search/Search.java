package com.Doric.CarBook.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.Doric.CarBook.R;


public class Search extends Activity {
    private EditText mEditText;
    private Button mButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search);
        mEditText = (EditText) findViewById(R.id.EditText);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Editable str = mEditText.getText();
                if (!str.equals("")) {
                    Intent it = new Intent();
                    //ArrayList<Car> mCarList =PinyinSearch.search(CarSeableData.mCarSeable, str.toString());
                    it.putExtra("type", "Search");
                    it.putExtra("KeyWord", str.toString());
                    it.setClass(Search.this, Result.class);
                    Search.this.startActivity(it);
                    Search.this.finish();

                }

            }

        });
    }
}
