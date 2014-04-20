package com.Doric.CarBook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.Doric.CarBook.car.CarShow;
import com.Doric.CarBook.member.Login;
import com.Doric.CarBook.member.UserCollection;
import com.Doric.CarBook.search.AlphaShow;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //µ«¬º≤‚ ‘
        Button loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });

        //∆˚≥µ’π æ≤‚ ‘
        Button carShowButton = (Button) findViewById(R.id.car_show);
        carShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("carID", "123");
                intent.setClass(MainActivity.this, CarShow.class);
                startActivity(intent);
            }
        });

        //À—À˜≤‚ ‘
        Button searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AlphaShow.class);
                startActivity(intent);
            }
        });
        // ’≤ÿº–≤‚ ‘
        Button userCollectionButton = (Button) findViewById(R.id.collection);
        userCollectionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ();
                intent.setClass(MainActivity.this, UserCollection.class);
                intent.putExtra("userName","defaultUser");
                startActivity(intent);
            }
        });
    }
}
