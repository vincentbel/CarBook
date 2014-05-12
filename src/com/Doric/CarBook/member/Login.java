package com.Doric.CarBook.member;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Login extends Activity implements OnClickListener {

    //服务器请求相关变量
    private String url = Static.BASE_URL + "/login.php";  //登录请求的url,务必加上http://或https://
    private List<NameValuePair> loginParams;    //登录时发送给服务器的数据
    private JSONObject loginInfo;       //向服务器请求得到的json对象

    //定义控件
    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister;
    private ProgressDialog progressDialog;   //异步任务时显示的进度条

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //设置控件
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login);
        btnRegister = (Button) findViewById(R.id.register);

        //添加监听器
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //设置Actionbar
        getActionBar().setTitle("登录");
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onClick(View v) {

        int id = v.getId();
        //获取用户名密码
        String name = edtUsername.getText().toString();
        String psd = edtPassword.getText().toString();

        //“注册”按钮
        if (id == R.id.register) {
            //从登陆界面转到注册界面时将用户名和密码传过去，减少用户输入
            Intent intent = new Intent(Login.this, Register.class);
            intent.putExtra("username", name);
            intent.putExtra("password", psd);
            startActivity(intent);
        }

        //“登陆”按钮
        if (id == R.id.login) {

            //判断用户名是否为空
            if (name.equals("")) {
                Toast.makeText(Login.this, "请输入用户名", Toast.LENGTH_LONG).show();
            }
            //判断密码是否为空
            else if (psd.equals("")) {
                Toast.makeText(Login.this, "请输入密码", Toast.LENGTH_LONG).show();
            } else {
                //发送用户信息到服务器
                loginParams = new ArrayList<NameValuePair>();
                loginParams.add(new BasicNameValuePair("tag", "login"));
                loginParams.add(new BasicNameValuePair("username", name));
                loginParams.add(new BasicNameValuePair("password", psd));

                //异步任务判断用户是否登录成功
                new CheckUser().execute();
            }
        }
    }

    private class CheckUser extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //弹出"正在登录"框
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setMessage("正在登录..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            loginInfo = jsonParser.getJSONFromUrl(url, loginParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            //判断收到的json是否为空
            if (loginInfo != null) {
                try {
                    //账户信息验证失败
                    if (loginInfo.getString("success").equals("0")) {
                        Toast.makeText(Login.this, "用户名或密码错误，请重新输入", Toast.LENGTH_LONG).show();
                    }
                    //账户信息验证成功
                    else {

                        Toast.makeText(Login.this, "登录成功！", Toast.LENGTH_LONG).show();

                        //获取用户名
                        String name = edtUsername.getText().toString();

                        //跳转至个人中心
                        Intent intent = new Intent(Login.this, PersonalCenter.class);
                        intent.putExtra("name", name);
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(Login.this, "验证失败，请检查您的网络是否正常", Toast.LENGTH_LONG).show();
            }
        }
    }
}
