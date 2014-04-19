package com.Doric.CarBook.member;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.Doric.CarBook.R;

/**
 * Created by Sunyao_Will on 2014/4/16.
 */
public class UserCollection extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_collection);
        /*
            接收用户名
         */
        Intent intent = getIntent();
        intent.getStringExtra("userName");
        /*
            通过Json构造用户的所有收藏
            String url = "";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", userCollection));
            params.add(new BasicNameValuePair("username", name));
            JSONParser jsonParser = new JSONParser();
            JSONObject loginInfo = jsonParser.getJSONFromUrl(url, params);
         */
        /*
            动态添加收藏条目
         */
        LinearLayout baseLinearLayout = (LinearLayout) findViewById(R.id.baseLinearLayout);
        int userCollectionCount =2;
    }
}
