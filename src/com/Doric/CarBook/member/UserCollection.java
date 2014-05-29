package com.Doric.CarBook.member;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.R;
import com.Doric.CarBook.utility.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserCollection extends Fragment {

    // 用户收藏JSON包
    private JSONObject userCollection = null;
    // 进度条
    ProgressDialog progressDialog;
    // 获取本地数据库的工具类实例
    private UserFunctions userFunctions;
    // 动态添加用户收藏的列表
    ListView userCollectionList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_collection, container, false);
        // 获取本地数据库的工具类实例
        userFunctions = new UserFunctions(getActivity());
        // 初始化用户收藏列表
        userCollectionList = (ListView) (rootView != null ? rootView.findViewById(R.id.bodyStruText) : null);
        // 开启异步线程获取Json包，并构建fragment
        new GetUserCollection().execute();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*

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
    }
    /*
     *  异步获取用户收藏工具类
     */
    private class GetUserCollection extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //加载时弹出
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("加载中..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //  从本地数据库获取用户收藏Json包
            //userCollection = userFunctions.getUserCollection();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            initFragment();
        }
    }
    private void initFragment(){

        ArrayList<Map<String, Object>> list = getData();
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),list,R.layout.user_collection_list,
                new String[]{"userCollectionThumPic","carNameText","carGradeText","carPriceText"},
                new int[]{R.id.userCollectionThumPic,R.id.carNameText,R.id.carGradeText,R.id.carPriceText});
        if (userCollectionList != null) {
            userCollectionList.setAdapter(adapter);
            setListViewHeightBasedOnChildren(userCollectionList);
        }
    }

    ArrayList<Map<String, Object>> getData(){
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject carInCollection = null;
        try {
            for (Integer i=1;i<= userCollection.getInt("number");i++){
                map =  new HashMap<String, Object>();
                carInCollection = userCollection.getJSONObject("car_"+i.toString());
                map.put("carNameText",carInCollection.getString("brand")+" "+carInCollection.getString("brand_series")
                        +" "+carInCollection.getString("model_number"));
                map.put("carGradeText",carInCollection.getString("grade"));
                map.put("carPriceText",carInCollection.getString("price"));

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

}
