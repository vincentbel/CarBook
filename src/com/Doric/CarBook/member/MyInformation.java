package com.Doric.CarBook.member;

import android.app.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.MainActivity;
import com.Doric.CarBook.R;
import com.Doric.CarBook.car.HotCarShow;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class MyInformation extends Activity implements View.OnClickListener {

    //服务器请求相关变量
    private String sexURL = Constant.BASE_URL + "/sex.php";  //登录请求的url,务必加上http://或https://
    private List<NameValuePair> sexParams;    //登录时发送给服务器的数据
    private JSONObject sexInfo;       //向服务器请求得到的json对象

    private String headURL = Constant.BASE_URL + "/user_setting.php";  //登录请求的url,务必加上http://或https://
    private List<NameValuePair> headParams;    //登录时发送给服务器的数据
    private JSONObject headInfo;       //向服务器请求得到的json对象

    private String informationURL = Constant.BASE_URL + "/user_setting.php";  //登录请求的url,务必加上http://或https://
    private List<NameValuePair> informationParams;    //登录时发送给服务器的数据
    private JSONObject informationInfo;       //向服务器请求得到的json对象

    private UserFunctions userFunctions;

    //定义控件
    private ProgressDialog progressDialog;   //异步任务时显示的进度条
    private RelativeLayout loHead, loSex, loUsername;
    private Button btnLogOut, btnBack;
    private TextView tvUsername;

    //定义变量
    private String name = "暂无", sex = "男";
    private String[] sexes = new String[]{"男", "女"};

    //设置头像所用变量
    private String whichHead = "0";
    private ImageView currentHead = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);

        userFunctions = new UserFunctions(getApplicationContext());

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

        //获取用户信息
        informationParams = new ArrayList<NameValuePair>();
        informationParams.add(new BasicNameValuePair("tag", "get_avatar"));
        informationParams.add(new BasicNameValuePair("username", name));

        //异步任务
        //new getInformation().execute();
        setHead();
    }

    //为图像添加边框
    public void handleImageView(ImageView imageView){
        currentHead.setImageDrawable(null);
        imageView.setImageResource(R.drawable.head_border);
        currentHead = imageView;
    }

    //设置头像
    public void setHead() {
        ImageView imageHead = (ImageView) findViewById(R.id.head_image);
        imageHead.setBackgroundResource(userFunctions.getUserAvatarResource());
    }

    public void onClick(View v) {
        int id = v.getId();

        //"头像"按钮
        if (id == R.id.head_layout) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.choose_head, (ViewGroup) findViewById(R.id.chooseHead));
            AlertDialog headBuilder =  new AlertDialog.Builder(this)
                    .setTitle("自定义头像")
                    .setView(layout)
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //发送用户信息到服务器
                            headParams = new ArrayList<NameValuePair>();
                            headParams.add(new BasicNameValuePair("tag", "update_avatar"));
                            headParams.add(new BasicNameValuePair("user_id", name));
                            headParams.add(new BasicNameValuePair("status", whichHead));

                            //异步任务
                            new changeHead().execute();
                        }
                    })
                    .show();

            final ImageView imageHead1 = (ImageView) layout.findViewById(R.id.head1);
            final ImageView imageHead2 = (ImageView) layout.findViewById(R.id.head2);
            final ImageView imageHead3 = (ImageView) layout.findViewById(R.id.head3);
            final ImageView imageHead4 = (ImageView) layout.findViewById(R.id.head4);
            final ImageView imageHead5 = (ImageView) layout.findViewById(R.id.head5);
            final ImageView imageHead6 = (ImageView) layout.findViewById(R.id.head6);
            final ImageView imageHead7 = (ImageView) layout.findViewById(R.id.head7);
            final ImageView imageHead8 = (ImageView) layout.findViewById(R.id.head8);
            final ImageView imageHead9 = (ImageView) layout.findViewById(R.id.head9);

            //默认给第一个头像添加边框
            imageHead1.setImageResource(R.drawable.head_border);
            currentHead = imageHead1;
            whichHead = "1";

            //添加监听器
            imageHead1.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) { whichHead = "1"; handleImageView(imageHead1);}
            });
            imageHead2.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) { whichHead = "2"; handleImageView(imageHead2);}
            });
            imageHead3.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) { whichHead = "3"; handleImageView(imageHead3);}
            });
            imageHead4.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) { whichHead = "4"; handleImageView(imageHead4);}
            });
            imageHead5.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) { whichHead = "5"; handleImageView(imageHead5);}
            });
            imageHead6.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) { whichHead = "6"; handleImageView(imageHead6);}
            });
            imageHead7.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) { whichHead = "7"; handleImageView(imageHead7);}
            });
            imageHead8.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) { whichHead = "8"; handleImageView(imageHead8);}
            });
            imageHead9.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) { whichHead = "9"; handleImageView(imageHead9);}
            });
        }

        //"性别"按钮
        if (id == R.id.sex_layout) {
            new AlertDialog.Builder(MyInformation.this) // build AlertDialog
                    .setTitle("选择性别") // title
                    .setItems(sexes, new DialogInterface.OnClickListener() { //content
                        public void onClick(DialogInterface dialog, int which) {
                            //判断是否修改了性别
                            if (!sexes[which].equals(sex)) {
                                /*
                                //发送用户信息到服务器
                                sexParams = new ArrayList<NameValuePair>();
                                sexParams.add(new BasicNameValuePair("tag", "sex"));
                                sexParams.add(new BasicNameValuePair("username", name));
                                sexParams.add(new BasicNameValuePair("sex", sex));

                                //异步任务
                                new changeSex().execute();
                                */
                                if (sex.equals("男")) {
                                    sex = "女";
                                } else {
                                    sex = "男";
                                }
                                TextView tvSex = (TextView) findViewById(R.id.sex_text2);
                                tvSex.setText(sex);
                                Toast.makeText(MyInformation.this, "性别修改成功", Toast.LENGTH_LONG).show();
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
                userFunctions.logoutUser();
                dialog.dismiss();
                startActivity(new Intent(MyInformation.this, MainActivity.class));
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


    //获取用户信息的异步任务
    private class getInformation extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            informationInfo = jsonParser.getJSONFromUrl(informationURL, informationParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //判断收到的json是否为空
            if (informationInfo != null) {
                try {
                    if (informationInfo.getString("success").equals("1")) {
                        //获取头像
                        whichHead = informationInfo.getString("status");
                        setHead();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //修改头像
    private class changeHead extends AsyncTask<Void, Void, Boolean> {

        protected void onPreExecute() {
            super.onPreExecute();
            //弹出"正在修改"框
            progressDialog = new ProgressDialog(MyInformation.this);
            progressDialog.setMessage("正在修改..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Boolean doInBackground(Void... params) {
            return userFunctions.setUserAvatar(name, Integer.parseInt(whichHead));
        }

        protected void onPostExecute(Boolean result) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //判断收到的json是否为空
            if (result) {
                        Toast.makeText(MyInformation.this, "头像修改成功", Toast.LENGTH_LONG).show();
                        setHead();
            } else {
                Toast.makeText(MyInformation.this, getResources().getString(R.string.no_internet_connection),Toast.LENGTH_LONG).show();
            }
        }
    }

    //修改性别
    private class changeSex extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            sexInfo = jsonParser.getJSONFromUrl(sexURL, sexParams);
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
                    if (sexInfo.getString("success").equals("1")) {
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
