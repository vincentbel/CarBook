package com.Doric.CarBook.search;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.app.Fragment;
import org.apache.http.NameValuePair;


public class ConditionSearch extends Fragment {

    Spinner spinner;
    CheckBox carSize;
    Button search;
    Grade grade;
    ArrayAdapter<String> adapter;
    ArrayList<PriceGrade> priceGrades;
    RelativeLayout relativeLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //��ȡ����Ʒ����Ϣ
        relativeLayout = (RelativeLayout) inflater.inflate(R.layout.sea_condition_search, container, false);
        if (relativeLayout == null)
            return null;
        initPage();
        return relativeLayout;

    }

    @Override
    public void onResume() {
        super.onResume();
        SearchMain.searchmain.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }


    public void initPage() {

        priceGrades = new ArrayList<PriceGrade>();
        grade = new Grade();

        spinner = (Spinner) relativeLayout.findViewById(R.id.carPrice);
        search = (Button) relativeLayout.findViewById(R.id.csearchbutton);
        createCarPriceGrades();
        String[] text = new String[priceGrades.size()];
        for (int i = 0; i < text.length; i++) {
            text[i] = priceGrades.get(i).text;
        }

        adapter = new ArrayAdapter<String>(SearchMain.searchmain, android.R.layout.simple_spinner_item, text);
        //���������б�ķ��

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //��adapter ��ӵ�spinner��

        spinner.setAdapter(adapter);

        //����Ĭ��ֵ

        spinner.setVisibility(View.VISIBLE);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createGrade(grade);
                if (grade.isChoose()) {


                    String text = (String) spinner.getSelectedItem();
                    Double l = new Double(0.0);
                    Double h = new Double(0.0);
                    Price p =findPrice(text);

                    SearchMain.searchmain.SearchToResult(p.low,p.high,grade);
                    /*
                    Intent it = new Intent();
                    Toast.makeText(ConditionSearch.this, text,Toast.LENGTH_LONG).show();
                    it.putExtra("pricelow",l);
                    it.putExtra("pricehig",h);
                    it.putExtra("grade",grade);
                    it.setClass(ConditionSearch.this, Result.class);
                    */
                    //ConditionSearch.this.startActivity(it);
                    //ConditionSearch.this.finish();
                }
            }

        });
    }

    private boolean isInt(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= '0' && str.charAt(i) <= '9') {

            } else {
                return false;
            }
        }
        return true;
    }

    private void createCarPriceGrades() {
        PriceGrade p = new PriceGrade();
        p.text = "5������";
        p.higPrice = 50000.0;
        p.lowPrice = 0.0;
        priceGrades.add(p);

        p = new PriceGrade();
        p.text = "5��8��";
        p.higPrice = 80000.0;
        p.lowPrice = 50000.0;
        priceGrades.add(p);

        p = new PriceGrade();
        p.text = "8������";
        p.higPrice = 999999999.0;
        p.lowPrice = 80000.0;
        priceGrades.add(p);
    }

    private void createGrade(Grade g) {

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox00);
        CharSequence cs = carSize.getText();
        Boolean b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox01);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox02);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox10);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox11);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox12);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox20);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox21);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox22);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox30);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox31);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox32);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox40);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox41);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox42);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox50);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

        carSize = (CheckBox) relativeLayout.findViewById(R.id.checkbox51);
        cs = carSize.getText();
        b = carSize.isChecked();
        g.setGradeMap(cs.toString(), b);

    }
    class Price{
        public Double high;
        public Double low;
    }


    private Price findPrice(String text) {
        for (int i = 0; i < priceGrades.size(); i++) {
            PriceGrade pg= priceGrades.get(i);
            if (pg.text.equals(text)) {
                Price p= new Price();
                p.low = pg.lowPrice;
                p.high = pg.higPrice;
                return p;

            }
        }
        Price price =new Price();
        price.high = 0.0;
        price.low  = 0.0;
        return price;
    }

}

class PriceGrade {
    public String text;
    public Double lowPrice;
    public Double higPrice;
}


class Grade implements Serializable {
    static String[] mstring;
    public  Map<String, Boolean> gradeMap = new HashMap<String, Boolean>();

    public Grade() {
        mstring = new String[17];
        mstring[0] = "΢�ͳ�";
        mstring[1] = "С�ͳ�";
        mstring[2] = "�����ͳ�";
        mstring[3] = "���ͳ�";
        mstring[4] = "�д��ͳ�";
        mstring[5] = "������";
        mstring[6] = "С��SUV";
        mstring[7] = "������SUV";
        mstring[8] = "����SUV";
        mstring[9] = "�д���SUV";
        mstring[10] = "ȫ�ߴ�SUV";
        mstring[11] = "MPV";
        mstring[12] = "�ܳ�";
        mstring[13] = "Ƥ��";
        mstring[14] = "΢��";
        mstring[15] = "���";
        mstring[16] = "΢��";


    }

    public void setGradeMap(String s, Boolean b) {
        if (gradeMap.containsKey(s)) {
            gradeMap.remove(s);
            gradeMap.put(s, b);
        } else {
            gradeMap.put(s, b);
        }
    }

    public boolean isChoose() {
        Collection<Boolean> lb = gradeMap.values();
        return lb.contains(Boolean.TRUE);
    }

    public boolean getValue(String key) {
        return gradeMap.get(key);
    }

}

