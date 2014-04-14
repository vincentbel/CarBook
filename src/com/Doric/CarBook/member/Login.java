package com.Doric.CarBook.member;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.Doric.CarBook.R;
import com.Doric.CarBook.Static;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Login extends Activity implements OnClickListener {

    String url = Static.BASE_URL + "/login.php";  //登录请求的url,务必加上http://或https://
    List<NameValuePair> loginParams;    //登录时发送给服务器的数据
    JSONObject loginInfo;       //向服务器请求得到的json对象
    private EditText edt_username, edt_password;
    private Button btn_login, btn_register;
    private ProgressDialog progressDialog;   //异步任务时显示的进度条

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //设置控件
        edt_username = (EditText) findViewById(R.id.username);
        edt_password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.login);
        btn_register = (Button) findViewById(R.id.register);

        //添加监听器
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        //设置Actionbar
        getActionBar().setTitle("登录");

        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public void onClick(View v) {

        int id = v.getId();

        //“注册”按钮
        if (id == R.id.register) {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
        }

        //“登陆”按钮
        if (id == R.id.login) {
            //获取用户名密码
            String name = edt_username.getText().toString();
            String psd = edt_password.getText().toString();

            //判断用户名是否为空
            if (name.equals("")) {
                Toast.makeText(Login.this, "请输入用户名", Toast.LENGTH_LONG).show();
            }
            //判断密码是否为空
            else if (psd.equals("")) {
                Toast.makeText(Login.this, "请输入密码", Toast.LENGTH_LONG).show();
            }

            //发送用户信息到服务器
            loginParams = new ArrayList<NameValuePair>();
            loginParams.add(new BasicNameValuePair("tag", "login"));
            loginParams.add(new BasicNameValuePair("username", name));
            loginParams.add(new BasicNameValuePair("password", psd));

            //异步任务判断用户是否登录成功
            new CheckUser().execute();

            //验证用户名密码
            if (true) {

            }

            //若用户名密码错误
            else {
                Toast.makeText(Login.this, "用户名或密码错误，请重新输入", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class CheckUser extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            JSONParser jsonParser = new JSONParser();
            loginInfo = jsonParser.getJSONFromUrl(url, loginParams);
            Log.d("Response: ", "" + loginInfo);

            if (loginInfo != null) {
                Log.e("JSON", loginInfo.toString());
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
}
