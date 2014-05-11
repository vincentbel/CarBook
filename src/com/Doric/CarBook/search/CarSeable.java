package com.Doric.CarBook.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.Doric.CarBook.car.CarInfor;

class CarSeable {

    private ArrayList<CarSystem> carSystemList;    //车辆list
    private String carSeableName; //中文名
    private String carSeableId;        //id
    private String picPath;

    //初始化
    public CarSeable(String carSeableName, String id) {
        this.carSeableName = carSeableName;
        this.carSeableId = id;

    }

    CarSystem findCarSystem(String name){
        for(CarSystem cs : carSystemList){
            if(cs.getName().equals(name))
                return cs;
        }
        return null;
    }

    public void LoadCar() {
        carSystemList = new ArrayList<CarSystem>();

        if (carSeableName.equals("大众")) {

                CarSystem  ci = new CarSystem();
                ci.setCarSeableName(carSeableName);
                ci.setName("D");
                ci.gsetHighPrice(200.0);
                ci.setLowPrice(100.0);
                carSystemList.add(ci);

                ci = new CarSystem();
                ci.setName("F");
                ci.setCarSeableName(carSeableName);
                ci.gsetHighPrice(200.0);
                ci.setLowPrice(100.0);
                carSystemList.add(ci);

        } else if (carSeableName.equals("福特")) {

                CarSystem  ci = new CarSystem();
            ci.setCarSeableName(carSeableName);
                ci.setName("F");
                ci.gsetHighPrice(200.0);
                ci.setLowPrice(100.0);
                carSystemList.add(ci);

        } else if (carSeableName.equals("宝马")) {
            CarSystem  ci = new CarSystem();
            ci.setCarSeableName(carSeableName);
            ci.setName("B");
            ci.gsetHighPrice(200.0);
            ci.setLowPrice(100.0);
            carSystemList.add(ci);
        } else if (carSeableName.equals("三菱")) {
            CarSystem  ci = new CarSystem();
            ci.setCarSeableName(carSeableName);
            ci.setName("S");
            ci.gsetHighPrice(200.0);
            ci.setLowPrice(100.0);
            carSystemList.add(ci);
        }
        Collections.sort(carSystemList, new ComparatorCarSystem());

    }


    public ArrayList<CarSystem> getCarSystemList() {
        return carSystemList;
    }

    public void setCarSystemList(ArrayList<CarSystem> carSystemList) {
        this.carSystemList = carSystemList;
    }

    public String getCarSeableName() {
        return carSeableName;
    }

    public void setCarSeableName(String carSeableName) {
        this.carSeableName = carSeableName;
    }

    public String getCarSeableId() {
        return carSeableId;
    }

    public void setCarSeableId(String csId) {
        this.carSeableId = csId;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}









	

