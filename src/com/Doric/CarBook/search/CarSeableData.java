package com.Doric.CarBook.search;

import com.Doric.CarBook.Static;

import java.util.ArrayList;
import java.util.Collections;

public class CarSeableData {
    public static ArrayList<CarSeable> mCarSeable;

    public static void getData() {
        mCarSeable = new ArrayList<CarSeable>();


        Collections.sort(mCarSeable, new ComparatorCarSeable());
    }

    public static CarSeable find(String cName) {
        for (CarSeable cs : mCarSeable) {
            if (cs.getCarSeableName().equals(cName))
                return cs;
        }
        return null;
    }
}
