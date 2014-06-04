package com.Doric.CarBook.member;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCollection extends Fragment {

    // �û��ղ�JSON��
    private JSONObject userCollection = null;
    // ������
    ProgressDialog progressDialog;
    // ��ȡ�������ݿ�Ĺ�����ʵ��
    private UserFunctions userFunctions;
    // ��̬����û��ղص��б�
    ListView userCollectionList = null;
    // ͼƬ�б�
    List<ImageView> imageViewList = new ArrayList<ImageView>();
    // ��ͼƬ����Ĺ�����
    private ImageLoader imageLoader;
    // ͼƬ���
    private final int picWidth = 150;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_collection, container, false);
        // ��ȡ�������ݿ�Ĺ�����ʵ��
        userFunctions = new UserFunctions(getActivity());
        // ��ʼ���û��ղ��б�
        userCollectionList = (ListView) rootView.findViewById(R.id.userCollectionList);
        Log.d("UserCollection","GetUserCollection.execute");
        // �����첽�̻߳�ȡJson����������fragment
        new GetUserCollection().execute();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*

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
            setListViewHeightBasedOnChildren();
        }
        */
    }
    /*
     *  �첽��ȡ�û��ղع�����
     */
    private class GetUserCollection extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //����ʱ����
            Log.d("GetUserCollection","progressDialog");
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("������..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //  �ӱ������ݿ��ȡ�û��ղ�Json��
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
                Toast.makeText(getActivity(), "�޷��������磬���������ֻ���������", Toast.LENGTH_LONG).show();
            }

        }
    }
    private void initFragment(){
        Log.d("GetUserCollection","initFragment");
        ArrayList<Map<String, Object>> list = getData();
        SimpleAdapter adapter = new SimpleAdapter(getActivity(),list,R.layout.user_collection_list,
                new String[]{"userCollectionThumPic","carNameText","carGradeText",
                        "carPriceText","collectionBrand","collectionBrandSeries","collectionModelNumber",
                        "car_id"},
                new int[]{R.id.userCollectionThumPic,R.id.carNameText,R.id.carGradeText,
                        R.id.carPriceText,R.id.collectionBrand,R.id.collectionBrandSeries,R.id.collectionModelNumber,
                        R.id.car_id});
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
                String brand_series = map.get("collectionBrandSeries");
                String model_number = map.get("collectionModelNumber");
                String car_id = map.get("car_id");
                Bundle bundle = new Bundle();
                bundle.putString("brand",brand);
                bundle.putString("series",brand_series);
                bundle.putString("model_number",model_number);
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
                map.put("collectionBrand",carInCollection.getString("brand_name"));
                map.put("collectionBrandSeries",carInCollection.getString("brand_series"));
                map.put("collectionModelNumber",carInCollection.getString("model_number"));
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
     * �첽����ͼƬ������
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
                    .equals(Environment.MEDIA_MOUNTED);   //�ж�sd���Ƿ����
            if   (sdCardExist)
            {
                sdDir = Environment.getExternalStorageDirectory();//��ȡ��Ŀ¼
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
//            private String getImagePath(String imageUrl) {
//                int lastSlashIndex = imageUrl.lastIndexOf("/");
//                String imageName = imageUrl.substring(lastSlashIndex + 1);
//                String imageDir = Environment.getExternalStorageDirectory().getPath()
//                        + "/CarBook/Cache/";
//                File file = new File(imageDir);
//                if (!file.exists()) {
//                    file.mkdirs();
//                }
//                String imagePath = imageDir + imageName;
//                return imagePath;
//            }
}


