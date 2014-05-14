package com.Doric.CarBook.member;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.Doric.CarBook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserCollection extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_collection, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View mView = getView();
        //setContentView(R.layout.user_collection);
        /*if (getActionBar() != null) {
            getActionBar().setTitle(R.string.user_collection);
        }*/
        /*

        Intent intent = getIntent();
        intent.getStringExtra("userName");
            ͨ��Json�����û��������ղ�
            String url = "";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", userCollection));
            params.add(new BasicNameValuePair("username", name));
            JSONParser jsonParser = new JSONParser();
            JSONObject loginInfo = jsonParser.getJSONFromUrl(url, params);

            ��̬����ղ���Ŀ
        int userCollectionCount =2;
        */
        /*
        ListView userCollectionList = (ListView) findViewById(R.id.userCollectionList);
        ArrayList<Map<String, Object>> list = getData();
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.sale_company_list,
                new String[]{"storeName","storeAddr"},
                new int[]{R.id.storeName,R.id.storeAddr});
        if (userCollectionList != null) {
            userCollectionList.setAdapter(adapter);
            setListViewHeightBasedOnChildren(userCollectionList);
        }
        */
        ListView userCollectionList = (ListView) mView.findViewById(R.id.userCollectionList);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("carName", "BMW 7 series 2013 740Li");
        map.put("carGrade", "������");
        map.put("carPrice", "117��-144.4��");
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list.add(map);
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.user_collection_list,
                new String[]{"carName", "carGrade", "carPrice"},
                new int[]{R.id.carNameText, R.id.carGradeText, R.id.carPriceText});
        userCollectionList.setAdapter(adapter);

    }
    /*
    ArrayList<Map<String, Object>> getData(){
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            for (Integer i=1;i<= userCollectionJSON.getInt("user_collection_num");i++){
                map =  new HashMap<String, Object>();
                JSONObject carSaleCompany = userCollectionJSON.getJSONObject("user_collection_"+i.toString());
                map.put("carName",userCollection.getString("name"));
                map.put("price",userCollection.getString("address"));
                map.put("carGrade",userCollection.getString("price"));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // ��ȡListView��Ӧ��Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()�������������Ŀ
            View listItem = listAdapter.getView(i, null, listView);
            // ��������View �Ŀ��
            if (listItem != null) {
                listItem.measure(0, 0);
            }
            // ͳ������������ܸ߶�
            if (listItem != null) {
                totalHeight += listItem.getMeasuredHeight();
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (params != null) {
            params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        }
        // listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�
        // params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�
        listView.setLayoutParams(params);
    }
    */
}
