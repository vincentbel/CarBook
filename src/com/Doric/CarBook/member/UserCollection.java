package com.Doric.CarBook.member;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.R;
import com.Doric.CarBook.car.CarShow;
import com.Doric.CarBook.car.SummaryFragment;
import org.json.JSONException;
import org.json.JSONObject;

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
            通过Json构造用户的所有收藏
            String url = "";
                List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("tag", userCollection));
            params.add(new BasicNameValuePair("username", name));
            JSONParser jsonParser = new JSONParser();
            JSONObject loginInfo = jsonParser.getJSONFromUrl(url, params);

            动态添加收藏条目
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
        map.put("carName","BMW 7 series 2013 740Li");
        map.put("carGrade","豪华车");
        map.put("carPrice","117万-144.4万");
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        list.add(map);
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),list,R.layout.user_collection_list,
                new String[]{"carName","carGrade","carPrice"},
                new int[]{R.id.carNameText,R.id.carGradeText,R.id.carPriceText});
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
            params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        }
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    */
}
