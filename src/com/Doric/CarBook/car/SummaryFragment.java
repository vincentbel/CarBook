package com.Doric.CarBook.car;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Sunyao_Will on 2014/4/3.
 */
public class SummaryFragment extends Fragment {

    private final int picWidth = 150; // ͼƬ��ȡ�

    private ImageLoader imageLoader; // ��ͼƬ����Ĺ�����

    ArrayList<Map<String, Object>> list = null;    // ���������б��������Ϣ�ļ�ֵ��

    private final int storeNum = 5;

    private ImageView thumPic = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.car_summary, container, false);

        TextView bodyStruText = (TextView) (rootView != null ? rootView.findViewById(R.id.bodyStruText) : null);
        TextView SCBText = (TextView) (rootView != null ? rootView.findViewById(R.id.SCBText) : null);
        TextView driveModeText = (TextView) (rootView != null ? rootView.findViewById(R.id.driveModeText) : null);
        TextView lowCostText = (TextView) (rootView != null ? rootView.findViewById(R.id.lowCostText) : null);


        thumPic = (ImageView) (rootView != null ? rootView.findViewById(R.id.thumPic) : null);

        imageLoader = new ImageLoader();
        // ͨ���������߳�������ͼƬ
        LoadImageTask task = new LoadImageTask();
        task.execute();
        // ΪͼƬ����onClick����
        thumPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImageDetailsActivity.class);
                intent.putExtra("image_position", 0);
                getActivity().startActivity(intent);
            }
        });
        try {
            if (driveModeText != null) {
                driveModeText.setText("����:" + CarShow.carInfo.getString("car_grade"));
            }
            if (bodyStruText != null) {
                bodyStruText.setText("����ṹ:" + CarShow.carInfo.getString("car_body_structure"));
            }
            if (SCBText != null) {
                SCBText.setText("������:" + CarShow.carInfo.getString("transmission"));
            }
            if (lowCostText != null) {
                lowCostText.setText("�۸�����:" + CarShow.carInfo.getString("price"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //����������Ϣ
        ListView storeList = (ListView) (rootView != null ? rootView.findViewById(R.id.storeList) : null);
        list = getData();
        /*
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), list, R.layout.sale_company_list,
                new String[]{"storeName", "storeAddr","phoneNumber"},
                new int[]{R.id.storeName, R.id.storeAddr});
        */
        mAdapter adapter = new mAdapter (getActivity());
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
            for (Integer i = 1; i <= storeNum; i++) {
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
     * ����ListView�߶ȣ��Ա�ScrollView���й���
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
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
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        }
        // listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�
        // params.height���õ�����ListView������ʾ��Ҫ�ĸ߶�
        listView.setLayoutParams(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);


    }

    /**
     * �첽����ͼƬ������
     */
    private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
        /**
         * ͼƬ��URL��ַ
         */
        private String mImageUrl;

        /**
         * ImageView
         */
        private ImageView mImageView;

        public LoadImageTask() {
            super();
        }

        /**
         * �����ظ�ʹ�õ�ImageView����
         *
         * @param imageView
         */
        public LoadImageTask(ImageView imageView) {
            super();
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                mImageUrl = Constant.BASE_URL + "/"+ CarShow.carInfo.getString("pictures_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println("picture url: "+mImageUrl);

            Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);

            if (imageBitmap == null) {
                imageBitmap = loadImage(mImageUrl);
            }
            return imageBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                double ratio = bitmap.getWidth() / (picWidth * 1.0);
                int scaledHeight = (int) (bitmap.getHeight() / ratio);
                addImage(bitmap, picWidth, scaledHeight);
            }
        }

        /**
         * ���ݴ����URL����ͼƬ���м��ء��������ͼƬ�Ѿ�������SD���У���ֱ�Ӵ�SD�����ȡ������ʹ����������ء�
         *
         * @param imageUrl ͼƬ��URL��ַ
         * @return ���ص��ڴ��ͼƬ��
         */
        private Bitmap loadImage(String imageUrl) {
            File imageFile = new File(getImagePath(imageUrl));
            if (!imageFile.exists()) {
                downloadImage(imageUrl);
            }
            if (imageUrl != null) {
                Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(),
                        picWidth);
                if (bitmap != null) {
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                    return bitmap;
                }
            }
            return null;
        }

        /**
         * ��ImageView�����һ��ͼƬ
         *
         * @param bitmap      ����ӵ�ͼƬ
         * @param imageWidth  ͼƬ�Ŀ��
         * @param imageHeight ͼƬ�ĸ߶�
         */
        private void addImage(Bitmap bitmap, int imageWidth, int imageHeight) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth,
                    imageHeight);
            if (thumPic != null) {
                thumPic.setImageBitmap(bitmap);
            } else {
                thumPic = new ImageView(getActivity());
                thumPic.setLayoutParams(params);
                thumPic.setImageBitmap(bitmap);
                thumPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                thumPic.setTag(R.string.image_url, mImageUrl);
            }
        }

        /**
         * ��ͼƬ���ص�SD������������
         *
         * @param imageUrl ͼƬ��URL��ַ��
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
                URL url = new URL(HotCarShow.Transform(imageUrl.replace(" ", "%20")));
                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(5 * 1000);
                con.setReadTimeout(15 * 1000);
                con.setDoInput(true);
                con.setDoOutput(true);
                bis = new BufferedInputStream(con.getInputStream());
                String path = getImagePath(imageUrl);
                imageFile = new File(path);
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
                Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(),
                        picWidth);
                if (bitmap != null) {
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
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

        /**
         * ��ȡͼƬ�ı��ش洢·����
         *
         * @param imageUrl ͼƬ��URL��ַ��
         * @return ͼƬ�ı��ش洢·����
         */
        private String getImagePath(String imageUrl) {
            int lastSlashIndex = imageUrl.lastIndexOf("/");
            String imageTPath = imageUrl.substring(0, lastSlashIndex);
            String extra ="_"+ imageUrl.substring(imageUrl.lastIndexOf("/")+1);
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
