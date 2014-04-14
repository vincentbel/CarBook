package com.Doric.CarBook.car;

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

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarSize() {
        return carSize;
    }

    public void setCarSize(String carSize) {
        this.carSize = carSize;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public Double getHighSpeed() {
        return highSpeed;
    }
    //初始化变量

    public void setHighSpeed(Double highSpeed) {
        this.highSpeed = highSpeed;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public Double getTimeTo100Km() {
        return timeTo100Km;
    }

    public void setTimeTo100Km(Double timeTo100Km) {
        this.timeTo100Km = timeTo100Km;
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
