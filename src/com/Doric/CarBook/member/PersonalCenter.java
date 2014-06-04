package com.Doric.CarBook.member;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.Doric.CarBook.MainActivity;
import com.Doric.CarBook.R;
import com.Doric.CarBook.car.HotCarShow;

public class PersonalCenter extends Fragment implements View.OnClickListener {

    UserFunctions userFunctions;
    private String name;
    //定义控件
    private Button btnInformation, btnComment, btnLogOut;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.personal_center, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mView = getView();

        userFunctions = new UserFunctions(getActivity().getApplicationContext());
        name = userFunctions.getUsername();

        //设置控件
        btnInformation = (Button) mView.findViewById(R.id.button_my_information);
        btnComment = (Button) mView.findViewById(R.id.button_my_comments);
        btnLogOut = (Button) mView.findViewById(R.id.button_log_out);
        textView = (TextView) mView.findViewById(R.id.bar_username);

        textView.setText(name);

        //添加监听器
        btnComment.setOnClickListener(this);
        btnInformation.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        //隐藏Actionbar
        //getActivity().getActionBar().hide();
    }

    public void onClick(View v) {
        int id = v.getId();

        //"我的资料"按钮
        if (id == R.id.button_my_information) {
            Intent intent = new Intent(getActivity(), MyInformation.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }

        //"我的评论"按钮
        if (id == R.id.button_my_comments) {
            /*Intent intent = new Intent(PersonalCenter.this, comment.class);
            intent.putExtra("name",name);
             startActivity(intent);*/
        }

        //"退出登录"按钮
        if (id == R.id.button_log_out) {
            logOutDialog();
        }
    }

    //退出登录对话框
    public void logOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                /*FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new HotCarShow();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();*/
                getActivity().finish();
                startActivity(getActivity().getIntent());
            }
        });
        builder.create().show();
    }
}
