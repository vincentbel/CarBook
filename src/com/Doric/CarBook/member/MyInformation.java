package com.Doric.CarBook.member;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.Doric.CarBook.R;

public class MyInformation extends Activity implements View.OnClickListener {

    //定义控件
    private Button btnHead, btnName, btnSex, btnLogOut;

    //定义变量
    private String name = "暂无", sex = null;
    private String[] sexes = new String[]{"男", "女"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);

        //设置控件
        btnHead = (Button) findViewById(R.id.head);
        btnSex = (Button) findViewById(R.id.sex);
        btnLogOut = (Button) findViewById(R.id.log_out);

        //添加监听器
        btnHead.setOnClickListener(this);
        btnSex.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        //设置Actionbar
        getActionBar().setTitle("我的资料");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //取得启动该Activity的Intent对象
        Intent intent = getIntent();

        //取出Intent中附加的数据
        name = intent.getStringExtra("name");
    }

    public void onClick(View v) {
        int id = v.getId();

        //"头像"按钮
        if (id == R.id.head) {

        }

        //"性别"按钮
        if (id == R.id.sex) {
            new AlertDialog.Builder(MyInformation.this) // build AlertDialog
                    .setTitle("选择性别") // title
                    .setItems(sexes, new DialogInterface.OnClickListener() { //content
                        public void onClick(DialogInterface dialog, int which) {
                            sex = sexes[which];
                        }
                    })
                    .show();
        }
        //"退出登录"按钮
        if (id == R.id.log_out) {
            logOutDialog();
        }
    }

    //退出登录对话框
    public void logOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyInformation.this);
        builder.setMessage("确定要退出登录吗？");
        builder.setTitle("退出登录");
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MyInformation.this.finish();
            }
        });
        builder.create().show();
    }
}

