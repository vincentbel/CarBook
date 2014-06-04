package com.Doric.CarBook.member;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;
import com.Doric.CarBook.car.CarShow;
import com.Doric.CarBook.car.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    // 图片列表
    List<ImageView> imageViewList = new ArrayList<ImageView>();
    // 对图片管理的工具类
    private ImageLoader imageLoader;
    // 图片宽度
    private final int picWidth = 150;
    // 无用户收藏的提示
    TextView noCollectionTextView = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_collection, container, false);
        // 获取本地数据库的工具类实例
        userFunctions = new UserFunctions(getActivity());
        // 初始化用户收藏列表
        userCollectionList = (ListView) rootView.findViewById(R.id.userCollectionList);
        // 初始化TextView
        noCollectionTextView = (TextView) rootView.findViewById(R.id.noCollectionTextView);
        Log.d("UserCollection","GetUserCollection.execute");
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
            setListViewHeightBasedOnChildren();
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
            Log.d("GetUserCollection","progressDialog");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("加载中..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //  从本地数据库获取用户收藏Json包
            userCollection = userFunctions.getMyCollection();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            super.onPostExecute(aVoid);
            if (userCollection!=null){
                initFragment();
                new GetPicData().start();
            }else{
                Toast.makeText(getActivity(), "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
            }

        }
    }
    private void initFragment(){
        Log.d("GetUserCollection","initFragment");
        try {
            if(userCollection.getInt("number")==0){
                noCollectionTextView.setVisibility(View.VISIBLE);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayList<Map<String, Object>> list = getData();
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),list,R.layout.user_collection_list,
                new String[]{"userCollectionThumPic","carNameText","carGradeText",
                        "carPriceText","car_id"},
                new int[]{R.id.userCollectionThumPic,R.id.carNameText,R.id.carGradeText,
                        R.id.carPriceText,R.id.car_id});
        adapter.setViewBinder(new myViewBinder());
        if (userCollectionList != null) {
            userCollectionList.setAdapter(adapter);
            //setListViewHeightBasedOnChildren(userCollectionList);
        }
        userCollectionList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("douBI!!!!!!!!!!!!!!!!!!!!!!!!!");

                ListView tempList = (ListView) parent;
                HashMap<String, String> map = (HashMap<String, String>) tempList.getItemAtPosition(position);

                String brand = map.get("collectionBrand");
/*                String brand_series = map.get("collectionBrandSeries");
                String model_number = map.get("collectionModelNumber");*/
                String car_id = map.get("car_id");
                Bundle bundle = new Bundle();
                bundle.putString("brand",brand);
/*                bundle.putString("series",brand_series);
                bundle.putString("model_number",model_number);*/
                bundle.putString("car_id",car_id);

                Intent intent = new Intent(getActivity(),CarShow.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    ArrayList<Map<String, Object>> getData(){
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject carInCollection = null;
        try {

            for (Integer i=1;i<= userCollection.getInt("number");i++){
                map =  new HashMap<String, Object>();
                carInCollection = userCollection.getJSONObject("car_"+i.toString());
                map.put("carNameText",carInCollection.getString("brand_name")+" "+carInCollection.getString("brand_series")
                        +" "+carInCollection.getString("model_number"));
/*                map.put("collectionBrand",carInCollection.getString("brand_name"));
                map.put("collectionBrandSeries",carInCollection.getString("brand_series"));
                map.put("collectionModelNumber",carInCollection.getString("model_number"));*/
                map.put("carGradeText",carInCollection.getString("grade"));
                map.put("carPriceText",carInCollection.getString("price"));
                map.put("car_id",carInCollection.getString("car_id"));
                map.put("userCollectionThumPic",R.drawable.ic_launcher);
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
    class myViewBinder implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data, String textRepresentation) {
            if ((view instanceof ImageView) & (data instanceof Bitmap)) {
                ImageView iv = (ImageView) view;
                Bitmap bmp = (Bitmap) data;
                iv.setImageBitmap(bmp);
                return true;
            }
            return false;
        }
    }
    final Handler cwjHandler = new Handler();
    class UpdateRunnable implements  Runnable{
        SimpleAdapter simpleAdapter = null;
        public UpdateRunnable(SimpleAdapter sa){
            simpleAdapter = sa;
        }
        public void run() {
            simpleAdapter.notifyDataSetChanged();
        }
    };
    /**
     * 异步下载图片的任务。
     */
    public class GetPicData extends Thread {
        // private Set<LoadImage> taskSet;

        public GetPicData() {

            //taskSet = new HashSet<LoadImage>();
        }

        public void run() {
            Log.d("GetPicData","run");
                //LoadImage i =  new LoadImage(cs.getCarSeableName(),cs.getPicPath());
                HttpURLConnection con = null;
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                BufferedInputStream bis = null;
                File imageFile = null;
                SimpleAdapter simpleAdapter = (SimpleAdapter) userCollectionList.getAdapter();
                for (Integer i = 1; i <= simpleAdapter.getCount(); i++) {
                    Log.d("GetPicData","download"+i.toString());
                    Map<String, Object> map = (Map<String, Object>) simpleAdapter.getItem(i-1);
                    Bitmap bitmap =null;
                    JSONObject car = null;
                    String imageUrl = null;
                    try {
                        System.out.println(userCollection.toString());
                        car = userCollection.getJSONObject("car_"+i.toString());
                        imageUrl=Constant.BASE_URL + "/"+ car.getString("pictures_url") ;

                        System.out.println(imageUrl);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    imageFile = new File(getImagePath(imageUrl));
                    try {
                        if (!imageFile.exists()) {
                            URL url = new URL(Transform(imageUrl.replace(" ","%20")));
                            System.out.println(url);
                            con = (HttpURLConnection) url.openConnection();
                            con.setConnectTimeout(5 * 1000);
                            con.setReadTimeout(15 * 1000);
                            con.setDoInput(true);
                            con.setDoOutput(true);
                            bis = new BufferedInputStream(con.getInputStream());
                            imageFile = new File(getImagePath(imageUrl));
                            fos = new FileOutputStream(imageFile);
                            bos = new BufferedOutputStream(fos);
                            byte[] b = new byte[1024];
                            int length;
                            while ((length = bis.read(b)) != -1) {
                                bos.write(b, 0, length);
                                bos.flush();
                            }
                            if (bis != null) {
                                bis.close();
                            }
                            if (bos != null) {
                                bos.close();
                            }
                            bitmap = BitmapFactory.decodeFile(getImagePath(imageUrl));

                        }
                        else{
                            bitmap = BitmapFactory.decodeFile(getImagePath(imageUrl));
                        }
                        if (bitmap!= null) {
                            map.put("userCollectionThumPic", bitmap);
                            cwjHandler.post(new UpdateRunnable(simpleAdapter));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }

        private String getSDPath(){
            File sdDir = null;
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
            if   (sdCardExist)
            {
                sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            }
            return sdDir.toString();

        }


        private String getImagePath(String imageUrl) {
            int lastSlashIndex = imageUrl.lastIndexOf("/");
            String imageTPath = imageUrl.substring(0, lastSlashIndex);
            String extra = imageUrl.substring(imageUrl.lastIndexOf("."));
            lastSlashIndex = imageTPath.lastIndexOf("/");
            String imageSeries = imageTPath.substring(lastSlashIndex + 1);  //  Series
            imageTPath = imageTPath.substring(0, lastSlashIndex);
            String imageName = imageTPath.substring(imageTPath.lastIndexOf("/") + 1);
            imageName = imageName + imageSeries + extra;
            System.out.println(imageName);
            String imageDir = getSDPath()
                    + "/CarBook/Cache/";
            File file = new File(imageDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String imagePath = imageDir + imageName;

            return imagePath;
        }
    public static String Transform(String str){
        byte[] b = str.getBytes();
        char[] c = new char[b.length];
        for (int i=0;i<b.length;i++){
            if(b[i]!=' ')
                c[i] = (char)(b[i]&0x00FF);

        }
        return new String(c);
    }
}


