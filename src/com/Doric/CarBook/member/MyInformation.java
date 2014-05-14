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

    //������������ر���
    private String url = Constant.BASE_URL + "/sex.php";  //��¼�����url,��ؼ���http://��https://
    private List<NameValuePair> sexParams;    //��¼ʱ���͸�������������
    private JSONObject sexInfo;       //�����������õ���json����

    //����ؼ�
    private ProgressDialog progressDialog;   //�첽����ʱ��ʾ�Ľ�����
    private RelativeLayout loHead, loSex, loUsername;
    private Button btnLogOut, btnBack;
    private TextView tvUsername;

    //�������
    private String name = "����", sex = "��";
    private String[] sexes = new String[]{"��", "Ů"};

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_information);

        //���ÿؼ�
        loHead = (RelativeLayout) findViewById(R.id.head_layout);
        loSex = (RelativeLayout) findViewById(R.id.sex_layout);
        loUsername = (RelativeLayout) findViewById(R.id.username_layout);
        btnLogOut = (Button) findViewById(R.id.log_out);
        btnBack = (Button) findViewById(R.id.back);
        tvUsername = (TextView) findViewById(R.id.username_text2);

        //��Ӽ�����
        loHead.setOnClickListener(this);
        loSex.setOnClickListener(this);
        loUsername.setOnClickListener(this);
        btnLogOut.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //����Actionbar
        getActionBar().hide();

        //ȡ��������Activity��Intent����
        Intent intent = getIntent();

        //ȡ��Intent�и��ӵ�����
        if (intent.getStringExtra("name") != null) {
            name = intent.getStringExtra("name");
            tvUsername.setText(name);
        }
    }

    public void onClick(View v) {
        int id = v.getId();

        //"ͷ��"��ť
        if (id == R.id.head_layout) {

        }

        //"�Ա�"��ť
        if (id == R.id.sex_layout) {
            new AlertDialog.Builder(MyInformation.this) // build AlertDialog
                    .setTitle("ѡ���Ա�") // title
                    .setItems(sexes, new DialogInterface.OnClickListener() { //content
                        public void onClick(DialogInterface dialog, int which) {
                            //�ж��Ƿ��޸����Ա�
                            if (!sexes[which].equals(sex)) {
                                //�����û���Ϣ��������
                                sexParams = new ArrayList<NameValuePair>();
                                sexParams.add(new BasicNameValuePair("tag", "sex"));
                                sexParams.add(new BasicNameValuePair("username", name));
                                sexParams.add(new BasicNameValuePair("sex", sex));

                                //�첽����
                                new registerUser().execute();
                            }
                        }
                    })
                    .show();
        }

        //"�˳���¼"��ť
        if (id == R.id.log_out) {
            logOutDialog();
        }

        //"����"��ť
        if (id == R.id.back) {
            MyInformation.this.finish();
        }
    }

    //�˳���¼�Ի���
    public void logOutDialog() {
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
            //����"�����޸�"��
            progressDialog = new ProgressDialog(MyInformation.this);
            progressDialog.setMessage("�����޸�..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //���������������
            JSONParser jsonParser = new JSONParser();
            sexInfo = jsonParser.getJSONFromUrl(url, sexParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //�ж��յ���json�Ƿ�Ϊ��
            if (sexInfo != null) {
                try {
                    if (sexInfo.getString("error").equals("0")) {
                        if (sex.equals("��")) {
                            sex = "Ů";
                        } else {
                            sex = "��";
                        }
                        TextView tvSex = (TextView) findViewById(R.id.sex_text2);
                        tvSex.setText(sex);
                        Toast.makeText(MyInformation.this, "�Ա��޸ĳɹ�", Toast.LENGTH_LONG).show();
                    }
                    //��������
                    else {
                        Toast.makeText(MyInformation.this, "�޸�ʧ��", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(MyInformation.this, "�޸�ʧ�ܣ��������������Ƿ�����", Toast.LENGTH_LONG).show();
            }
        }
    }

}
