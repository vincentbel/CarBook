package com.Doric.CarBook.search;

import java.util.Comparator;

class ComparatorCarInfo implements Comparator<CarInfor> {
    @Override
    public int compare(CarInfor o1, CarInfor o2) {
        return ToPinYinString(o1.getCarName().toUpperCase()).compareTo(ToPinYinString(o2.getCarName()).toUpperCase());
    }

    private String ToPinYinString(String str) {
        return PinyinUtil.getFullSpell(str);
    }
}


class ComparatorCarSeable implements Comparator<CarSeable> {
    @Override
    public int compare(CarSeable o1, CarSeable o2) {

        return ToPinYinString(o1.getCarSeableName()).toUpperCase().compareTo(ToPinYinString(o2.getCarSeableName()).toUpperCase());
    }

    private String ToPinYinString(String str) {


        return PinyinUtil.getFullSpell(str);
    }

}


class ComparatorCarSeries implements Comparator<CarSeries> {
    @Override
    public int compare(CarSeries o1, CarSeries o2) {
        return ToPinYinString(o1.getName()).toUpperCase().compareTo(ToPinYinString(o2.getName()).toUpperCase());
    }

    private String ToPinYinString(String str) {
        return PinyinUtil.getFullSpell(str);
    }
}


