package com.Doric.CarBook.member;


import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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

public class PersonalCenter extends Fragment implements View.OnClickListener {

    UserFunctions userFunctions;
    private String name;
    private String whichHead = "0";

    //定义控件
    private Button btnInformation, btnComment, btnLogOut;
    private TextView textView;

    //服务器请求相关变量
    private String headURL = Constant.BASE_URL + "/user_setting.php";  //登录请求的url,务必加上http://或https://
    private List<NameValuePair> headParams;    //登录时发送给服务器的数据
    private JSONObject headInfo;       //向服务器请求得到的json对象

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

        //发送用户信息到服务器
        headParams = new ArrayList<NameValuePair>();
        headParams.add(new BasicNameValuePair("tag", "get_avatar"));
        headParams.add(new BasicNameValuePair("username", name));

        //异步任务
        new getHead().execute();
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
            Intent intent = new Intent(getActivity(), MyComments.class);
            startActivity(intent);
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

    //获取头像
    private class getHead extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            headInfo = jsonParser.getJSONFromUrl(headURL, headParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //判断收到的json是否为空
            if (headInfo != null) {
                try {
                    if (headInfo.getString("success").equals("1")) {
                        whichHead = headInfo.getString("status");
                        setHead(whichHead);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //设置头像
    public void setHead(String which) {
        ImageView imageHead = (ImageView)getView().findViewById(R.id.pc_head);
        switch ( Integer.parseInt(which) ){
            case 1: imageHead.setBackgroundResource(R.drawable.head1); break;
            case 2: imageHead.setBackgroundResource(R.drawable.head2); break;
            case 3: imageHead.setBackgroundResource(R.drawable.head3); break;
            case 4: imageHead.setBackgroundResource(R.drawable.head4); break;
            case 5: imageHead.setBackgroundResource(R.drawable.head5); break;
            case 6: imageHead.setBackgroundResource(R.drawable.head6); break;
            case 7: imageHead.setBackgroundResource(R.drawable.head7); break;
            case 8: imageHead.setBackgroundResource(R.drawable.head8); break;
            case 9: imageHead.setBackgroundResource(R.drawable.head9); break;
            default:imageHead.setBackgroundResource(R.drawable.pc_default_head); break;
        }
    }

}
