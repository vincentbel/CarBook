package com.Doric.CarBook.car;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.Doric.CarBook.R;

/**
 * Created by Sunyao_Will on 2014/4/3.
 */
public class SummaryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.car_summary, container, false);

        TextView bodyStruText = (TextView) rootView.findViewById(R.id.bodyStruText);
        TextView SCBText = (TextView) rootView.findViewById(R.id.SCBText);
        TextView driveModeText = (TextView) rootView.findViewById(R.id.driveModeText);
        TextView lowCostText = (TextView) rootView.findViewById(R.id.lowCostText);

        bodyStruText.setText("����ṹ��ʮ��ʮ��");
        SCBText.setText("�����䣺�޼�ʮ������");
        driveModeText.setText("��ģʽ��ǰ����");
        lowCostText.setText("��ͼ۸񣺲�ҪǮ");

        TextView store1Name = (TextView) rootView.findViewById(R.id.store1Name);
        TextView store2Name = (TextView) rootView.findViewById(R.id.store2Name);
        TextView store3Name = (TextView) rootView.findViewById(R.id.store3Name);
        TextView store4Name = (TextView) rootView.findViewById(R.id.store4Name);
        TextView store5Name = (TextView) rootView.findViewById(R.id.store5Name);

        store1Name.setText("1��");
        store2Name.setText("2��");
        store3Name.setText("3��");
        store4Name.setText("4��");
        store5Name.setText("5��");

        TextView store1Addr = (TextView) rootView.findViewById(R.id.store1Addr);
        TextView store2Addr = (TextView) rootView.findViewById(R.id.store2Addr);
        TextView store3Addr = (TextView) rootView.findViewById(R.id.store3Addr);
        TextView store4Addr = (TextView) rootView.findViewById(R.id.store4Addr);
        TextView store5Addr = (TextView) rootView.findViewById(R.id.store5Addr);

        store1Addr.setText("�Ҳ��š�");
        store2Addr.setText("�Ҳ��š�");
        store3Addr.setText("�Ҳ��š�");
        store4Addr.setText("�Ҳ��š�");
        store5Addr.setText("�Ҳ��š�");

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}
