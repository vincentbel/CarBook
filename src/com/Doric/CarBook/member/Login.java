package com.Doric.CarBook.member;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.Doric.CarBook.MainActivity;
import com.Doric.CarBook.R;
import org.json.JSONException;
import org.json.JSONObject;


public class Login extends Activity implements OnClickListener {

    UserFunctions userFunctions;        //�û�������
    String username;    //�û���
    String password;    //����
    private JSONObject loginInfo;       //�����������õ���json����
    //����ؼ�
    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister, btnBack;
    private ProgressDialog progressDialog;   //�첽����ʱ��ʾ�Ľ�����
    private TextView tvFindPsd;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userFunctions = new UserFunctions(getApplicationContext());

        //���ÿؼ�
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login);
        btnRegister = (Button) findViewById(R.id.register);
        btnBack = (Button) findViewById(R.id.back);
        tvFindPsd = (TextView) findViewById(R.id.forget_psd);

        //��Ӽ�����
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        tvFindPsd.setOnClickListener(this);

        //����Actionbar
        getActionBar().hide();
    }

    public void onClick(View v) {

        int id = v.getId();

        //��ȡ�û�������
        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();

        //��ע�ᡱ��ť
        if (id == R.id.register) {
            // �ӵ�½����ת��ע�����ʱ���û��������봫��ȥ�������û�����
            Intent intent = new Intent(Login.this, Register.class);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);
        }

        //����½����ť
        if (id == R.id.login) {

            //�ж��û����Ƿ�Ϊ��
            if (username.equals("")) {
                Toast.makeText(Login.this, "�������û���", Toast.LENGTH_LONG).show();
            }
            //�ж������Ƿ�Ϊ��
            else if (password.equals("")) {
                Toast.makeText(Login.this, "����������", Toast.LENGTH_LONG).show();
            } else {
                //�첽�����ж��û��Ƿ��¼�ɹ�
                new CheckUser().execute();
            }
        }

        //"����"��ť
        if (id == R.id.back) {
            Login.this.finish();
        }

        //"�������룿"��ť
        if (id == R.id.forget_psd) {
            Intent intent = new Intent(Login.this, FindPsd.class);
            startActivity(intent);
        }
    }

    private class CheckUser extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //����"���ڵ�¼"��
            progressDialog = new ProgressDialog(Login.this);
            progressDialog.setMessage("���ڵ�¼..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {

            //���������������
            loginInfo = userFunctions.loginUser(username, password);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            //�ж��յ���json�Ƿ�Ϊ��
            if (loginInfo != null) {
                try {
                    //�˻���Ϣ��֤ʧ��
                    if (loginInfo.getString("success").equals("0")) {
                        Toast.makeText(Login.this, "�û����������������������", Toast.LENGTH_LONG).show();
                    }
                    //�˻���Ϣ��֤�ɹ�
                    else {
                        Toast.makeText(Login.this, "��¼�ɹ���", Toast.LENGTH_LONG).show();

                        //��ȡ�û���
                        String name = edtUsername.getText().toString();

                        //��ת��������
                        Login.this.finish();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(Login.this, "��֤ʧ�ܣ��������������Ƿ�����", Toast.LENGTH_LONG).show();
            }
        }
    }
}
