package com.Doric.CarBook.member;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

public class MyInformation extends Activity implements View.OnClickListener {

    //服务器请求相关变量
    private String url = Constant.BASE_URL + "/sex.php";  //登录请求的url,务必加上http://或https://
    private List<NameValuePair> sexParams;    //登录时发送给服务器的数据
    private JSONObject sexInfo;       //向服务器请求得到的json对象

    //定义控件
    private ProgressDialog progressDialog;   //异步任务时显示的进度条
    private RelativeLayout loHead, loSex, loUsername;
    private Button btnLogOut, btnBack;
    private TextView tvUsername;

    //定义变量
    private String name = "暂无", sex = "男";
    private String[] sexes = new String[]{"男", "女"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);

        //设置控件
        loHead = (RelativeLayout) findViewById(R.id.head_layout);
        loSex = (RelativeLayout) findViewById(R.id.sex_layout);
        loUsername = (RelativeLayout) findViewById(R.id.username_layout);

        btnLogOut = (Button) findViewById(R.id.button_log_out);
        btnBack = (Button) findViewById(R.id.back);
        tvUsername = (TextView) findViewById(R.id.username_text2);

        //添加监听器
        loHead.setOnClickListener(this);
        loSex.setOnClickListener(this);
        loUsername.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //隐藏Actionbar
        getActionBar().hide();

        //取得启动该Activity的Intent对象
        Intent intent = getIntent();

        //取出Intent中附加的数据
        if (intent.getStringExtra("name") != null) {
            name = intent.getStringExtra("name");
            tvUsername.setText(name);
        }
    }

    public void onClick(View v) {
        int id = v.getId();

        //"头像"按钮
        if (id == R.id.head_layout) {

        }

        //"性别"按钮
        if (id == R.id.sex_layout) {
            new AlertDialog.Builder(MyInformation.this) // build AlertDialog
                    .setTitle("选择性别") // title
                    .setItems(sexes, new DialogInterface.OnClickListener() { //content
                        public void onClick(DialogInterface dialog, int which) {
                            //判断是否修改了性别
                            if (!sexes[which].equals(sex)) {
                                //发送用户信息到服务器
                                sexParams = new ArrayList<NameValuePair>();
                                sexParams.add(new BasicNameValuePair("tag", "sex"));
                                sexParams.add(new BasicNameValuePair("username", name));
                                sexParams.add(new BasicNameValuePair("sex", sex));

                                //异步任务
                                new registerUser().execute();
                            }
                        }
                    })
                    .show();
        }

        //"退出登录"按钮
        if (id == R.id.button_log_out) {
            logOutDialog();
        }

        //"返回"按钮
        if (id == R.id.back) {
            MyInformation.this.finish();
        }
    }

    //退出登录对话框
    public void logOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyInformation.this);
        builder.setMessage("确定要退出登录吗？");
        builder.setTitle("退出登录");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MyInformation.this.finish();
            }
        });
        builder.create().show();
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
            //弹出"正在修改"框
            progressDialog = new ProgressDialog(MyInformation.this);
            progressDialog.setMessage("正在修改..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            sexInfo = jsonParser.getJSONFromUrl(url, sexParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //判断收到的json是否为空
            if (sexInfo != null) {
                try {
                    if (sexInfo.getString("error").equals("0")) {
                        if (sex.equals("男")) {
                            sex = "女";
                        } else {
                            sex = "男";
                        }
                        TextView tvSex = (TextView) findViewById(R.id.sex_text2);
                        tvSex.setText(sex);
                        Toast.makeText(MyInformation.this, "性别修改成功", Toast.LENGTH_LONG).show();
                    }
                    //发生错误
                    else {
                        Toast.makeText(MyInformation.this, "修改失败", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MyInformation.this, "修改失败，请检查您的网络是否正常", Toast.LENGTH_LONG).show();
            }
        }
    }

}
