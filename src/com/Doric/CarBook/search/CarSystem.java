package com.Doric.CarBook.search;

import com.Doric.CarBook.car.CarInfor;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/5/4.
 */
public class CarSystem {


    private String carSeableName;
    private String name;
    private Double highPrice;
    private Double lowPrice;
    private String picPath;
    private ArrayList<CarInfor> carList;
    public boolean loadCar(){
        return true;
    }
    public ArrayList<CarInfor> getCarList() {
        return carList;
    }

    public void setCarList(ArrayList<CarInfor> carList) {
        this.carList = carList;
    }


    public CarSystem(){
            }

    public Double getHighPrice() {
        return highPrice;
    }

    public String getName() {
        return name;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void gsetHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
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

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }
}
