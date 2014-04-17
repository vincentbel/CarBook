package com.Doric.CarBook.member;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
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


public class Register extends Activity implements View.OnClickListener {

    //服务器请求相关变量
    private String url = Static.BASE_URL + "/login.php";  //登录请求的url,务必加上http://或https://
    private List<NameValuePair> registerParams;    //登录时发送给服务器的数据
    private JSONObject registerInfo;       //向服务器请求得到的json对象

    //定义控件
    private ProgressDialog progressDialog;   //异步任务时显示的进度条
    private EditText edtUsername, edtPassword, edtEnsurePsd, edtEmail;
    private Button btnRegister;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //设置控件
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        edtEnsurePsd = (EditText) findViewById(R.id.ensure_password);
        edtEmail = (EditText) findViewById(R.id.email);
        btnRegister = (Button) findViewById(R.id.register);

        //添加监听器
        btnRegister.setOnClickListener(this);

        //设置Actionbar
        getActionBar().setTitle("注册账号");

        android.app.ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onClick(View v) {
        int id = v.getId();

        //“注册”按钮
        if (id == R.id.register) {
            //获取用户输入的信息
            String name = edtUsername.getText().toString();
            String psd = edtPassword.getText().toString();
            String enPsd = edtEnsurePsd.getText().toString();
            String emailAddress = edtEmail.getText().toString();

            //判断用户名是否为空
            if (name.equals("")) {
                Toast.makeText(Register.this, "请输入用户名", Toast.LENGTH_LONG).show();
            }
            //判断用户名是否过长
            else if (name.length() > 16) {
                Toast.makeText(Register.this, "用户名过长，请重新输入", Toast.LENGTH_LONG).show();
            }
            //判断用户名是否过短
            else if (name.length() < 6) {
                Toast.makeText(Register.this, "用户名过短，请重新输入", Toast.LENGTH_LONG).show();
            }
            //判断密码是否为空
            else if (psd.equals("")) {
                Toast.makeText(Register.this, "请输入密码", Toast.LENGTH_LONG).show();
            }
            //判断密码是否过长
            else if (psd.length() > 16) {
                Toast.makeText(Register.this, "密码过长，请重新输入", Toast.LENGTH_LONG).show();
            }
            //判断密码是否过短
            else if (psd.length() < 6) {
                Toast.makeText(Register.this, "密码过短，请重新输入", Toast.LENGTH_LONG).show();
            }
            //判断验证密码是否为空
            else if (enPsd.equals("")) {
                Toast.makeText(Register.this, "请再次输入您的密码", Toast.LENGTH_LONG).show();
            }
            //判断验证密码是否正确
            else if (!psd.equals(enPsd)) {
                Toast.makeText(Register.this, "两次输入的密码不同，请重新输入?", Toast.LENGTH_LONG).show();
            }
            //判断邮箱是否为空
            else if (emailAddress.equals("")) {
                Toast.makeText(Register.this, "请输入您的邮箱地址?", Toast.LENGTH_LONG).show();
            }
            //判断邮箱格式是否合规范
            else if (!check_email(emailAddress)) {
                Toast.makeText(Register.this, "您的邮箱地址不正确，请重新输入", Toast.LENGTH_LONG).show();
            }

            //发送用户信息到服务器
            else {

                //发送用户信息到服务器
                registerParams = new ArrayList<NameValuePair>();
                registerParams.add(new BasicNameValuePair("tag", "login"));
                registerParams.add(new BasicNameValuePair("username", name));
                registerParams.add(new BasicNameValuePair("password", psd));

                //异步任务判断用户是否登录成功
                new registerUser().execute();
            }
        }
    }

    //验证邮箱格式
    public boolean check_email(String ead) {
        int len = ead.length();
        int i;
        for (i = 0; i < len; i++) {
            if (ead.charAt(i) == '@') {
                return true;
            }
        }
        return false;
    }

    private class registerUser extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute()
        {
            super.onPreExecute();
            //弹出"正在注册"框
            progressDialog = new ProgressDialog(Register.this);
            progressDialog.setMessage("正在注册..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params)
        {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            registerInfo = jsonParser.getJSONFromUrl(url, registerParams);;

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            //判断收到的json是否为空
            if (registerInfo != null) {
                try {
                    //注册成功
                    if ( registerInfo.getString("error").equals("0")) {
                        //跳转至个人中心
                        Intent intent = new Intent(Register.this,PersonalCenter.class);
                        startActivity(intent);
                    }
                    //发生错误
                    else if ( registerInfo.getString("error").equals("1") ){
                        Toast.makeText(Register.this, "注册失败", Toast.LENGTH_LONG).show();
                    }
                    //用户名已存在
                    else {
                        Toast.makeText(Register.this, "用户名已存在，请重新输入", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Toast.makeText(Register.this, "注册失败，请检查您的网络是否正常", Toast.LENGTH_LONG).show();
            }

        }
    }
}