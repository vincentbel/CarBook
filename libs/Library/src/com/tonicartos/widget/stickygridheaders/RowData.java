package com.tonicartos.widget.stickygridheaders;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/5/25.
 */
public class RowData{
    private ArrayList<String> strings;

    public RowData(ArrayList<String> strings){
        this.strings = new ArrayList<String>(strings);
    }
    public ArrayList<String> getData(){

        return strings;
    }

}