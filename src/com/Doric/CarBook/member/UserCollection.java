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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    private ImageLoader imageLoader=ImageLoader.getInstance();;
    // 图片宽度
    private final int picWidth = 150;
    // 无用户收藏的提示
    TextView noCollectionTextView = null;
    //  记录所加载的图片宽度，避免内存溢出
    private final int columnWidth = 480 ;
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
                try {
                    if(userCollection.getInt("number")==0){
                        noCollectionTextView.setVisibility(View.VISIBLE);
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initFragment();
                new GetPicData().execute();
            }else{
                Toast.makeText(getActivity(), "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
            }

        }
    }
    private void initFragment(){
        Log.d("GetUserCollection","initFragment");
        // 获取数据
        ArrayList<Map<String, Object>> list = getData();
        // 设置适配器，包括car_id,carName,carGrade,carPrice和图片信息
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),list,R.layout.user_collection_list,
                new String[]{"userCollectionThumPic","carNameText","carGradeText",
                        "carPriceText","car_id"},
                new int[]{R.id.userCollectionThumPic,R.id.carNameText,R.id.carGradeText,
                        R.id.carPriceText,R.id.car_id});
        // 自定义绑定bitmap
        adapter.setViewBinder(new myViewBinder());
        if (userCollectionList != null) {
            // 添加适配器
            userCollectionList.setAdapter(adapter);
        }
        userCollectionList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取父listView
                ListView tempList = (ListView) parent;
                HashMap<String, String> map = (HashMap<String, String>) tempList.getItemAtPosition(position);

                // 从map中获取car_id
                String car_id = map.get("car_id");

                Bundle bundle = new Bundle();
                bundle.putString("car_id",car_id);
                // 跳转到对应Activity
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
                // 获取对应车辆
                carInCollection = userCollection.getJSONObject("car_"+i.toString());

                // 将信息添加到map中

                map.put("carNameText",carInCollection.getString("brand")+" "+carInCollection.getString("brand_series")
                        +" "+carInCollection.getString("model_number"));
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

    /*
    * 自定义绑定
    */
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
    /*
    * ListView更新消息处理
    */
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

    private class GetPicData extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {

        }


        @Override
        protected Void doInBackground(Void... params) {
            JSONObject car = null;
            SimpleAdapter simpleAdapter = (SimpleAdapter) userCollectionList.getAdapter();

            for (Integer i = 1; i <= simpleAdapter.getCount(); i++) {
                Log.d("GetPicData", "download" + i.toString());
                Map<String, Object> map = (Map<String, Object>) simpleAdapter.getItem(i - 1);
                String mImageUrl = null;
                try {
                    // 获取对应车辆的图片URL
                    car = userCollection.getJSONObject("car_" + i.toString());
                    mImageUrl = Constant.BASE_URL + "/" + car.getString("pictures_url");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 从缓存中加载
                Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);
                if (imageBitmap == null) {
                    System.out.println("缓存中不存在位图，需要下载或从sd卡加载图片");
                    // 缓存中不存在位图，需要下载或从sd卡加载图片
                    imageBitmap = loadImage(mImageUrl);
                }else {
                    System.out.println("缓存中存在位图，不需要加载图片");
                }
                // 将imageBitmap 添加到 map对应位置中
                map.put("userCollectionThumPic", imageBitmap);
                // 发送更新消息
                cwjHandler.post(new UpdateRunnable(simpleAdapter));

            }
            return null;
        }

        /*
         * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
         *
         * @param imageUrl 图片的URL地址
         * @return 加载到内存的图片。
         */
        private Bitmap loadImage(String imageUrl) {
            File imageFile = new File(getImagePath(imageUrl));
            if (!imageFile.exists()) {
                System.out.println("sd卡中不存在准备从服务器下载");
                // sd卡中不存在准备从服务器下载
                downloadImage(imageUrl);
            } else {
                System.out.println("sd卡中存在");
            }
            if (imageUrl != null) {
                System.out.println("从sd卡对应路径加载图片");
                // 从sd卡对应路径加载图片，并根据columnWidth解码
                Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(),
                        columnWidth);
                if (bitmap != null) {
                    // 成功获取图片，将图片加入缓存
                    System.out.println("成功获取图片，将图片加入缓存");
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                    return bitmap;
                }
            }
            return null;
        }

        /*
         * 将图片下载到SD卡缓存起来。
         *
         * @param  。
         */
        private void downloadImage(String imageUrl) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.d("TAG", "monted sdcard");
            } else {
                Log.d("TAG", "has no sdcard");
            }
            HttpURLConnection con = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            File imageFile = null;
            try {
                URL url = new URL(Transform(imageUrl.replace(" ","%20")));
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
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                    if (con != null) {
                        con.disconnect();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (imageFile != null) {
                System.out.println("网络图片获取成功");
                Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(),
                        columnWidth);
                if (bitmap != null) {
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                }
            }else {
                System.out.println("网络图片获取不成功");
            }
        }

        protected void onPostExecute(Void aVoid) {

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

    /**
     * 获取图片的本地存储路径。
     *
     * @param imageUrl 图片的URL地址。
     * @return 图片的本地存储路径。
     */

    private String getImagePath(String imageUrl) {
        int lastSlashIndex = imageUrl.lastIndexOf("/");
        String imageTPath = imageUrl.substring(0, lastSlashIndex);
        // 图片序号及格式后缀
        String extra ="_"+ imageUrl.substring(imageUrl.lastIndexOf("/")+1);

        lastSlashIndex = imageTPath.lastIndexOf("/");
        String imageSeries = imageTPath.substring(lastSlashIndex + 1);  //  Series
        imageTPath = imageTPath.substring(0, lastSlashIndex);
        String imageName = imageTPath.substring(imageTPath.lastIndexOf("/") + 1);
        imageName = imageName + imageSeries + extra;
        System.out.println(imageName);
        // 图片的储存路径
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


