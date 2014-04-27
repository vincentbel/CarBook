package com.Doric.CarBook.member;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.Doric.CarBook.MainActivity;
import com.Doric.CarBook.R;

public class PersonalCenter extends Activity implements View.OnClickListener {

    //定义控件
    private Button btnInformation,btnComment, btnFavourite,btnLogOut,btnBack;
    private TextView tvUsername;

    //定义变量
    private String name;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_center);

        //设置控件
        btnFavourite = (Button) findViewById(R.id.name);
        btnInformation = (Button) findViewById(R.id.head);
        btnComment = (Button) findViewById(R.id.sex);
        btnLogOut = (Button) findViewById(R.id.log_out);
        btnBack = (Button) findViewById(R.id.back);
        tvUsername = ( TextView ) findViewById(R.id.bar_username);

        //添加监听器
        btnComment.setOnClickListener(this);
        btnFavourite.setOnClickListener(this);
        btnInformation.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //隐藏Actionbar
        getActionBar().hide();

        //取得启动该Activity的Intent对象
        Intent intent =getIntent();

        //取出Intent中附加的数据
        if ( intent.getStringExtra("name") !=  null ) {
            name = intent.getStringExtra("name");
            tvUsername.setText(name);
        }
    }

    public void onClick(View v) {
        int id = v.getId();

        //"我的资料"按钮
        if (id == R.id.head) {
            Intent intent = new Intent(PersonalCenter.this, MyInformation.class);
            intent.putExtra("name",name);
            startActivity(intent);
        }

        //"我的评论"按钮
        if (id == R.id.sex) {
            /*Intent intent = new Intent(PersonalCenter.this, comment.class);
            intent.putExtra("name",name);
             startActivity(intent);*/
        }

        //"我的收藏"按钮
        if (id == R.id.name) {
            Intent intent = new Intent(PersonalCenter.this, UserCollection.class);
            intent.putExtra("name",name);
            startActivity(intent);
        }

        //"退出登录"按钮
        if (id == R.id.log_out) {
            logOutDialog();
        }

        //"返回"按钮
        if (id == R.id.back) {
            PersonalCenter.this.finish();
        }
    }

    //退出登录对话框
    public void logOutDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalCenter.this);
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
                PersonalCenter.this.finish();
            }
        });
        builder.create().show();
     }

}
