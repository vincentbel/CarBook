package com.tonicartos.widget.stickygridheaders;

import java.util.ArrayList;

/**
 * Created by Administrator on 2014/5/25.
 */
public class HeaderGridData{
    private   String header;
    private ArrayList<RowData> rowDatas;
    public HeaderGridData(String header,ArrayList<RowData> rowDatas){
        this.header = header;
        this.rowDatas = rowDatas;
    }
    public String getHeader(){
        return header;
    }

    public ArrayList<String> getRowDatas(){
        ArrayList<String> str=  new ArrayList<String>();
        for(RowData rd : rowDatas){
            str.addAll(rd.getData());
        }
        return str;
    }


}
