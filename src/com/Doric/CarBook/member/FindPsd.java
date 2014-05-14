package com.Doric.CarBook.member;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class FindPsd extends Activity implements View.OnClickListener {

    //������������ر���
    private String url = Constant.BASE_URL + "/findPsd.php";  //��¼�����url,��ؼ���http://��https://
    private List<NameValuePair> findPsdParams;    //��¼ʱ���͸�������������
    private JSONObject findPsdInfo;       //�����������õ���json����

    //����ؼ�
    private Button btnSubmit, btnBack;
    private ProgressDialog progressDialog;   //�첽����ʱ��ʾ�Ľ�����
    private EditText edtUsername, edtEmailAddress;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_psd);

        //���ÿؼ�
        btnSubmit = (Button) findViewById(R.id.submit);
        btnBack = (Button) findViewById(R.id.back);
        edtUsername = (EditText) findViewById(R.id.username);
        edtEmailAddress = (EditText) findViewById(R.id.email);

        //��Ӽ�����
        btnSubmit.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //����Actionbar
        getActionBar().hide();
    }

    public void onClick(View v) {
        int id = v.getId();

        //"����"��ť
        if (id == R.id.back) {
            FindPsd.this.finish();
        }

        //"�ύ"��ť
        if (id == R.id.submit) {

            //��ȡ�û�������
            String name = edtUsername.getText().toString();
            String email = edtEmailAddress.getText().toString();

            //�ж��û����Ƿ�Ϊ��
            if (name.equals("")) {
                Toast.makeText(FindPsd.this, "�������û���", Toast.LENGTH_LONG).show();
            }
            //�ж������Ƿ�Ϊ��
            else if (email.equals("")) {
                Toast.makeText(FindPsd.this, "����������", Toast.LENGTH_LONG).show();
            } else if (check_email(email)) {
                //�����û���Ϣ��������
                findPsdParams = new ArrayList<NameValuePair>();
                findPsdParams.add(new BasicNameValuePair("tag", "findPsd"));
                findPsdParams.add(new BasicNameValuePair("username", name));
                findPsdParams.add(new BasicNameValuePair("email", email));

                //�첽�����ж��û��Ƿ��¼�ɹ�
                new CheckUser().execute();
            } else {
                Toast.makeText(FindPsd.this, "���������ַ����ȷ������������", Toast.LENGTH_LONG).show();
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

    private class CheckUser extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //����"������֤"��
            progressDialog = new ProgressDialog(FindPsd.this);
            progressDialog.setMessage("������֤..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //���������������
            JSONParser jsonParser = new JSONParser();
            findPsdInfo = jsonParser.getJSONFromUrl(url, findPsdParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //�ж��յ���json�Ƿ�Ϊ��
            if (findPsdInfo != null) {
                try {
                    //��֤ʧ��
                    if (findPsdInfo.getString("success").equals("0")) {
                        Toast.makeText(FindPsd.this, "��֤ʧ�ܣ�����������", Toast.LENGTH_LONG).show();
                    }
                    //�˻���Ϣ��֤�ɹ�
                    else {
                        Toast.makeText(FindPsd.this, "��֤�ɹ���", Toast.LENGTH_LONG).show();
                        FindPsd.this.finish();
                        Intent intent = new Intent(FindPsd.this, FindPsd.class);
                        startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(FindPsd.this, "��֤ʧ�ܣ��������������Ƿ�����", Toast.LENGTH_LONG).show();
            }
        }
    }
}
