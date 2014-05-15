package com.Doric.CarBook.search;


import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//����Ʒ�����ݾ�̬�ࡣ�Ա�֤���κ�Act...�ж����Ի�ó�����Ϣ
public class CarSeableData {
    //Ʒ��list
    public static ArrayList<CarSeable> mCarSeable;

    public static FragmentTransaction fragmentTransaction;
    public static JSONObject brandObj;
    public static List<NameValuePair> brandParams = new ArrayList<NameValuePair>();
    public static String url = Constant.BASE_URL + "/search.php";
    public static boolean isload = false;



    static {
        mCarSeable = new ArrayList<CarSeable>();
    }
    //�ӷ������˻�ȡ����
    public static void getData(FragmentTransaction ft) {
        fragmentTransaction = ft;

        if (!isload) {
            brandParams.add(new BasicNameValuePair("tag", "brand"));

            new GetBrand().execute();
        } else

            ft.commit();

    }
    //ֱ�Ӵ��ڴ濽��
    public static void setData(ArrayList<CarSeable> al){
        mCarSeable.clear();
        mCarSeable.addAll(al);
    }
    public static CarSeable find(String cName) {
        for (CarSeable cs : mCarSeable) {
            if (cs.getCarSeableName().equals(cName))
                return cs;
        }
        return null;
    }

    private static class GetBrand extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //����"���ڵ�¼"��
            SearchMain.searchmain.loading();
        }

        protected Void doInBackground(Void... params) {
            //���������������
            JSONParser jsonParser = new JSONParser();
            brandObj = jsonParser.getJSONFromUrl(url, brandParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SearchMain.searchmain.stopLoading();

            if (brandObj != null) {
                try {
                    int success = brandObj.getInt("success");
                    if (success == 1) {
                        int num = brandObj.getInt("brand_number");
                        for (int i = 1; i <= num; i++) {
                            CarSeable cs = new CarSeable();
                            cs.setCarSeableName(brandObj.getString("brand_" + i));
                            cs.setPicPath(Constant.BASE_URL + "/" + brandObj.getString("brand_" + i + "_url"));

                            mCarSeable.add(cs);
                        }
                        isload = true;
                        if (mCarSeable.size() > 0)
                            Collections.sort(mCarSeable, new ComparatorCarSeable());

                        fragmentTransaction.commit();
                        //context.initPage();
                    }
                } catch (JSONException e) {

                    Toast.makeText(SearchMain.searchmain, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(SearchMain.searchmain, "�޷��������磬���������ֻ���������", Toast.LENGTH_LONG).show();

            }
        }
    }


}

