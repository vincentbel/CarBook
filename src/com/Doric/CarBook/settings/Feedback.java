package com.Doric.CarBook.settings;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.Doric.CarBook.R;
import org.json.JSONException;
import org.json.JSONObject;


public class Feedback extends Activity implements OnClickListener {

    String username;    //用户名
    String password;    //密码
    private JSONObject loginInfo;       //向服务器请求得到的json对象
    //定义控件
    private EditText edtContent;
    private Button btnSend, btnBack;
    private ProgressDialog progressDialog;   //异步任务时显示的进度条

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        //设置控件
        edtContent = (EditText) findViewById(R.id.content);
        btnSend = (Button) findViewById(R.id.send);
        btnBack = (Button) findViewById(R.id.back);

        //添加监听器
        btnSend.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //隐藏Actionbar
        getActionBar().hide();
    }

    public void onClick(View v) {

        int id = v.getId();

        //“发送”按钮
        if (id == R.id.send) {

            //判断输入是否为空
            if (edtContent.getText().equals("")) {
                Toast.makeText(Feedback.this, "输入不能为空", Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(Feedback.this, "发送成功！", Toast.LENGTH_LONG).show();
                Feedback.this.finish();
            }
        }

        //"返回"按钮
        if (id == R.id.back) {
            Feedback.this.finish();
        }

    }

    private class SendContent extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //弹出"正在发送"框
            progressDialog = new ProgressDialog(Feedback.this);
            progressDialog.setMessage("正在发送..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {

            //向服务器发送请求
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
                        Toast.makeText(Feedback.this, "发生错误", Toast.LENGTH_LONG).show();
                    }
                    //账户信息验证成功
                    else {
                        Toast.makeText(Feedback.this, "意见反馈成功！", Toast.LENGTH_LONG).show();
                        Feedback.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(Feedback.this, "发送失败，请检查您的网络是否正常", Toast.LENGTH_LONG).show();
            }
        }
    }
}
