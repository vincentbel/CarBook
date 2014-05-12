package com.Doric.CarBook.search;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.Doric.CarBook.Static;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2014/5/4.
 */
public class CarSeries {


    private String carSeableName;
    private String name;
    private Double highPrice;
    private Double lowPrice;
    private String picPath;
    private ArrayList<CarInfor> carList;


    private MyFragment context;

    private JSONObject carObj;

    private List<NameValuePair> carParams = new ArrayList<NameValuePair>();

    private String url = Static.BASE_URL + "/search.php";

    private boolean isload = false;

    public CarSeries() {
        carList = new ArrayList<CarInfor>();
    }

    //获取该品牌，车系下的车辆信息
    public boolean loadCar(MyFragment f) {
        context = f;
        if (!isload) {
            carParams.add(new BasicNameValuePair("tag", "model_number"));
            carParams.add(new BasicNameValuePair("brand", carSeableName));
            carParams.add(new BasicNameValuePair("brand_series", name));

            new GetCarInfor().execute();
        } else
            context.initPage();
        return true;
    }

    public ArrayList<CarInfor> getCarList() {
        return carList;
    }

    public void setCarList(ArrayList<CarInfor> carList) {
        this.carList = carList;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public String getCarSeableName() {
        return carSeableName;
    }

    public void setCarSeableName(String carSeableName) {
        this.carSeableName = carSeableName;
    }

    private class GetCarInfor extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //弹出"正在登录"框
            context.loading();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            carObj = jsonParser.getJSONFromUrl(url, carParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            context.stopLoading();
            if (carObj != null) {
                try {
                    int success = carObj.getInt("success");
                    if (success == 1) {
                        int num = carObj.getInt("model_number_amount");
                        for (int i = 1; i <= num; i++) {
                            CarInfor cs = new CarInfor();
                            cs.setCarName(carObj.getString("model_number_" + i));
                            cs.setCarSerie(name);
                            cs.setCarSeable(carSeableName);
                            carList.add(cs);
                        }
                        isload = true;
                        if (carList.size() > 0)
                            Collections.sort(carList, new ComparatorCarInfo());
                        context.initPage();
                    }
                } catch (JSONException e) {
                    Context c = context.getActivity().getApplicationContext();
                    if (c == null) return;
                    Toast.makeText(c, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                Context c = context.getActivity().getApplicationContext();
                if (c == null) return;
                Toast.makeText(c, "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
            }
        }
    }
}

