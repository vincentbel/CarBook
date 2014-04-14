package com.Doric.CarBook.member;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.Doric.CarBook.R;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Register extends Activity implements View.OnClickListener {

    EditText edt_username, edt_password, edt_ensurePsd, edt_email;
    Button btn_register;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //设置控件
        edt_username = (EditText) findViewById(R.id.username);
        edt_password = (EditText) findViewById(R.id.password);
        edt_ensurePsd = (EditText) findViewById(R.id.ensure_password);
        edt_email = (EditText) findViewById(R.id.email);
        btn_register = (Button) findViewById(R.id.register);

        //添加监听器
        btn_register.setOnClickListener(this);

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
            String name = edt_username.getText().toString();
            String psd = edt_password.getText().toString();
            String enPsd = edt_ensurePsd.getText().toString();
            String emailAddress = edt_email.getText().toString();

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
            else if (psd.equals(enPsd)) {
                Toast.makeText(Register.this, "两次输入的密码不同，请重新输入?", Toast.LENGTH_LONG).show();
            }
            //判断邮箱是否为空
            else if (emailAddress.equals("")) {
                Toast.makeText(Register.this, "请输入您的邮箱地址?", Toast.LENGTH_LONG).show();
            }
            //判断邮箱格式是否合规范
            else if (!check_email(emailAddress)) {
                Toast.makeText(Register.this, "您的邮箱地址不正确，请重新输入", Toast.LENGTH_LONG).show();
            } else {
                //发送用户信息到服务器
                String url = "";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", name));
                params.add(new BasicNameValuePair("password", psd));
                params.add(new BasicNameValuePair("email", emailAddress));
                JSONParser jsonParser = new JSONParser();
                JSONObject loginInfo = jsonParser.getJSONFromUrl(url, params);
            }
        }
    }

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
}
