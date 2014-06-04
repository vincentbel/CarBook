package com.Doric.CarBook.search;

import java.io.Serializable;

public class CarInfor implements Serializable {

    //成员变量
    //通过数字表示车辆大小类型,引擎类型


    private String carSerie;
    private String carSeable;
    private String carName;
    private String carGrade;
    private Double lowPrice;
    private Double higPrice;

    private String carPicPath;
    private String carId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass()!= o.getClass()) return false;

        CarInfor carInfor = (CarInfor) o;


        if (carGrade != null ? !carGrade.equals(carInfor.carGrade) : carInfor.carGrade != null) return false;
        if (carName != null ? !carName.equals(carInfor.carName) : carInfor.carName != null) return false;
        if (carPicPath != null ? !carPicPath.equals(carInfor.carPicPath) : carInfor.carPicPath != null) return false;
        if (carSeable != null ? !carSeable.equals(carInfor.carSeable) : carInfor.carSeable != null) return false;
        if (carSerie != null ? !carSerie.equals(carInfor.carSerie) : carInfor.carSerie != null) return false;

        if (higPrice != null ? !higPrice.equals(carInfor.higPrice) : carInfor.higPrice != null) return false;

        if (lowPrice != null ? !lowPrice.equals(carInfor.lowPrice) : carInfor.lowPrice != null) return false;


        return true;
    }


    @Override
    public int hashCode() {
        int result = carSerie != null ? carSerie.hashCode() : 0;
        result = 31 * result + (carSeable != null ? carSeable.hashCode() : 0);
        result = 31 * result + (carName != null ? carName.hashCode() : 0);

        result = 31 * result + (carGrade != null ? carGrade.hashCode() : 0);

        result = 31 * result + (lowPrice != null ? lowPrice.hashCode() : 0);
        result = 31 * result + (higPrice != null ? higPrice.hashCode() : 0);

        result = 31 * result + (carPicPath != null ? carPicPath.hashCode() : 0);
        return result;
    }

    //构造函数
    public CarInfor() {

    }

    public String getCarId(){    return carId; }

    public void setCarId(String id){carId = id;}

    public String getCarSerie() {
        return carSerie;
    }

    public void setCarSerie(String carSerie) {
        this.carSerie = carSerie;
    }

    public String getCarSeable() {
        return carSeable;
    }

    public void setCarSeable(String carSeable) {
        this.carSeable = carSeable;
    }

    public String getCarPicPath() {
        return carPicPath;
    }

    public void setCarPicPath(String carPicPath) {
        this.carPicPath = carPicPath;
    }


    //获取成员变量的值

    public String getCarGrade() {
        return carGrade;
    }

    public void setCarGrade(String carGrade) {
        this.carGrade = carGrade;
    }


    //初始化变量



    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }



    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Double getHigPrice() {
        return higPrice;
    }

    public void setHigPrice(Double higPrice) {
        this.higPrice = higPrice;
    }


}
