package com.Doric.CarBook.search;

import android.app.FragmentTransaction;

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

/**
 * Created by Administrator on 2014/5/4.
 */

/**
 * 车系
 */
public class CarSeries {


    private String carSeableName;
    private String name;
    private Double highPrice;
    private Double lowPrice;
    private String picPath;
    private ArrayList<CarInfor> carList;


    private  FragmentTransaction fragmentTransaction;

    private JSONObject carObj;

    private List<NameValuePair> carParams = new ArrayList<NameValuePair>();

    private String url = Constant.BASE_URL + "/search.php";

    private boolean isload = false;

    public CarSeries() {
        carList = new ArrayList<CarInfor>();
    }

    //获取该品牌，车系下的车辆信息
    public boolean loadCar(FragmentTransaction ft) {
        fragmentTransaction = ft;
        if (!isload) {
            carParams.add(new BasicNameValuePair("tag", "model_number"));
            carParams.add(new BasicNameValuePair("brand", GBK2UTF.Transform(carSeableName)));
            carParams.add(new BasicNameValuePair("brand_series", GBK2UTF.Transform(name)));
            carObj= DataCache.InputToMemory(carParams);
            if(carObj!=null){
                try{
                    DecodeJSON();
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                SearchMain.searchmain.stopLoading();
                fragmentTransaction.commit();
            }
            else {
                new GetCarInfor().execute();
            }
        } else {
            CarListShow.setCarList(carList);
            fragmentTransaction.commit();
        }
        return true;
    }
    public CarInfor findCarInfo(String name) {
        for (CarInfor cs : carList) {
            if (cs.getCarName().equals(name))
                return cs;
        }
        return null;
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
            SearchMain.searchmain.loading();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            carObj = jsonParser.getJSONFromUrl(url, carParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SearchMain.searchmain.stopLoading();
            if (carObj != null) {
                try {
                    int success = carObj.getInt("success");
                    if (success == 1) {
                        DataCache.OutputToCacheFile(carParams,carObj);
                        DecodeJSON();
                        SearchMain.searchmain.stopLoading();
                        fragmentTransaction.commit();
                    }
                } catch (JSONException e) {
                    SearchMain.searchmain.stopLoading();
                    Toast.makeText(SearchMain.searchmain, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                SearchMain.searchmain.stopLoading();
                Toast.makeText(SearchMain.searchmain, "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
            }
        }

    }
    private void DecodeJSON()throws JSONException{
        int num = carObj.getInt("model_number_amount");
        for (int i = 1; i <= num; i++) {
            CarInfor cs = new CarInfor();
            cs.setCarName(carObj.getString("model_number_" + i));
            cs.setCarSerie(name);
            cs.setCarSeable(carSeableName);
            cs.setCarId(carObj.getString("model_number_" + i+"_car_id"));
            System.out.println(cs.getCarId());
            //用车系的第一张图片作为车辆的图片
            carList.add(cs);
        }
        isload = true;
        if (carList.size() > 0)
            Collections.sort(carList, new ComparatorCarInfo());
        CarListShow.setCarList(carList);
    }
}

