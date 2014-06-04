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
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FindPsd2 extends Activity implements View.OnClickListener {

    //服务器请求相关变量
    private String url = Constant.BASE_URL + "/changePsd.php";  //登录请求的url,务必加上http://或https://
    private List<NameValuePair> changePsdParams;    //登录时发送给服务器的数据
    private JSONObject changePsdInfo;       //向服务器请求得到的json对象

    private String name;

    //定义控件
    private Button btnSubmit, btnBack;
    private ProgressDialog progressDialog;   //异步任务时显示的进度条
    private EditText edtPsd,edtPsd2;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_psd);

        //设置控件
        btnSubmit = (Button) findViewById(R.id.submit);
        btnBack = (Button) findViewById(R.id.back);
        edtPsd = (EditText)findViewById(R.id.username);
        edtPsd2 = (EditText)findViewById(R.id.email);

        //添加监听器
        btnSubmit.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //隐藏Actionbar
        getActionBar().hide();

        //取得启动该Activity的Intent对象
        Intent intent =getIntent();

        //取出Intent中附加的数据
        if ( intent.getStringExtra("name") !=  null ) {
            name = intent.getStringExtra("name");
        }
    }

    public void onClick(View v) {
        int id = v.getId();

        //"返回"按钮
        if (id == R.id.back) {
            FindPsd2.this.finish();
        }

        //"提交"按钮
        if( id == R.id.submit) {

            //获取用户名邮箱
            String psd = edtPsd.getText().toString();
            String psd2 = edtPsd2.getText().toString();

            //判断用户名是否为空
            if (psd.equals("")) {
                Toast.makeText(FindPsd2.this, "请输入新密码", Toast.LENGTH_LONG).show();
            }
            //判断密码是否为空
            else if (psd2.equals("")) {
                Toast.makeText(FindPsd2.this, "请再一次输入新密码", Toast.LENGTH_LONG).show();
            }
            //判断两次输入的密码是否一致
            else if ( !psd.equals(psd2)) {
                Toast.makeText(FindPsd2.this, "两次输入的密码不同，请重新输入", Toast.LENGTH_LONG).show();
            }
            else{
                //发送用户信息到服务器
                changePsdParams = new ArrayList<NameValuePair>();
                changePsdParams.add(new BasicNameValuePair("tag", "changePsd"));
                changePsdParams.add(new BasicNameValuePair("username", name));
                changePsdParams.add(new BasicNameValuePair("password", psd));

                //异步任务判断用户是否登录成功
                new changePsd().execute();
            }
        }
    }


    private class changePsd extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //弹出"正在验证"框
            progressDialog = new ProgressDialog(FindPsd2.this);
            progressDialog.setMessage("正在修改..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            changePsdInfo = jsonParser.getJSONFromUrl(url, changePsdParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //判断收到的json是否为空
            if (changePsdInfo != null) {
                try {
                    //验证失败
                    if (changePsdInfo.getString("success").equals("0")) {
                        Toast.makeText(FindPsd2.this, "修改失败，请重新输入", Toast.LENGTH_LONG).show();
                    }
                    //账户信息验证成功
                    else {
                        Toast.makeText(FindPsd2.this, "修改成功！", Toast.LENGTH_LONG).show();
                        FindPsd2.this.finish();
                        Intent intent = new Intent(FindPsd2.this, Login.class);
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(FindPsd2.this, "修改失败，请检查您的网络是否正常", Toast.LENGTH_LONG).show();
            }
        }
    }
}