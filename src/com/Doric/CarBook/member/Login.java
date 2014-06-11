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
import android.widget.TextView;
import android.widget.Toast;
import com.Doric.CarBook.MainActivity;
import com.Doric.CarBook.R;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends Activity implements OnClickListener {

    UserFunctions userFunctions;        //用户功能类
    String username;    //用户名
    String password;    //密码
    private JSONObject loginInfo;       //向服务器请求得到的json对象
    //定义控件
    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister, btnBack;
    private ProgressDialog progressDialog;   //异步任务时显示的进度条
    private TextView tvFindPsd;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userFunctions = new UserFunctions(getApplicationContext());

        //设置控件
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login);
        btnRegister = (Button) findViewById(R.id.register);
        btnBack = (Button) findViewById(R.id.back);
        tvFindPsd = (TextView) findViewById(R.id.forget_psd);

        //添加监听器
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        tvFindPsd.setOnClickListener(this);

        //隐藏Actionbar
        getActionBar().hide();
    }

    public void onClick(View v) {

        int id = v.getId();

        //获取用户名密码
        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();

        //“注册”按钮
        if (id == R.id.register) {
            // 从登陆界面转到注册界面时将用户名和密码传过去，减少用户输入
            Intent intent = new Intent(Login.this, Register.class);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);
        }

        //“登陆”按钮
        if (id == R.id.login) {

            //判断用户名是否为空
            if (username.equals("")) {
                Toast.makeText(Login.this, "请输入用户名", Toast.LENGTH_LONG).show();
            }
            //判断密码是否为空
            else if (password.equals("")) {
                Toast.makeText(Login.this, "请输入密码", Toast.LENGTH_LONG).show();
            } else {
                //异步任务判断用户是否登录成功
                new CheckUser().execute();
            }
        }

        //"返回"按钮
        if (id == R.id.back) {
            Login.this.finish();
        }

        //"忘记密码？"按钮
        if (id == R.id.forget_psd) {
            Intent intent = new Intent(Login.this, FindPsd.class);
            startActivity(intent);
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
            loginInfo = userFunctions.loginUser(username, password);
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

                        //跳转至主界面
                        Login.this.finish();
                        Intent intent = new Intent(Login.this, MainActivity.class);
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
