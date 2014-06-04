package com.Doric.CarBook.member;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;

public class MyComments extends Activity {

    UserFunctions userFunctions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userFunctions = new UserFunctions(getApplicationContext());
        setContentView(R.layout.my_comments);
    }

    private class GetMyComments extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
