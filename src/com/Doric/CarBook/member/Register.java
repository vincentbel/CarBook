package com.Doric.CarBook.member;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.Doric.CarBook.MainActivity;
import com.Doric.CarBook.R;
import org.json.JSONException;
import org.json.JSONObject;


public class Register extends Activity implements View.OnClickListener {

    private JSONObject registerInfo;       //向服务器请求得到的json对象
    private UserFunctions userFunctions;

    //定义控件
    private ProgressDialog progressDialog;   //异步任务时显示的进度条
    private EditText edtUsername, edtPassword, edtEnsurePsd, edtEmail;
    private Button btnRegister, btnBack;
    private String username;
    private String password;
    private String emailAddress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        userFunctions = new UserFunctions(getApplicationContext());

        //设置控件
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        edtEnsurePsd = (EditText) findViewById(R.id.ensure_password);
        edtEmail = (EditText) findViewById(R.id.email);
        btnRegister = (Button) findViewById(R.id.register);
        btnBack = (Button) findViewById(R.id.back);

        //添加监听器
        btnRegister.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //隐藏Actionbar
        getActionBar().hide();

        Intent intent = getIntent();
        edtUsername.setText(intent.getStringExtra("username"));
        edtPassword.setText(intent.getStringExtra("password"));
    }

    public void onClick(View v) {
        int id = v.getId();

        //“注册”按钮
        if (id == R.id.register) {
            //获取用户输入的信息
            username = edtUsername.getText().toString();
            password = edtPassword.getText().toString();
            String enPassword = edtEnsurePsd.getText().toString();
            emailAddress = edtEmail.getText().toString();

            //判断用户名是否为空
            if (username.equals("")) {
                Toast.makeText(Register.this, "请输入用户名", Toast.LENGTH_LONG).show();
            }
            //判断用户名是否过长
            else if (username.length() > 16) {
                Toast.makeText(Register.this, "用户名过长，请重新输入", Toast.LENGTH_LONG).show();
            }
            //判断用户名是否过短
            else if (username.length() < 6) {
                Toast.makeText(Register.this, "用户名过短，请重新输入", Toast.LENGTH_LONG).show();
            }
            //判断密码是否为空
            else if (password.equals("")) {
                Toast.makeText(Register.this, "请输入密码", Toast.LENGTH_LONG).show();
            }
            //判断密码是否过长
            else if (password.length() > 16) {
                Toast.makeText(Register.this, "密码过长，请重新输入", Toast.LENGTH_LONG).show();
            }
            //判断密码是否过短
            else if (password.length() < 6) {
                Toast.makeText(Register.this, "密码过短，请重新输入", Toast.LENGTH_LONG).show();
            }
            //判断验证密码是否为空
            else if (enPassword.equals("")) {
                Toast.makeText(Register.this, "请再次输入您的密码", Toast.LENGTH_LONG).show();
            }
            //判断验证密码是否正确
            else if (!password.equals(enPassword)) {
                Toast.makeText(Register.this, "两次输入的密码不同，请重新输入", Toast.LENGTH_LONG).show();
            }
            //判断邮箱是否为空
            else if (emailAddress.equals("")) {
                Toast.makeText(Register.this, "请输入您的邮箱地址", Toast.LENGTH_LONG).show();
            }
            //判断邮箱格式是否合规范
            else if (!check_email(emailAddress)) {
                Toast.makeText(Register.this, "您的邮箱地址不正确，请重新输入", Toast.LENGTH_LONG).show();
            }

            //发送用户信息到服务器
            else {
                //异步任务判断用户是否登录成功
                new registerUser().execute();
            }
        }

        //"返回"按钮
        if (id == R.id.back) {
            Register.this.finish();
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

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class registerUser extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //弹出"正在注册"框
            progressDialog = new ProgressDialog(Register.this);
            progressDialog.setMessage("正在注册..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            registerInfo = userFunctions.registerUser(username, emailAddress, password);
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
                    if (registerInfo.getString("error").equals("0")) {
                        //跳转至个人中心
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                    }
                    //发生错误
                    else if (registerInfo.getString("error").equals("1")) {
                        Toast.makeText(Register.this, "注册失败", Toast.LENGTH_LONG).show();
                    }
                    //用户名已存在
                    else if (registerInfo.getString("error").equals("2")) {
                        Toast.makeText(Register.this, "用户名已存在，请重新输入", Toast.LENGTH_LONG).show();
                    }
                    //邮箱已被使用
                    else {
                        Toast.makeText(Register.this, "邮箱已存在，请重新输入", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(Register.this, "注册失败，请检查您的网络是否正常", Toast.LENGTH_LONG).show();
            }
        }
    }
}
