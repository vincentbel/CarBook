package com.Doric.CarBook.car;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class HotCarShow extends Fragment {

    //  ����ViewPager����ʾ��ͼƬ����
    private final Integer PicNum = 5;
    //  ����ͼƬUrl���ַ�������,ViewPager�й̶�ֻ��ʾ5��ͼƬ
    String[] imageUrls = new String[PicNum];
    //  �������϶�Ӧ��url
    String url = Constant.BASE_URL + "/hotcarshow.php";

    //  ����������͵�����
    List<NameValuePair> hotCarRequest = new ArrayList<NameValuePair>();

    //  ������
    ProgressDialog progressDialog;
    // Fragment��view;
    View mView;
    //  �����������ݵ�Json����
    private JSONObject hotCarShow;
    //  ��ͼƬ����Ĺ�����
    private ImageLoader imageLoader;
    //  ��������ͼƬ
    private List<ImageView> imageViews = new ArrayList<ImageView>();
    //  ��¼�����ص�ͼƬ��ȣ������ڴ����
    private int columnWidth;
    //  �������
    private ViewPager viewPager;
    //  ͼƬ����
    private String[] titles;
    //  ͼƬ����İ׵�
    private List<View> dots;
    //  ��ʾ�����TextView
    private TextView tv_title;
    //  ��ǰ��������,��ʼ��Ϊ0
    private int currentItem = 0;
    //  �л���ǰ��ʾ��ͼƬ
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            viewPager.setCurrentItem(currentItem);// �л���ǰ��ʾ��ͼƬ
        }
    };
    // An ExecutorService that can schedule commands to run after a given delay,
    // or to execute periodically.
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hot_car_show, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = getView();
        columnWidth = ((FrameLayout) mView.findViewById(R.id.vp_frame_layout)).getWidth();
        // ��������
        hotCarRequest.add(new BasicNameValuePair("tag", "hotcarshow"));

        // ͨ�����̻߳�ȡJSONObject,����ʼ��Activity
        new GetHotCar().execute();
    }

    /*
     *  ��ʼ��ViewPager
     */
    private void initViewPager() {

        // ���ù���ͼƬ��ͼƬ���
        columnWidth = (mView.findViewById(R.id.vp_frame_layout)).getWidth();
        // �첽��ȡͼƬ��Ϣ�����������ImageView�ļ���
        for (Integer i = 1; i <= 5; i++) {
            GetPicTask task = new GetPicTask();
            task.execute(i - 1);
        }
        // ��ʼ���л��õĵ�
        dots = new ArrayList<View>();
        dots.add(mView.findViewById(R.id.v_dot0));
        dots.add(mView.findViewById(R.id.v_dot1));
        dots.add(mView.findViewById(R.id.v_dot2));
        dots.add(mView.findViewById(R.id.v_dot3));
        dots.add(mView.findViewById(R.id.v_dot4));
        dots.add(mView.findViewById(R.id.v_dot5));
        // ��ʼ�����ֱ���
        tv_title = (TextView) mView.findViewById(R.id.tv_title);
        tv_title.setText(titles[0]);

        viewPager = (ViewPager) mView.findViewById(R.id.vp);
        viewPager.setAdapter(new MyAdapter());// �������ViewPagerҳ���������
        // ����һ������������ViewPager�е�ҳ��ı�ʱ����
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    /*
     *  ��ʾʱViewPager���й���
     */
    @Override
    public void onStart() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // ��Activity��ʾ������ÿ�������л�һ��ͼƬ��ʾ
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2, TimeUnit.SECONDS);
        super.onStart();
    }

    /*
     *  ����ʾʱViewPagerֹͣ����
     */
    @Override
    public void onStop() {
        // ��Activity���ɼ���ʱ��ֹͣ�л�
        scheduledExecutorService.shutdown();
        super.onStop();
    }

    /*
     *  ��ʼ��ListView
     */
    private void initListView() {
        // ���ù���ͼƬ��ͼƬ���
        columnWidth = (mView.findViewById(R.id.carPicImageView)).getWidth();
        for (Integer i = 1; i <= 10; i++) {
            GetPicTask task = new GetPicTask();
            task.execute(i - 1);
        }
        // �������ų����б�
        ListView hotCarShowList = (ListView) mView.findViewById(R.id.hot_car_show_List);
        ArrayList<Map<String, Object>> list = getData();
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.hot_car_show_list,
                new String[]{"carNameTextView", "carPicImageView"},
                new int[]{R.id.carNameTextView, R.id.carPicImageView});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                // �ж�Ҫ����Ķ���
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView iv = (ImageView) view;
                    iv.setImageBitmap((Bitmap) data);
                    return true;
                } else
                    return false;
            }
        });
        if (hotCarShowList != null) {
            hotCarShowList.setAdapter(adapter);
            setListViewHeightBasedOnChildren(hotCarShowList);
        }

    }

    /*
     *  ��JSONObject�л�ȡ��Ϣ
     */
    private ArrayList<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        // ����ʮ��ͼƬ
        for (Integer i = 1; i <= 10; i++) {
            GetPicTask task = new GetPicTask();
            task.execute(i - 1);
        }

        try {
            for (Integer i = 1; i <= 10; i++) {
                map = new HashMap<String, Object>();
                map.put("carNameTextView", hotCarShow.getString("car_brand_" + i) + " " +
                        hotCarShow.getString("car_series_" + i) + " " +
                        hotCarShow.getString("car_model_number_" + i));
                map.put("carPicImageView", imageViews.get(i - 1));
                list.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*
     *  ����ListView�߶ȣ��Ա�ScrollView���й���
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

    /*
     *  �첽������Ϣ�Ļ�ȡ
     */
    private class GetHotCar extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            //����ʱ����
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("������..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //���������������
            JSONParser jsonParser = new JSONParser();
            hotCarShow = jsonParser.getJSONFromUrl(url, hotCarRequest);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            // �����⵽��ȡ��Json�����͹����Ϸ�ViewPager���·���ListView
            if (hotCarShow != null) {
                // ͨ��Json������ʼ��ImageUrls
                for (Integer i = 1; i <= 10; i++) {
                    try {
                        imageUrls[i - 1] = Constant.BASE_URL + "/" + hotCarShow.getString("pictures_url_" + i.toString());
                        if (i <= 5)
                            titles[i - 1] = hotCarShow.getString("title_" + i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                initListView();
                initViewPager();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "�޷��������磬���������ֻ���������", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
     *  �����л�����
     *
     */
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (viewPager) {
                System.out.println("currentItem: " + currentItem);
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget(); // ͨ��Handler�л�ͼƬ
            }
        }

    }

    /*
     *  ��ViewPager��ҳ���״̬�����ı�ʱ����
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
            tv_title.setText(titles[position]);
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    /*
     *  ���ViewPagerҳ���������
     *
     *  ���ֺ������汾�������պ��޸�
     *
     */
    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return PicNum;
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(imageViews.get(arg1));
            return imageViews.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }
    }

    /*
     *  �첽����ͼƬ�Ĺ�����
     */
    class GetPicTask extends AsyncTask<Integer, Void, Bitmap> {

        private String mImageUrl;   // ͼƬ��url��ַ
        private ImageView mImageView; // ������imageViewList���ͼƬ�Ŀ��ظ�ʹ�õ�ImageView


        public GetPicTask() {
        }

        /**
         * �����ظ�ʹ�õ�ImageView����
         *
         * @param imageView
         */
        public GetPicTask(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            mImageUrl = imageUrls[params[0]];
            Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);
            if (imageBitmap == null) {
                imageBitmap = loadImage(mImageUrl);
            }
            return imageBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                double ratio = bitmap.getWidth() / (columnWidth * 1.0);
                int scaledHeight = (int) (bitmap.getHeight() / ratio);
                addImage(bitmap, columnWidth, scaledHeight);
            }
            //taskCollection.remove(this);
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
            if (mImageView != null) {
                mImageView.setImageBitmap(bitmap);
            } else {
                ImageView imageView = new ImageView(getActivity());
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setTag(R.string.image_url, mImageUrl);
                /*
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                */
                imageViews.add(imageView);
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
                        columnWidth);
                if (bitmap != null) {
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                    return bitmap;
                }
            }
            return null;
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
                URL url = new URL(imageUrl);
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
                Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(),
                        columnWidth);
                if (bitmap != null) {
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                }
            }
        }

        /**
         * ��ȡͼƬ�ı��ش洢·����
         *
         * @param imageUrl ͼƬ��URL��ַ��
         * @return ͼƬ�ı��ش洢·����
         */
        private String getImagePath(String imageUrl) {
            int lastSlashIndex = imageUrl.lastIndexOf("/");
            String imageName = imageUrl.substring(lastSlashIndex + 1);
            String imageDir = Environment.getExternalStorageDirectory().getPath()
                    + "/CarBook/Cache/";
            File file = new File(imageDir);
            if (!file.exists()) {
                file.mkdirs();
            }
            String imagePath = imageDir + imageName;
            return imagePath;
        }
    }
}
