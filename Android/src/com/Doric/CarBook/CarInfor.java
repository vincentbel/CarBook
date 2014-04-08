package com.Doric.CarBook;

/**
 * Created by Sunyao_Will on 14-3-18.
 */
/*
    
 */
public class CarInfor {

    //成员变量
    //通过数字表示车辆大小类型,引擎类型
    private int carId;
    private String carName;
    private String engineType;
    private String brandId;
    private String carSize;
    private Double highSpeed;
    private Double timeTo100Km;
    private Double lowPrice;
    private Double higPrice;

    //构造函数
    public CarInfor() {

    }

    public CarInfor(int carId, String carSize, String brandId,
                    String engineType, Double highSpeed, String carName,
                    Double timeTo100Km, Double lowPrice, Double higPrice) {
        this.carId = carId;
        this.carSize = carSize;
        this.brandId = brandId;
        this.engineType = engineType;
        this.highSpeed = highSpeed;
        this.carName = carName;
        this.timeTo100Km = timeTo100Km;
        this.lowPrice = lowPrice;
        this.higPrice = higPrice;
    }

    //获取成员变量的值
    public int getCarId() {
        return carId;
    }

    public String getCarSize() {
        return carSize;
    }

    public String getBrandId() {
        return brandId;
    }

    public String getEngineType() {
        return engineType;
    }

    public Double getHighSpeed() {
        return highSpeed;
    }

    public String getCarName() {
        return carName;
    }

    public Double getTimeTo100Km() {
        return timeTo100Km;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public Double getHigPrice() {
        return higPrice;
    }
    //初始化变量


    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setCarSize(String carSize) {
        this.carSize = carSize;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public void setHighSpeed(Double highSpeed) {
        this.highSpeed = highSpeed;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public void setTimeTo100Km(Double timeTo100Km) {
        this.timeTo100Km = timeTo100Km;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public void setHigPrice(Double higPrice) {
        this.higPrice = higPrice;
    }
}