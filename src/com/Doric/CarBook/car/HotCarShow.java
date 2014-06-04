package com.Doric.CarBook.car;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
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
    //  热门车列表
    ListView hotCarShowList = null;
    //  滚动ViewPager中显示的图片数量
    private final Integer PicNum = 5;
    //  服务器上对应的url
    String url = Constant.BASE_URL + "/favour.php";
    // 图片管理工具类
    private ImageLoader imageLoader = ImageLoader.getInstance();
    //  向服务器发送的请求
    List<NameValuePair> hotCarRequest = new ArrayList<NameValuePair>();
    // 判断线程是否结束
    boolean threadTag = false;
    //  进度条
    ProgressDialog progressDialog;
    // Fragment的view;
    View mView;
    //  用来接收数据的Json对象
    private JSONObject hotCarShow;
    //  储存所有图片
    private List<ImageView> imageViews = new ArrayList<ImageView>();
    //  用于添加imageViews的可复用的imageView
    private ImageView imageView = null;
    //  记录所加载的图片宽度，避免内存溢出
    private final int columnWidth = 480 ;
    //  滑动组件
    private ViewPager viewPager;
    //  图片标题
    private String[] titles = new String[PicNum];
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
        /*columnWidth = ((FrameLayout) mView.findViewById(R.id.vp_frame_layout)).getWidth();*/
        // 构建请求
        hotCarRequest.add(new BasicNameValuePair("tag", "favour"));
        // 初始化imags
        for (int i=0;i<5;i++){
            imageView = new ImageView(getActivity());
            imageView.setImageResource(R.drawable.ic_launcher);
            imageViews.add(imageView);
        }
        Log.d("onActivityCreated","初始化imageViews");
        // 通过新线程获取JSONObject,并初始化Activity
        new GetHotCar().execute();
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
            Log.d("doInBackground","获取Json包");
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
                Log.d("onPostExecute","构建titles");
                // 通过Json包，初始化ImageUrls
                for (Integer i = 1; i <= 5; i++) {
                    try {
                            JSONObject car = hotCarShow.getJSONObject("car_"+i.toString());
                            titles[i - 1] = car.getString("brand_series")+" "+
                                            car.getString("model_number");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                initListView();
                new GetPicData().execute();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
            }
        }
    }
    /*
     *  初始化ListView
     */
    private void initListView() {
        Log.d("initListView","初始化listView");
        // 设置滚动图片的图片宽度
        /*columnWidth = 150;*/
//        for (Integer i = 1; i <= 10; i++) {
//            GetPicTask task = new GetPicTask();
//            task.execute(i - 1);
//        }
        // 创建热门车辆列表
        hotCarShowList = (ListView) mView.findViewById(R.id.hot_car_show_List);
        ArrayList<Map<String, Object>> list = getData();
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.hot_car_show_list,
                new String[]{"car_id","carBrandTextView","carModelNumberTextView", "carPicImageView"},
                new int[]{R.id.car_id,R.id.carBrandTextView, R.id.carModelNumberTextView,R.id.carPicImageView});
        adapter.setViewBinder(new myViewBinder());
        if (hotCarShowList != null) {
            hotCarShowList.setAdapter(adapter);
            setListViewHeightBasedOnChildren(hotCarShowList);
        }
        hotCarShowList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView tempList = (ListView) parent;
                HashMap<String, String> map = (HashMap<String, String>) tempList.getItemAtPosition(position);
                String car_id = map.get("car_id");
                String brand_series = map.get("carBrandTextView");
                String model_number = map.get("carModelNumberTextView");

                Bundle bundle = new Bundle();
                bundle.putString("car_id",car_id);
                bundle.putString("series",brand_series);
                bundle.putString("model_number",model_number);

                Intent intent = new Intent(getActivity(),CarShow.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

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
    /*
     *  从JSONObject中获取信息
     */
    private ArrayList<Map<String, Object>> getData() {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();

        try {
            for (Integer i = 1; i <= 10; i++) {
                JSONObject car = hotCarShow.getJSONObject("car_"+i.toString());
                map = new HashMap<String, Object>();
                map.put("car_id",car.getString("car_id"));
                map.put("carBrandTextView", car.getString("brand_series"));
                map.put("carModelNumberTextView",car.getString("model_number"));
                map.put("carPicImageView", R.drawable.ic_launcher);
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
     *  初始化ViewPager
     */
    private void initViewPager() {
        for (Integer i=1;i<=5;i++){
            imageViews.get(i-1).setTag(i);
            imageViews.get(i-1).setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject car = null;
                    try {
                        car = hotCarShow.getJSONObject("car_"+v.getTag());
                        Bundle bundle = new Bundle();
                        bundle.putString("car_id",car.getString("car_id"));
                        bundle.putString("series",car.getString("brand_series"));
                        bundle.putString("model_number",car.getString("model_number"));

                        Intent intent = new Intent(getActivity(),CarShow.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }


        // 设置滚动图片的图片宽度
        /*columnWidth = (mView.findViewById(R.id.vp_frame_layout)).getWidth();*/
        // 异步获取图片信息，并构建完成ImageView的集合
//        for (Integer i = 1; i <= 5; i++) {
//            GetPicTask task = new GetPicTask();
//            task.execute(i - 1);
//        }
        // 初始化切换用的点
        dots = new ArrayList<View>();
        dots.add(mView.findViewById(R.id.v_dot0));
        dots.add(mView.findViewById(R.id.v_dot1));
        dots.add(mView.findViewById(R.id.v_dot2));
        dots.add(mView.findViewById(R.id.v_dot3));
        dots.add(mView.findViewById(R.id.v_dot4));
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
     *  换行切换任务
     *
     */
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (viewPager) {
                //System.out.println("currentItem: " + currentItem);
                currentItem = (currentItem + 1) % imageViews.size();
                Integer i = imageViews.size();
                //Log.d("imageViews.size()",i.toString());
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

    private class GetPicData extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            //加载时弹出
            /*progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("加载中..");
            progressDialog.setCancelable(true);
            progressDialog.show();*/
        }

        /*protected Void doInBackground(Void... params) {
            Log.d("GetPicData", "获取图片");
            //LoadImage i =  new LoadImage(cs.getCarSeableName(),cs.getPicPath());
            HttpURLConnection con = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            BufferedInputStream bis = null;
            File imageFile = null;
            JSONObject car = null;

            SimpleAdapter simpleAdapter = (SimpleAdapter) hotCarShowList.getAdapter();
            for (Integer i = 1; i <= simpleAdapter.getCount(); i++) {
                Log.d("GetPicData", "download" + i.toString());
                Map<String, Object> map = (Map<String, Object>) simpleAdapter.getItem(i - 1);
                Bitmap bitmap = null;
                String imageUrl = null;
                try {
                    car = hotCarShow.getJSONObject("car_" + i.toString());
                    imageUrl = Constant.BASE_URL + "/" + car.getString("pictures_url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                imageFile = new File(getImagePath(imageUrl));
                try {
                    if (!imageFile.exists()) {
                        URL url = new URL(Transform(imageUrl.replace(" ", "%20")));
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

                    } else {
                        bitmap = BitmapFactory.decodeFile(getImagePath(imageUrl));
                    }
                    if (bitmap != null) {

                        map.put("carPicImageView", bitmap);
                        cwjHandler.post(new UpdateRunnable(simpleAdapter));
                        if (i <= 5) {
                            imageViews.get(i - 1).setImageBitmap(bitmap);
                            imageViews.get(i - 1).setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return null;
        }
*/
        @Override
        protected Void doInBackground(Void... params) {
            JSONObject car = null;
            SimpleAdapter simpleAdapter = (SimpleAdapter) hotCarShowList.getAdapter();

            for (Integer i = 1; i <= simpleAdapter.getCount(); i++) {
                Log.d("GetPicData", "download" + i.toString());
                Map<String, Object> map = (Map<String, Object>) simpleAdapter.getItem(i - 1);
                String mImageUrl = null;
                try {
                    car = hotCarShow.getJSONObject("car_" + i.toString());
                    mImageUrl = Constant.BASE_URL + "/" + car.getString("pictures_url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);
                if (imageBitmap == null) {
                    System.out.println("缓存中不存在位图，需要加载图片");
                    imageBitmap = loadImage(mImageUrl);
                }else {
                    System.out.println("缓存中存在位图，不需要加载图片");
                }
                map.put("carPicImageView", imageBitmap);
                cwjHandler.post(new UpdateRunnable(simpleAdapter));
                if (i<=5){
                    imageViews.get(i - 1).setImageBitmap(imageBitmap);
                    imageViews.get(i - 1).setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
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
                downloadImage(imageUrl);
            } else {
                System.out.println("sd卡中存在");
            }
            if (imageUrl != null) {
                System.out.println("从服务器下载");
                Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(),
                        columnWidth);
                if (bitmap != null) {
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
                URL url = new URL(Transform(imageUrl));
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
            initViewPager();
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            // 当Activity显示出来后，每两秒钟切换一次图片显示
            scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1, 2, TimeUnit.SECONDS);
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
    /*private String getImagePath(String imageUrl) {
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
    }*/
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
