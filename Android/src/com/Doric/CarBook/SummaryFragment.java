package com.Doric.CarBook;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.CarBook_master.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunyao_Will on 2014/4/3.
 */
public class SummaryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =inflater.inflate(R.layout.car_summary, container, false);

        TextView bodyStruText = (TextView) rootView.findViewById(R.id.bodyStruText);
        TextView SCBText = (TextView) rootView.findViewById(R.id.SCBText);
        TextView driveModeText = (TextView) rootView.findViewById(R.id.driveModeText);
        TextView lowCostText = (TextView) rootView.findViewById(R.id.lowCostText);

        bodyStruText.setText("车身结构：十门十座");
        SCBText.setText("变速箱：无极十档变速");
        driveModeText.setText("驱动模式：前后都行");
        lowCostText.setText("最低价格：不要钱");

        TextView store1Name = (TextView) rootView.findViewById(R.id.store1Name);
        TextView store2Name = (TextView) rootView.findViewById(R.id.store2Name);
        TextView store3Name = (TextView) rootView.findViewById(R.id.store3Name);
        TextView store4Name = (TextView) rootView.findViewById(R.id.store4Name);
        TextView store5Name = (TextView) rootView.findViewById(R.id.store5Name);

        store1Name.setText("1店");
        store2Name.setText("2店");
        store3Name.setText("3店");
        store4Name.setText("4店");
        store5Name.setText("5店");

        TextView store1Addr = (TextView) rootView.findViewById(R.id.store1Addr);
        TextView store2Addr = (TextView) rootView.findViewById(R.id.store2Addr);
        TextView store3Addr = (TextView) rootView.findViewById(R.id.store3Addr);
        TextView store4Addr = (TextView) rootView.findViewById(R.id.store4Addr);
        TextView store5Addr = (TextView) rootView.findViewById(R.id.store5Addr);

        store1Addr.setText("找不着。");
        store2Addr.setText("找不着。");
        store3Addr.setText("找不着。");
        store4Addr.setText("找不着。");
        store5Addr.setText("找不着。");

        return rootView;
}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}
