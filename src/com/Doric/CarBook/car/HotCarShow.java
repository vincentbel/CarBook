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
import com.Doric.CarBook.MainActivity;
import com.Doric.CarBook.R;
import com.Doric.CarBook.Static;
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

    //  滚动ViewPager中显示的图片数量
    private final Integer PicNum = 5;
    //  储存图片Url的字符串数组,ViewPager中固定只显示5张图片
    String[] imageUrls = new String[PicNum];
    //  服务器上对应的url
    String url = Static.BASE_URL + "/hotcarshow.php";

    //  向服务器发送的请求
    List<NameValuePair> hotCarRequest = new ArrayList<NameValuePair>();

    //  进度条
    ProgressDialog progressDialog;
    // Fragment的view;
    View mView;
    //  用来接收数据的Json对象
    private JSONObject hotCarShow;
    //  对图片管理的工具类
    private ImageLoader imageLoader;
    //  储存所有图片
    private List<ImageView> imageViews = new ArrayList<ImageView>();
    //  记录所加载的图片宽度，避免内存溢出
    private int columnWidth;
    //  滑动组件
    private ViewPager viewPager;
    //  图片标题
    private String[] titles;
    //  图片标题的白点
    private List<View> dots;
    //  显示标题的TextView
    private TextView tv_title;
    //  当前的索引号,初始化为0
    private int currentItem = 0;
    //  切换当前显示的图片
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
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
        // 构建请求
        hotCarRequest.add(new BasicNameValuePair("tag", "hotcarshow"));

        // 通过新线程获取JSONObject,并初始化Activity
        new GetHotCar().execute();
    }

    /*
     *  初始化ViewPager
     */
    private void initViewPager() {

        // 设置滚动图片的图片宽度
        columnWidth = (mView.findViewById(R.id.vp_frame_layout)).getWidth();
        // 异步获取图片信息，并构建完成ImageView的集合
        for (Integer i = 1; i <= 5; i++) {
            GetPicTask task = new GetPicTask();
            task.execute(i - 1);
        }
        // 初始化切换用的点
        dots = new ArrayList<View>();
        dots.add(mView.findViewById(R.id.v_dot0));
        dots.add(mView.findViewById(R.id.v_dot1));
        dots.add(mView.findViewById(R.id.v_dot2));
        dots.add(mView.findViewById(R.id.v_dot3));
        dots.add(mView.findViewById(R.id.v_dot4));
        dots.add(mView.findViewById(R.id.v_dot5));
        // 初始化文字标题
        tv_title = (TextView) mView.findViewById(R.id.tv_title);
        tv_title.setText(titles[0]);

        viewPager = (ViewPager) mView.findViewById(R.id.vp);
        viewPager.setAdapter(new MyAdapter());// 设置填充ViewPager页面的适配器
        // 设置一个监听器，当ViewPager中的页面改变时调用
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    /*
     *  显示时ViewPager进行滚动
     */
    @Override
    public void onStart() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // 当Activity显示出来后，每两秒钟切换一次图片显示
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2, TimeUnit.SECONDS);
        super.onStart();
    }

    /*
     *  不显示时ViewPager停止滚动
     */
    @Override
    public void onStop() {
        // 当Activity不可见的时候停止切换
        scheduledExecutorService.shutdown();
        super.onStop();
    }

    /*
     *  初始化ListView
     */
    private void initListView() {
        // 设置滚动图片的图片宽度
        columnWidth = (mView.findViewById(R.id.carPicImageView)).getWidth();
        for (Integer i = 1; i <= 10; i++) {
            GetPicTask task = new GetPicTask();
            task.execute(i - 1);
        }
        // 创建热门车辆列表
        ListView hotCarShowList = (ListView) mView.findViewById(R.id.hot_car_show_List);
        ArrayList<Map<String, Object>> list = getData();
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.hot_car_show_list,
                new String[]{"carNameTextView", "carPicImageView"},
                new int[]{R.id.carNameTextView, R.id.carPicImageView});
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                // 判断要处理的对象
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
     *  从JSONObject中获取信息
     */
    private ArrayList<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        // 下载十张图片
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
     *  计算ListView高度，以便ScrollView进行滚动
     */
    private void setListViewHeightBasedOnChildren(ListView listView) {
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
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        }
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /*
     *  异步进行信息的获取
     */
    private class GetHotCar extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            //加载时弹出
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("加载中..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            hotCarShow = jsonParser.getJSONFromUrl(url, hotCarRequest);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            // 如果检测到获取了Json包，就构建上方ViewPager和下方的ListView
            if (hotCarShow != null) {
                // 通过Json包，初始化ImageUrls
                for (Integer i = 1; i <= 10; i++) {
                    try {
                        imageUrls[i - 1] = Static.BASE_URL + "/" + hotCarShow.getString("pictures_url_" + i.toString());
                        if (i <= 5)
                            titles[i - 1] = hotCarShow.getString("title_" + i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                initListView();
                initViewPager();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
            }
        }
    }

    /*
     *  换行切换任务
     *
     */
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (viewPager) {
                System.out.println("currentItem: " + currentItem);
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget(); // 通过Handler切换图片
            }
        }

    }

    /*
     *  当ViewPager中页面的状态发生改变时调用
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
     *  填充ViewPager页面的适配器
     *
     *  部分函数被版本抛弃，日后修改
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
     *  异步下载图片的工具类
     */
    class GetPicTask extends AsyncTask<Integer, Void, Bitmap> {

        private String mImageUrl;   // 图片的url地址
        private ImageView mImageView; // 用来向imageViewList添加图片的可重复使用的ImageView


        public GetPicTask() {
        }

        /**
         * 将可重复使用的ImageView传入
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
         * 向ImageView中添加一张图片
         *
         * @param bitmap      待添加的图片
         * @param imageWidth  图片的宽度
         * @param imageHeight 图片的高度
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
         * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
         *
         * @param imageUrl 图片的URL地址
         * @return 加载到内存的图片。
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
         * 将图片下载到SD卡缓存起来。
         *
         * @param imageUrl 图片的URL地址。
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
         * 获取图片的本地存储路径。
         *
         * @param imageUrl 图片的URL地址。
         * @return 图片的本地存储路径。
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
