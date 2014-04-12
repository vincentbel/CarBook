package com.Doric.CarBook;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.CarBook_master.R;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button testButton = (Button) findViewById(R.id.testButton);
        testButton.setText("TestButton");
        testButton.setOnClickListener(new myButtonListener());
    }

    class myButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("carID", "123");
            intent.setClass(MainActivity.this, CarShow.class);
            MainActivity.this.startActivity(intent);
        }
    }
}
