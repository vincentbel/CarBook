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

    // ��������Json��
    private JSONObject carPrices = null;
    // ������
    ProgressDialog progressDialog;
    // ��̬��ӳ������۵��б�
    ListView carPriceList = null;
    // ���������б��������Ϣ�ļ�ֵ��
    ArrayList<Map<String, Object>> list = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.car_prices, container, false);
        // ��ʼ���û��ղ��б�
        carPriceList = (ListView) rootView.findViewById(R.id.carPricesList);
        // ��ʼ��fragment
        initFragment();
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initFragment (){
        //����������Ϣ
        list = getData();
        /*
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), list, R.layout.sale_company_list,
                new String[]{"storeName", "storeAddr","phoneNumber"},
                new int[]{R.id.storeName, R.id.storeAddr});
        */
        mAdapter adapter = new mAdapter (getActivity());
        if (carPriceList != null) {
            carPriceList.setAdapter(adapter);
        }
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
                map.put("phoneNumber",carSaleCompany.get("telephone"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
    /*
     * ʵ��ΪListView�а�ť���onClick��adapter��
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
                //  ����R�пؼ�
                convertView = mInflater.inflate(R.layout.sale_company_list, null);
                holder.storeAddrText = (TextView)convertView.findViewById(R.id.storeAddr);
                holder.storeNameText = (TextView)convertView.findViewById(R.id.storeName);
                holder.callStoreBtn = (Button)convertView.findViewById(R.id.callStoreBtn);
                convertView.setTag(holder);

            }else {

                holder = (ViewHolder)convertView.getTag();
            }


            //holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
            holder.storeNameText.setText((String)list.get(position).get("storeName"));
            holder.storeAddrText.setText((String)list.get(position).get("storeAddr"));

            holder.callStoreBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //ȡ������ĵ绰���봮
                    String inputStr = (String) list.get(position).get("phoneNumber");
                    //������벻Ϊ�մ�����绰��Intent
                    if (inputStr.trim().length() != 0) {
                        Intent phoneIntent = new Intent("android.intent.action.CALL",
                                Uri.parse("tel:" + inputStr));
                        //����
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
