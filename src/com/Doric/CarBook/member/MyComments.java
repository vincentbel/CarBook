package com.Doric.CarBook.member;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;
import com.Doric.CarBook.car.ImageLoader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MyComments extends Activity {

    UserFunctions userFunctions;
    ProgressDialog progressDialog;
    JSONObject commentsData;
    JSONObject comments;
    ListView commentList;
    int columnWidth = 300;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<ImageView> imageViews = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userFunctions = new UserFunctions(getApplicationContext());
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_launcher);
        imageViews.add(imageView);
        imageViews.add(imageView);

        setContentView(R.layout.my_comments);
        commentList = (ListView) findViewById(R.id.my_comments_list);
        new GetMyComments().execute();
    }

    private class GetMyComments extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... voids) {
            commentsData = userFunctions.getMyComments();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                comments = commentsData.getJSONObject("comments");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new GetPicData().execute();
            commentList.setAdapter(new CommentsAdapter(MyComments.this, comments));
        }
    }

    private class GetPicData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
                String mImageUrl = null;
                // 获取对应车辆的图片URL
            for (Integer i = 0; i < comments.length(); i++) {
                try {
                    mImageUrl = Constant.BASE_URL + "/" + comments.getJSONObject(i + "").getString("pictures_url");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // 从缓存中加载
                Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mImageUrl);

                if (imageBitmap == null) {
                    System.out.println("缓存中不存在位图，需要下载或从sd卡加载图片");
                    // 缓存中不存在位图，需要下载或从sd卡加载图片
                    imageBitmap = loadImage(mImageUrl);
                } else {
                    System.out.println("缓存中存在位图，不需要加载图片");
                }
                // 为ViewPager添加图片

                imageViews.get(i).setImageBitmap(imageBitmap);
                imageViews.get(i).setScaleType(ImageView.ScaleType.CENTER_CROP);
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
                        300);
                if (bitmap != null) {
                    // 成功获取图片，将图片加入缓存
                    System.out.println("成功获取图片，将图片加入缓存");
                    imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
                    return bitmap;
                }
            }
            return null;
        }

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

        public String Transform(String str){
            byte[] b = str.getBytes();
            char[] c = new char[b.length];
            for (int i=0;i<b.length;i++){
                if(b[i]!=' ')
                    c[i] = (char)(b[i]&0x00FF);

            }
            return new String(c);
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

    private class CommentsAdapter extends ArrayAdapter<String> {

        private Context context;
        private JSONObject values;

        public CommentsAdapter(Context context, JSONObject values) {
            super(context, R.layout.my_comments_item);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.my_comments_item, parent, false);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.carNameTextView = (TextView) rowView.findViewById(R.id.my_comments_car_name);
                viewHolder.imageView = (ImageView) rowView.findViewById(R.id.my_comments_list_image);
                rowView.setTag(viewHolder);
            }
            String carName = null;
            String comment = null;
            try {
                carName = values.getJSONObject(position + "").getString("carname");
                comment = values.getJSONObject(position + "").getString("short_comments");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            int commentMaxLength = 50;
            if (comment.length() > commentMaxLength) {
                comment = comment.substring(0, commentMaxLength) + "...";
            }
            System.out.println("comment:" + comment);
            ViewHolder viewHolder = (ViewHolder) rowView.getTag();
            viewHolder.carNameTextView.setText(carName);
            viewHolder.commentsTextView.setText(comment);
            viewHolder.imageView.setImageBitmap(imageViews.get(position).getDrawingCache());
            return rowView;
        }

    }

    static class ViewHolder {
        TextView carNameTextView;
        TextView commentsTextView;
        ImageView imageView;
    }
}
