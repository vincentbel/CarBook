package com.Doric.CarBook.member;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.Doric.CarBook.R;
import com.Doric.CarBook.Static;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Register extends Activity implements View.OnClickListener {

    //������������ر���
    private String url = Static.BASE_URL + "/register.php";  //��¼�����url,��ؼ���http://��https://
    private List<NameValuePair> registerParams;    //��¼ʱ���͸�������������
    private JSONObject registerInfo;       //�����������õ���json����

    //����ؼ�
    private ProgressDialog progressDialog;   //�첽����ʱ��ʾ�Ľ�����
    private EditText edtUsername, edtPassword, edtEnsurePsd, edtEmail;
    private Button btnRegister;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //���ÿؼ�
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        edtEnsurePsd = (EditText) findViewById(R.id.ensure_password);
        edtEmail = (EditText) findViewById(R.id.email);
        btnRegister = (Button) findViewById(R.id.register);

        Intent intent = getIntent();
        edtUsername.setText(intent.getStringExtra("username"));
        edtPassword.setText(intent.getStringExtra("password"));

        //��Ӽ�����
        btnRegister.setOnClickListener(this);

        //����Actionbar
        getActionBar().setTitle("ע���˺�");
        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void onClick(View v) {
        int id = v.getId();

        //��ע�ᡱ��ť
        if (id == R.id.register) {
            //��ȡ�û��������Ϣ
            String name = edtUsername.getText().toString();
            String psd = edtPassword.getText().toString();
            String enPsd = edtEnsurePsd.getText().toString();
            String emailAddress = edtEmail.getText().toString();

            //�ж��û����Ƿ�Ϊ��
            if (name.equals("")) {
                Toast.makeText(Register.this, "�������û���", Toast.LENGTH_LONG).show();
            }
            //�ж��û����Ƿ����
            else if (name.length() > 16) {
                Toast.makeText(Register.this, "�û�������������������", Toast.LENGTH_LONG).show();
            }
            //�ж��û����Ƿ����
            else if (name.length() < 6) {
                Toast.makeText(Register.this, "�û������̣�����������", Toast.LENGTH_LONG).show();
            }
            //�ж������Ƿ�Ϊ��
            else if (psd.equals("")) {
                Toast.makeText(Register.this, "����������", Toast.LENGTH_LONG).show();
            }
            //�ж������Ƿ����
            else if (psd.length() > 16) {
                Toast.makeText(Register.this, "�������������������", Toast.LENGTH_LONG).show();
            }
            //�ж������Ƿ����
            else if (psd.length() < 6) {
                Toast.makeText(Register.this, "������̣�����������", Toast.LENGTH_LONG).show();
            }
            //�ж���֤�����Ƿ�Ϊ��
            else if (enPsd.equals("")) {
                Toast.makeText(Register.this, "���ٴ�������������", Toast.LENGTH_LONG).show();
            }
            //�ж���֤�����Ƿ���ȷ
            else if (!psd.equals(enPsd)) {
                Toast.makeText(Register.this, "������������벻ͬ������������", Toast.LENGTH_LONG).show();
            }
            //�ж������Ƿ�Ϊ��
            else if (emailAddress.equals("")) {
                Toast.makeText(Register.this, "���������������ַ", Toast.LENGTH_LONG).show();
            }
            //�ж������ʽ�Ƿ�Ϲ淶
            else if (!check_email(emailAddress)) {
                Toast.makeText(Register.this, "���������ַ����ȷ������������", Toast.LENGTH_LONG).show();
            }

            //�����û���Ϣ��������
            else {

                //�����û���Ϣ��������
                registerParams = new ArrayList<NameValuePair>();
                registerParams.add(new BasicNameValuePair("tag", "register"));
                registerParams.add(new BasicNameValuePair("username", name));
                registerParams.add(new BasicNameValuePair("password", psd));
                registerParams.add(new BasicNameValuePair("email", emailAddress));

                //�첽�����ж��û��Ƿ��¼�ɹ�
                new registerUser().execute();
            }
        }
    }

    //��֤�����ʽ
    public boolean check_email(String ead) {
        int len = ead.length();
        int i;
        for (i = 0; i < len; i++) {
            if (ead.charAt(i) == '@') {
                return true;
            }
        }
        return false;
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
            //����"����ע��"��
            progressDialog = new ProgressDialog(Register.this);
            progressDialog.setMessage("����ע��..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //���������������
            JSONParser jsonParser = new JSONParser();
            registerInfo = jsonParser.getJSONFromUrl(url, registerParams);
            return null;
        }
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //�ж��յ���json�Ƿ�Ϊ��
            if (registerInfo != null) {
                try {
                    //ע��ɹ�
                    if (registerInfo.getString("error").equals("0")) {
                        //��ת����������
                        Intent intent = new Intent(Register.this, PersonalCenter.class);
                        startActivity(intent);
                    }
                    //��������
                    else if (registerInfo.getString("error").equals("1")) {
                        Toast.makeText(Register.this, "ע��ʧ��", Toast.LENGTH_LONG).show();
                    }
                    //�û����Ѵ���
                    else if ( registerInfo.getString("error").equals("2") ) {
                        Toast.makeText(Register.this, "�û����Ѵ��ڣ�����������", Toast.LENGTH_LONG).show();
                    }
                    //�����ѱ�ʹ��
                    else {
                        Toast.makeText(Register.this, "�����Ѵ��ڣ�����������", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(Register.this, "ע��ʧ�ܣ��������������Ƿ�����", Toast.LENGTH_LONG).show();
            }
        }
    }
}
