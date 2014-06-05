package com.Doric.CarBook.car;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.Doric.CarBook.R;
import com.Doric.CarBook.member.UserFunctions;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sunyao_Will on 2014/4/3.
 */
public class PriceFragment extends Fragment {

    // 车辆报价Json包
    private JSONObject carPrices = null;
    // 进度条
    ProgressDialog progressDialog;
    // 动态添加车辆报价的列表
    ListView carPriceList = null;
    // 向车辆报价列表中添加信息的键值对
    ArrayList<Map<String, Object>> list = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.car_prices, container, false);
        // 初始化用户收藏列表
        carPriceList = (ListView) rootView.findViewById(R.id.carPricesList);
        // 初始化fragment
        initFragment();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initFragment (){
        //构建车行信息
        list = getData();
       // 自定义的适配器
        mAdapter adapter = new mAdapter (getActivity());
        if (carPriceList != null) {
            // 添加适配器
            carPriceList.setAdapter(adapter);
        }
    }
    private ArrayList<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            for (Integer i = 1; i <= CarShow.carInfo.getInt("sale_company_num"); i++) {
                map = new HashMap<String, Object>();
                // 获取对应商店信息
                JSONObject carSaleCompany = CarShow.carInfo.getJSONObject("sale_company_" + i.toString());
                // 将信息添加到map中
                map.put("storeName", carSaleCompany.getString("name"));
                map.put("storeAddr", carSaleCompany.getString("address"));
                map.put("phoneNumber",carSaleCompany.get("telephone"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    /*
     * 实现为ListView中按钮添加onClick的adapter类
     */
    private class mAdapter extends BaseAdapter {
        private LayoutInflater mInflater;


        public mAdapter(Context context){
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {

                holder=new ViewHolder();
                //  关联R中控件
                convertView = mInflater.inflate(R.layout.sale_company_list, null);
                holder.storeAddrText = (TextView)convertView.findViewById(R.id.storeAddr);
                holder.storeNameText = (TextView)convertView.findViewById(R.id.storeName);
                holder.callStoreBtn = (Button)convertView.findViewById(R.id.callStoreBtn);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }


            // 通过位置信息存储对应数据
            holder.storeNameText.setText((String)list.get(position).get("storeName"));
            holder.storeAddrText.setText((String)list.get(position).get("storeAddr"));

            holder.callStoreBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //取得输入的电话号码串
                    String inputStr = (String) list.get(position).get("phoneNumber");
                    //如果输入不为空创建打电话的Intent
                    if (inputStr.trim().length() != 0) {
                        Intent phoneIntent = new Intent("android.intent.action.CALL",
                                Uri.parse("tel:" + inputStr));
                        //启动
                        startActivity(phoneIntent);
                    }
                }
            });


            return convertView;
        }
    }
    class ViewHolder {
        TextView storeNameText;
        TextView storeAddrText;
        Button  callStoreBtn;
    }
}
