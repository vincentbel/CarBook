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


public class Login extends Activity implements OnClickListener {

    //������������ر���
    private String url = Static.BASE_URL + "/login.php";  //��¼�����url,��ؼ���http://��https://
    private List<NameValuePair> loginParams;    //��¼ʱ���͸�������������
    private JSONObject loginInfo;       //�����������õ���json����

    //����ؼ�
    private EditText edtUsername, edtPassword;
    private Button btnLogin, btnRegister;
    private ProgressDialog progressDialog;   //�첽����ʱ��ʾ�Ľ�����

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //���ÿؼ�
        edtUsername = (EditText) findViewById(R.id.username);
        edtPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login);
        btnRegister = (Button) findViewById(R.id.register);

        //��Ӽ�����
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //����Actionbar
        getActionBar().setTitle("��¼");
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onClick(View v) {

        int id = v.getId();
        //��ȡ�û�������
        String name = edtUsername.getText().toString();
        String psd = edtPassword.getText().toString();

        //��ע�ᡱ��ť
        if (id == R.id.register) {
            //�ӵ�½����ת��ע�����ʱ���û��������봫��ȥ�������û�����
            Intent intent = new Intent(Login.this, Register.class);
            intent.putExtra("username", name);
            intent.putExtra("password", psd);
            startActivity(intent);
        }

        //����½����ť
        if (id == R.id.login) {

            //�ж��û����Ƿ�Ϊ��
            if (name.equals("")) {
                Toast.makeText(Login.this, "�������û���", Toast.LENGTH_LONG).show();
            }
            //�ж������Ƿ�Ϊ��
            else if (psd.equals("")) {
                Toast.makeText(Login.this, "����������", Toast.LENGTH_LONG).show();
            } else {
                //�����û���Ϣ��������
                loginParams = new ArrayList<NameValuePair>();
                loginParams.add(new BasicNameValuePair("tag", "login"));
                loginParams.add(new BasicNameValuePair("username", name));
                loginParams.add(new BasicNameValuePair("password", psd));

                //�첽�����ж��û��Ƿ��¼�ɹ�
                new CheckUser().execute();
            }
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
            JSONParser jsonParser = new JSONParser();
            loginInfo = jsonParser.getJSONFromUrl(url, loginParams);
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

                        //��ת����������
                        Intent intent = new Intent(Login.this, PersonalCenter.class);
                        intent.putExtra("name",name);
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
