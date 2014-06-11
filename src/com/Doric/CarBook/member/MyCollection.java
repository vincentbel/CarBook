package com.Doric.CarBook.member;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;
import com.Doric.CarBook.car.CarShow;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;

public class MyCollection extends Fragment {

    // 进度条
    ProgressBar progressBar;
    // 动态添加用户收藏的列表
    ListView userCollectionList = null;
    // 无用户收藏的提示
    TextView noCollectionTextView = null;
    // 用户收藏JSON包
    private JSONObject userCollection = null;
    // 获取本地数据库的工具类实例
    private UserFunctions userFunctions;
    // 对图片管理的工具类
    private ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_collection, container, false);
        // 获取本地数据库的工具类实例
        userFunctions = new UserFunctions(getActivity());
        // 初始化用户收藏列表
        userCollectionList = (ListView) rootView.findViewById(R.id.my_collection_list);
        progressBar = (ProgressBar) rootView.findViewById(R.id.collection_progress_bar);
        // 初始化TextView
        noCollectionTextView = (TextView) rootView.findViewById(R.id.no_collection);
        Log.d("UserCollection", "GetUserCollection.execute");
        // 开启异步线程获取Json包，并构建fragment
        new GetUserCollection().execute();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initFragment() {
        // 添加适配器
        userCollectionList.setAdapter(new MyCollectionAdapter(getActivity(), userCollection));
        userCollectionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    String car_id = userCollection.getJSONObject("car_" + (position + 1)).getString("car_id");
                    Bundle bundle = new Bundle();
                    bundle.putString("car_id", car_id);
                    // 跳转到对应Activity
                    Intent intent = new Intent(getActivity(), CarShow.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    static class ViewHolder {
        TextView carNameTextView;
        TextView carGradeTextView;
        TextView carPriceTextView;
        ImageView imageView;
    }

    /*
     *  异步获取用户收藏工具类
     */
    private class GetUserCollection extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            //  从本地数据库获取用户收藏Json包
            userCollection = userFunctions.getMyCollection();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            if (userCollection != null) {
                try {
                    if (userCollection.getInt("number") == 0) {
                        noCollectionTextView.setVisibility(View.VISIBLE);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initFragment();
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
            }
        }
    }

    private class MyCollectionAdapter extends ArrayAdapter<String> {

        private Context context;
        private JSONObject values;

        public MyCollectionAdapter(Context context, JSONObject values) {
            super(context, R.layout.user_collection_list);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount() {
            int count = 0;
            try {
                count = userCollection.getInt("number");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return count;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            final ViewHolder viewHolder;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.user_collection_list, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.carNameTextView = (TextView) rowView.findViewById(R.id.my_collection_car_name);
                viewHolder.carGradeTextView = (TextView) rowView.findViewById(R.id.my_collection_car_grade);
                viewHolder.carPriceTextView = (TextView) rowView.findViewById(R.id.my_collection_car_price);
                viewHolder.imageView = (ImageView) rowView.findViewById(R.id.my_collection_car_image);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }
            JSONObject carInCollection;
            String carName = null;
            String carGrade = null;
            String carPrice = null;
            String imageUrl = null;
            try {
                carInCollection = values.getJSONObject("car_" + (position + 1));
                carName = carInCollection.getString("brand") + " " + carInCollection.getString("brand_series")
                        + " " + carInCollection.getString("model_number");
                carGrade = carInCollection.getString("grade");
                carPrice = carInCollection.getString("price");
                imageUrl = Constant.BASE_URL + carInCollection.getString("pictures_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            viewHolder.carNameTextView.setText(carName);
            viewHolder.carGradeTextView.setText(carGrade);
            viewHolder.carPriceTextView.setText(carPrice);
            imageLoader.displayImage(imageUrl, viewHolder.imageView);
            return rowView;
        }
    }
}
