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
import com.Doric.CarBook.R;
import com.Doric.CarBook.car.HotCarShow;

public class PersonalCenter extends Fragment implements View.OnClickListener {

    UserFunctions userFunctions;
    //����ؼ�
    private Button btnInformation, btnComment, btnFavourite, btnLogOut, btnBack;
    private TextView tvUsername;
    //�������
    private String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.personal_center, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mView = getView();

        userFunctions = new UserFunctions(getActivity().getApplicationContext());

        //���ÿؼ�
        btnFavourite = (Button) mView.findViewById(R.id.name);
        btnInformation = (Button) mView.findViewById(R.id.head);
        btnComment = (Button) mView.findViewById(R.id.sex);
        btnLogOut = (Button) mView.findViewById(R.id.log_out);
        btnBack = (Button) mView.findViewById(R.id.back);
        tvUsername = (TextView) mView.findViewById(R.id.bar_username);

        //��Ӽ�����
        btnComment.setOnClickListener(this);
        btnFavourite.setOnClickListener(this);
        btnInformation.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        //btnBack.setOnClickListener(this);

        //����Actionbar
        //getActivity().getActionBar().hide();

        //ȡ��������Activity��Intent����
        //Intent intent =getIntent();

        //ȡ��Intent�и��ӵ�����
        name = getArguments().getString("name");
        tvUsername.setText(name);
    }

    public void onClick(View v) {
        int id = v.getId();

        //"�ҵ�����"��ť
        if (id == R.id.head) {
            Intent intent = new Intent(getActivity(), MyInformation.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }

        //"�ҵ�����"��ť
        if (id == R.id.sex) {
            /*Intent intent = new Intent(PersonalCenter.this, comment.class);
            intent.putExtra("name",name);
             startActivity(intent);*/
        }

        //"�ҵ��ղ�"��ť
        if (id == R.id.name) {
            Intent intent = new Intent(getActivity(), UserCollection.class);
            intent.putExtra("name", name);
            startActivity(intent);
        }

        //"�˳���¼"��ť
        if (id == R.id.log_out) {
            logOutDialog();
        }

        /*//"����"��ť
        if (id == R.id.back) {
            PersonalCenter.this.finish();
        }*/
    }

    //�˳���¼�Ի���
    public void logOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("ȷ��Ҫ�˳���¼��");
        builder.setTitle("�˳���¼");
        builder.setPositiveButton("ȡ��", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                userFunctions.logoutUser();
                dialog.dismiss();
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new HotCarShow();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            }
        });
        builder.create().show();
    }
}
