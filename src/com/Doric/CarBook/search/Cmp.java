package com.Doric.CarBook.search;

import java.util.Comparator;

/**
 * 三个比较类
 */
class ComparatorCarInfo implements Comparator<CarInfor> {
    @Override
    public int compare(CarInfor o1, CarInfor o2) {
        if(!o1.getCarSeable().equals(o2.getCarSeable()))
            return ToPinYinString(o1.getCarSeable()).toUpperCase().compareTo(ToPinYinString(o2.getCarSeable()).toUpperCase());
        else if(!o1.getCarSerie().equals((o2.getCarSerie())))
            return ToPinYinString(o1.getCarSerie()).toUpperCase().compareTo(ToPinYinString(o2.getCarSerie()).toUpperCase());
        else
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
        if(!o1.getCarSeableName().equals(o2.getCarSeableName()))
            return ToPinYinString(o1.getCarSeableName()).toUpperCase().compareTo(ToPinYinString(o2.getCarSeableName()).toUpperCase());
        return ToPinYinString(o1.getName()).toUpperCase().compareTo(ToPinYinString(o2.getName()).toUpperCase());
    }

    private String ToPinYinString(String str) {
        return PinyinUtil.getFullSpell(str);
    }
}


