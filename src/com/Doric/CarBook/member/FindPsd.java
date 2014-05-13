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
import com.Doric.CarBook.MainActivity;
import com.Doric.CarBook.R;
import com.Doric.CarBook.Static;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindPsd  extends Activity implements View.OnClickListener {

    //服务器请求相关变量
    private String url = Static.BASE_URL + "/findPsd.php";  //登录请求的url,务必加上http://或https://
    private List<NameValuePair> findPsdParams;    //登录时发送给服务器的数据
    private JSONObject findPsdInfo;       //向服务器请求得到的json对象

    //定义控件
    private Button btnSubmit, btnBack;
    private ProgressDialog progressDialog;   //异步任务时显示的进度条
    private EditText edtUsername,edtEmailAddress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_psd);

        //设置控件
        btnSubmit = (Button) findViewById(R.id.submit);
        btnBack = (Button) findViewById(R.id.back);
        edtUsername = (EditText)findViewById(R.id.username);
        edtEmailAddress = (EditText)findViewById(R.id.email);

        //添加监听器
        btnSubmit.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //隐藏Actionbar
        getActionBar().hide();
    }

    public void onClick(View v) {
        int id = v.getId();

        //"返回"按钮
        if (id == R.id.back) {
            FindPsd.this.finish();
        }

        //"提交"按钮
        if( id == R.id.submit) {

            //获取用户名邮箱
            String name = edtUsername.getText().toString();
            String email = edtEmailAddress.getText().toString();

            //判断用户名是否为空
            if (name.equals("")) {
                Toast.makeText(FindPsd.this, "请输入用户名", Toast.LENGTH_LONG).show();
            }
            //判断密码是否为空
            else if (email.equals("")) {
                Toast.makeText(FindPsd.this, "请输入邮箱", Toast.LENGTH_LONG).show();
            }
            else if ( check_email(email) )  {
                //发送用户信息到服务器
                findPsdParams = new ArrayList<NameValuePair>();
                findPsdParams.add(new BasicNameValuePair("tag", "findPsd"));
                findPsdParams.add(new BasicNameValuePair("username", name));
                findPsdParams.add(new BasicNameValuePair("email", email));

                //异步任务判断用户是否登录成功
                new CheckUser().execute();
            }
            else {
                Toast.makeText(FindPsd.this, "您的邮箱地址不正确，请重新输入", Toast.LENGTH_LONG).show();
            }
        }
    }


    private class CheckUser extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //弹出"正在验证"框
            progressDialog = new ProgressDialog(FindPsd.this);
            progressDialog.setMessage("正在验证..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            findPsdInfo = jsonParser.getJSONFromUrl(url, findPsdParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //判断收到的json是否为空
            if (findPsdInfo != null) {
                try {
                    //验证失败
                    if (findPsdInfo.getString("success").equals("0")) {
                        Toast.makeText(FindPsd.this, "验证失败，请重新输入", Toast.LENGTH_LONG).show();
                    }
                    //账户信息验证成功
                    else {
                        Toast.makeText(FindPsd.this, "验证成功！", Toast.LENGTH_LONG).show();
                        FindPsd.this.finish();
                        Intent intent = new Intent(FindPsd.this,FindPsd.class);
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(FindPsd.this, "验证失败，请检查您的网络是否正常", Toast.LENGTH_LONG).show();
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
}