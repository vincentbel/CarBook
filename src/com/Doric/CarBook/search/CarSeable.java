package com.Doric.CarBook.search;

import java.util.ArrayList;
import java.util.Collections;


class CarSeable {

    private ArrayList<CarInfor> carList;    //车辆list
    private String carSeableName; //中文名
    private String carSeableId;        //id
    private int count;   ///总数量

    //初始化
    public CarSeable(String carSeableName, String id) {
        this.carSeableName = carSeableName;
        this.carSeableId = id;

    }


    public void LoadCar() {
        carList = new ArrayList<CarInfor>();
        if (carSeableName.equals("大众")) {
            for (int i = 1; i < 10; i++) {
                carList.add(new CarInfor(i, "M", "test", "test", 200.0, "D" + i, 20.0, 100.0, 200.0));
                count++;
            }
        } else if (carSeableName.equals("福特")) {
            for (int i = 1; i < 10; i++) {
                carList.add(new CarInfor(i, "S", "test", "test", 200.0, "F" + i, 20.0, 100.0, 200.0));
                count++;
            }
        } else if (carSeableName.equals("宝马")) {
            for (int i = 1; i < 10; i++) {
                carList.add(new CarInfor(i, "S", "test", "test", 200.0, "B" + i, 20.0, 100.0, 200.0));
                count++;
            }
        } else if (carSeableName.equals("三菱")) {
            for (int i = 1; i < 10; i++) {
                carList.add(new CarInfor(i, "L", "test", "test", 200.0, "S" + i, 20.0, 100.0, 200.0));
                count++;
            }
        }
        Collections.sort(carList, new ComparatorCarInfo());
    }


    public ArrayList<CarInfor> getCarList() {
        return carList;
    }

    public void setCarList(ArrayList<CarInfor> carList) {
        this.carList = carList;
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

    public void setCsId(String csId) {
        this.carSeableId = csId;
    }


}









	

