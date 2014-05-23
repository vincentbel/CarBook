package com.Doric.CarBook.car;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义的ScrollView，在其中动态地对图片进行添加。
 */
public class CarPicScrollView extends ScrollView implements OnTouchListener {

    /**
     * 每页要加载的图片数量
     */
    public static final int PAGE_SIZE = 15;
    static JSONObject carPic;
    /**
     * 记录所有正在下载或等待下载的任务。
     */
    private static Set<LoadImageTask> taskCollection;
    /**
     * MyScrollView下的直接子布局。
     */
    private static View scrollLayout;
    /**
     * MyScrollView布局的高度。
     */
    private static int scrollViewHeight;
    /**
     * 记录上垂直方向的滚动距离。
     */
    private static int lastScrollY = -1;
    private static Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            CarPicScrollView carPicScrollView = (CarPicScrollView) msg.obj;
            int scrollY = carPicScrollView.getScrollY();
            // 如果当前的滚动位置和上次相同，表示已停止滚动
            if (scrollY == lastScrollY) {
                // 当滚动的最底部，并且当前没有正在下载的任务时，开始加载下一页的图片
                if (scrollViewHeight + scrollY >= scrollLayout.getHeight()
                        && taskCollection.isEmpty()) {
                    carPicScrollView.loadMoreImages();
                }
                carPicScrollView.checkVisibility();
            } else {
                lastScrollY = scrollY;
                Message message = new Message();
                message.obj = carPicScrollView;
                // 5毫秒后再次对滚动位置进行判断
                handler.sendMessageDelayed(message, 5);
            }
        }

        ;

    };
    /**
     * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。
     */

    ProgressDialog progressDialog;
    String picUrl = Constant.BASE_URL + "/showcar.php";
    List<NameValuePair> carPicParams = new ArrayList<NameValuePair>();
    /**
     * 记录当前已加载到第几页
     */
    private int page;
    /**
     * 每一列的宽度
     */
    private int columnWidth;
    /**
     * 当前第一列的高度
     */
    private int firstColumnHeight;
    /**
     * 当前第二列的高度
     */
    private int secondColumnHeight;
    /**
     * 当前第三列的高度
     */
    private int thirdColumnHeight;
    /**
     * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
     */
    private boolean loadOnce;
    /**
     * 对图片进行管理的工具类
     */
    private ImageLoader imageLoader;
    /**
     * 第一列的布局
     */
    private LinearLayout firstColumn;
    /**
     * 第二列的布局
     */
    private LinearLayout secondColumn;
    /**
     * 第三列的布局
     */
    private LinearLayout thirdColumn;
    /**
     * 记录所有界面上的图片，用以可以随时控制对图片的释放。
     */
    private List<ImageView> imageViewList = new ArrayList<ImageView>();

    /**
     * MyScrollView的构造函数。
     *
     * @param context
     * @param attrs
     */
    public CarPicScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        imageLoader = ImageLoader.getInstance();
        taskCollection = new HashSet<LoadImageTask>();
        setOnTouchListener(this);
    }

    /**
     * 进行一些关键性的初始化操作，获取MyScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce) {
            scrollViewHeight = getHeight();
            scrollLayout = getChildAt(0);
            firstColumn = (LinearLayout) findViewById(R.id.first_column);
            secondColumn = (LinearLayout) findViewById(R.id.second_column);
            thirdColumn = (LinearLayout) findViewById(R.id.third_column);
            columnWidth = firstColumn.getWidth();
            loadOnce = true;

            carPicParams.add(new BasicNameValuePair("tag", "show_pictures"));
            carPicParams.add(new BasicNameValuePair("brand", "BMW"));
            carPicParams.add(new BasicNameValuePair("series", "7series"));
            carPicParams.add(new BasicNameValuePair("model_number", "2013 740Li grand"));
            new GetCarInfo().execute();
        }
    }

    /**
     * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Message message = new Message();
            message.obj = this;
            handler.sendMessageDelayed(message, 5);
        }
        return false;
    }

    /**
     * 开始加载下一页的图片，每张图片都会开启一个异步线程去下载。
     */
    public void loadMoreImages() {
        if (hasSDCard()) {
            int startIndex = page * PAGE_SIZE;
            int endIndex = page * PAGE_SIZE + PAGE_SIZE;
            if (startIndex < CarImages.imageUrls.length) {
                Toast.makeText(getContext(), "正在加载...", Toast.LENGTH_SHORT).show();
                if (endIndex > CarImages.imageUrls.length) {
                    endIndex = CarImages.imageUrls.length;
                }
                for (int i = startIndex; i < endIndex; i++) {
                    LoadImageTask task = new LoadImageTask();
                    taskCollection.add(task);
                    task.execute(i);
                }
                page++;
            } else {
                Toast.makeText(getContext(), "已没有更多图片", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "未发现SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 遍历imageViewList中的每张图片，对图片的可见性进行检查，如果图片已经离开屏幕可见范围，则将图片替换成一张空图。
     */
    public void checkVisibility() {
        for (int i = 0; i < imageViewList.size(); i++) {
            ImageView imageView = imageViewList.get(i);
            int borderTop = (Integer) imageView.getTag(R.string.border_top);
            int borderBottom = (Integer) imageView.getTag(R.string.border_bottom);
            if (borderBottom > getScrollY() && borderTop < getScrollY() + scrollViewHeight) {
                String imageUrl = (String) imageView.getTag(R.string.image_url);
                Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(imageUrl);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    LoadImageTask task = new LoadImageTask(imageView);
                    task.execute(i);
                }
            } else {
                imageView.setImageResource(R.drawable.empty_photo);
            }
        }
    }

    /**
     * 判断手机是否有SD卡。
     *
     * @return 有SD卡返回true，没有返回false。
     */
    private boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    private class GetCarInfo extends AsyncTask<Void, Void, Void> {

        protected void onPreExecute() {
            super.onPreExecute();
            //弹出"正在登录"框
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("加载中..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求
            JSONParser jsonParser = new JSONParser();
            System.out.println("获取carPicJson");
            carPic = jsonParser.getJSONFromUrl(picUrl, carPicParams);
            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (carPic != null) {
                try {
                    CarImages.initImages(carPic.getInt("pictures_num"), carPic);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getContext(), "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
            }
            loadMoreImages();
        }
    }

    /**
     * 异步下载图片的任务。
     */
    class LoadImageTask extends AsyncTask<Integer, Void, Bitmap> {

        /**
         * 记录每个图片对应的位置
         */
        private int mItemPosition;

        /**
         * 图片的URL地址
         */
        private String mImageUrl;

        /**
         * 可重复使用的ImageView
         */
        private ImageView mImageView;

        public LoadImageTask() {
        }

        /**
         * 将可重复使用的ImageView传入
         *
         * @param imageView
         */
        public LoadImageTask(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            System.out.println("后台加载图片");
            mItemPosition = params[0];
            mImageUrl = CarImages.imageUrls[mItemPosition];
            Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);
            if (imageBitmap == null) {
                System.out.println("缓存中不存在位图，需要加载图片");
                imageBitmap = loadImage(mImageUrl);
            }else {
                System.out.println("缓存中存在位图，不需要加载图片");
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
            taskCollection.remove(this);
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
                System.out.println("添加图片");
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ScaleType.FIT_XY);
                imageView.setPadding(5, 5, 5, 5);
                imageView.setTag(R.string.image_url, mImageUrl);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ImageDetailsActivity.class);
                        intent.putExtra("image_position", mItemPosition);
                        getContext().startActivity(intent);
                    }
                });
                findColumnToAdd(imageView, imageHeight).addView(imageView);
                System.out.println("向imageViewList中添加imageView");
                imageViewList.add(imageView);
            }
        }

        /**
         * 找到此时应该添加图片的一列。原则就是对三列的高度进行判断，当前高度最小的一列就是应该添加的一列。
         *
         * @param imageView
         * @param imageHeight
         * @return 应该添加图片的一列
         */
        private LinearLayout findColumnToAdd(ImageView imageView, int imageHeight) {
            if (firstColumnHeight <= secondColumnHeight) {
                if (firstColumnHeight <= thirdColumnHeight) {
                    imageView.setTag(R.string.border_top, firstColumnHeight);
                    firstColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, firstColumnHeight);
                    return firstColumn;
                }
                imageView.setTag(R.string.border_top, thirdColumnHeight);
                thirdColumnHeight += imageHeight;
                imageView.setTag(R.string.border_bottom, thirdColumnHeight);
                return thirdColumn;
            } else {
                if (secondColumnHeight <= thirdColumnHeight) {
                    imageView.setTag(R.string.border_top, secondColumnHeight);
                    secondColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, secondColumnHeight);
                    return secondColumn;
                }
                imageView.setTag(R.string.border_top, thirdColumnHeight);
                thirdColumnHeight += imageHeight;
                imageView.setTag(R.string.border_bottom, thirdColumnHeight);
                return thirdColumn;
            }
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
