package com.Doric.CarBook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.Doric.CarBook.member.Login;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //µÇÂ¼²âÊÔ
        Button loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
        /*
        //Æû³µÕ¹Ê¾²âÊÔ
        Button carShowButton = (Button) findViewById(R.id.carShow);
        carShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("carID", "123");
                intent.setClass(MainActivity.this, CarShow.class);
                startActivity(intent);
            }
        });

        //ËÑË÷²âÊÔ
        Button searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AlphaShow.class);
                startActivity(intent);
            }
        });
        */
    }
}
