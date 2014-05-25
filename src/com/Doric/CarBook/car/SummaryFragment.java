package com.Doric.CarBook.car;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.Doric.CarBook.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunyao_Will on 2014/4/3.
 */
public class SummaryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.car_summary, container, false);


        TextView bodyStruText = (TextView) (rootView != null ? rootView.findViewById(R.id.bodyStruText) : null);
        TextView SCBText = (TextView) (rootView != null ? rootView.findViewById(R.id.SCBText) : null);
        TextView driveModeText = (TextView) (rootView != null ? rootView.findViewById(R.id.driveModeText) : null);
        TextView lowCostText = (TextView) (rootView != null ? rootView.findViewById(R.id.lowCostText) : null);


        try {
            if (driveModeText != null) {
                driveModeText.setText("级别:" + CarShow.carInfo.getString("car_grade"));
            }
            if (bodyStruText != null) {
                bodyStruText.setText("车身结构:" + CarShow.carInfo.getString("car_body_structure"));
            }
            if (SCBText != null) {
                SCBText.setText("变速箱:" + CarShow.carInfo.getString("transmission"));
            }
            if (lowCostText != null) {
                lowCostText.setText("价格区间:" + CarShow.carInfo.getString("price"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //构建车行信息
        ListView storeList = (ListView) (rootView != null ? rootView.findViewById(R.id.storeList) : null);
        ArrayList<Map<String, Object>> list = getData();
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), list, R.layout.sale_company_list,
                new String[]{"storeName", "storeAddr"},
                new int[]{R.id.storeName, R.id.storeAddr});
        if (storeList != null) {
            storeList.setAdapter(adapter);
            setListViewHeightBasedOnChildren(storeList);
        }

        return rootView;
    }

    private ArrayList<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            for (Integer i = 1; i <= CarShow.carInfo.getInt("sale_company_num"); i++) {
                map = new HashMap<String, Object>();
                JSONObject carSaleCompany = CarShow.carInfo.getJSONObject("sale_company_" + i.toString());
                map.put("storeName", carSaleCompany.getString("name"));
                map.put("storeAddr", carSaleCompany.getString("address"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*
     * 计算ListView高度，以便ScrollView进行滚动
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            if (listItem != null) {
                listItem.measure(0, 0);
            }
            // 统计所有子项的总高度
            if (listItem != null) {
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (params != null) {
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        }
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }
}
