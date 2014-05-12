package com.Doric.CarBook.member;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.Doric.CarBook.R;

public class PersonalCenter extends Fragment implements View.OnClickListener {

    //定义控件
    private Button btnInformation, btnComment, btnFavourite, btnLogOut;

    //定义变量
    private String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.personal_center, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mView = getView();

        //设置控件
        btnFavourite = (Button) mView.findViewById(R.id.name);
        btnInformation = (Button) mView.findViewById(R.id.head);
        btnComment = (Button) mView.findViewById(R.id.sex);
        btnLogOut = (Button) mView.findViewById(R.id.log_out);

        //添加监听器
        btnComment.setOnClickListener(this);
        btnFavourite.setOnClickListener(this);
        btnInformation.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        //设置Actionbar
        //getActionBar().setTitle("我的主页");
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        //取得启动该Activity的Intent对象
        //Intent intent =getIntent();

        //取出Intent中附加的数据
        name = getArguments().getString("name");

    }

    public void onClick(View v) {
        int id = v.getId();

        //"我的资料"按钮
        if (id == R.id.head) {
            Intent intent = new Intent(getActivity(), MyInformation.class);
            intent.putExtra("name", name);
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
            /*Intent intent = new Intent(PersonalCenter.this, favourite.class);
            intent.putExtra("name",name);
            startActivity(intent);*/
        }

        //"退出登录"按钮
        if (id == R.id.log_out) {
            logOutDialog();
        }
    }

    //退出登录对话框
    public void logOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                //PersonalCenter.this.finish();
            }
        });
        builder.create().show();
    }
}
