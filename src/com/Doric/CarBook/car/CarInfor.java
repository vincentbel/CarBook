package com.Doric.CarBook.car;

public class CarInfor {

    //��Ա����
    //ͨ�����ֱ�ʾ������С����,��������
    private int carId;
    private String carName;
    private String engineType;
    private String brandId;
    private String carGrade;
    private Double highSpeed;
    private Double timeTo100Km;
    private Double lowPrice;
    private Double higPrice;
    private String transmission;
    private String price;
    private String carBodyStructure;


    //���캯��
    public CarInfor() {

    }


    //��ȡ��Ա������ֵ
    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getCarGrade() {
        return carGrade;
    }

    public void setCarGrade(String carGrade) {
        this.carGrade = carGrade;
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
    //��ʼ������

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

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCarBodyStructure() {
        return carBodyStructure;
    }

    public void setCarBodyStructure(String carBodyStructure) {
        this.carBodyStructure = carBodyStructure;
    }
}
