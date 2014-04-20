package com.Doric.CarBook.member;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.Doric.CarBook.R;

public class MyInformation extends Activity implements View.OnClickListener {

    //����ؼ�
    private Button btnHead,btnName, btnSex,btnLogOut;

    //�������
    private String name = "����",sex = null;
    private String[] sexes = new String[]{"��","Ů"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);

        //���ÿؼ�
        btnHead = (Button) findViewById(R.id.head);
        btnSex = (Button) findViewById(R.id.sex);
        btnLogOut = (Button) findViewById(R.id.log_out);

        //��Ӽ�����
        btnHead.setOnClickListener(this);
        btnSex.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);

        //����Actionbar
        getActionBar().setTitle("�ҵ�����");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //ȡ��������Activity��Intent����
        Intent intent =getIntent();

        //ȡ��Intent�и��ӵ�����
        name = intent.getStringExtra("name");
    }

    public void onClick(View v) {
        int id = v.getId();

        //"ͷ��"��ť
        if (id == R.id.head) {

        }

        //"�Ա�"��ť
        if (id == R.id.sex) {
            new AlertDialog.Builder(MyInformation.this) // build AlertDialog
                    .setTitle("ѡ���Ա�") // title
                    .setItems(sexes, new DialogInterface.OnClickListener() { //content
                        public void onClick(DialogInterface dialog, int which) {
                            sex = sexes[which];
                        }
                    })
                    .show();
        }
        //"�˳���¼"��ť
        if (id == R.id.log_out) {
            logOutDialog();
        }
    }

    //�˳���¼�Ի���
    public void logOutDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MyInformation.this);
        builder.setMessage("ȷ��Ҫ�˳���¼��");
        builder.setTitle("�˳���¼");
        builder.setPositiveButton("ȡ��", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                MyInformation.this.finish();
            }
        });
        builder.create().show();
    }
}

