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
import com.Doric.CarBook.MainActivity;
import com.Doric.CarBook.R;
import org.json.JSONException;
import org.json.JSONObject;


public class Register extends Activity implements View.OnClickListener {

    private JSONObject registerInfo;       //�����������õ���json����
    private UserFunctions userFunctions;

    //����ؼ�
    private ProgressDialog progressDialog;   //�첽����ʱ��ʾ�Ľ�����
    private EditText edtUsername, edtPassword, edtEnsurePsd, edtEmail;
    private Button btnRegister, btnBack;
    private String username;
    private String password;
    private String emailAddress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        userFunctions = new UserFunctions(getApplicationContext());

        //���ÿؼ�
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        edtEnsurePsd = (EditText) findViewById(R.id.ensure_password);
        edtEmail = (EditText) findViewById(R.id.email);
        btnRegister = (Button) findViewById(R.id.register);
        btnBack = (Button) findViewById(R.id.back);

        //��Ӽ�����
        btnRegister.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //����Actionbar
        getActionBar().hide();

        Intent intent = getIntent();
        edtUsername.setText(intent.getStringExtra("username"));
        edtPassword.setText(intent.getStringExtra("password"));
    }

    public void onClick(View v) {
        int id = v.getId();

        //��ע�ᡱ��ť
        if (id == R.id.register) {
            //��ȡ�û��������Ϣ
            username = edtUsername.getText().toString();
            password = edtPassword.getText().toString();
            String enPassword = edtEnsurePsd.getText().toString();
            emailAddress = edtEmail.getText().toString();

            //�ж��û����Ƿ�Ϊ��
            if (username.equals("")) {
                Toast.makeText(Register.this, "�������û���", Toast.LENGTH_LONG).show();
            }
            //�ж��û����Ƿ����
            else if (username.length() > 16) {
                Toast.makeText(Register.this, "�û�������������������", Toast.LENGTH_LONG).show();
            }
            //�ж��û����Ƿ����
            else if (username.length() < 6) {
                Toast.makeText(Register.this, "�û������̣�����������", Toast.LENGTH_LONG).show();
            }
            //�ж������Ƿ�Ϊ��
            else if (password.equals("")) {
                Toast.makeText(Register.this, "����������", Toast.LENGTH_LONG).show();
            }
            //�ж������Ƿ����
            else if (password.length() > 16) {
                Toast.makeText(Register.this, "�������������������", Toast.LENGTH_LONG).show();
            }
            //�ж������Ƿ����
            else if (password.length() < 6) {
                Toast.makeText(Register.this, "������̣�����������", Toast.LENGTH_LONG).show();
            }
            //�ж���֤�����Ƿ�Ϊ��
            else if (enPassword.equals("")) {
                Toast.makeText(Register.this, "���ٴ�������������", Toast.LENGTH_LONG).show();
            }
            //�ж���֤�����Ƿ���ȷ
            else if (!password.equals(enPassword)) {
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
                //�첽�����ж��û��Ƿ��¼�ɹ�
                new registerUser().execute();
            }
        }

        //"����"��ť
        if (id == R.id.back) {
            Register.this.finish();
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
            registerInfo = userFunctions.registerUser(username, emailAddress, password);
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
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                    }
                    //��������
                    else if (registerInfo.getString("error").equals("1")) {
                        Toast.makeText(Register.this, "ע��ʧ��", Toast.LENGTH_LONG).show();
                    }
                    //�û����Ѵ���
                    else if (registerInfo.getString("error").equals("2")) {
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
