package com.Doric.CarBook.search;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.R;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AlphaShowData{
    /**
     * 当前被选中的车辆品牌
     */
    public static CarSeable carseable;
    /**
     * 装载所有的listView
     */
    public static LinearLayout mLinearLayout;
    /**
     * 滚动轴
     */
    public  static ScrollView mScrollView;

    public static RelativeLayout mRelativeLayout;
    /**
     * ListView的集合。用于读取图片时获取每一个listView的Adapter
     */
    public static ArrayList<Pair<String, MyListView>> listarray;

    /**
     * 字母位置Map
     */
    private static Map<String,Integer> alpha_index = null;

    public static boolean isload = false;


    private static Context mContext;

    private static Toast toast ;
    /**
     * 初始化视图
     * @param context
     * @param res
     */

    public  static void initPage(Context context,Resources res) {
        mContext = context;

        mRelativeLayout = new RelativeLayout(mContext);
        listarray = new ArrayList<Pair<String, MyListView>>();
        mLinearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams param1 = new LinearLayout.LayoutParams(LinearLayout.
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mLinearLayout.setLayoutParams(param1);
        mLinearLayout.setOrientation(LinearLayout.VERTICAL);
        mLinearLayout.setBackgroundColor(Color.rgb(255, 255, 255));

        ArrayList<Pair<String, ArrayList<CarSeable>>> al = PinYinIndex.getIndex_CarSeable(CarSeableData.mCarSeable);
        mScrollView = new ScrollView(mContext);
        mScrollView.setEnabled(true);
        mScrollView.setBackgroundColor(Color.alpha(0));
        ScrollView.LayoutParams param2 = new ScrollView.LayoutParams(ScrollView.
                LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.MATCH_PARENT);
        mScrollView.setLayoutParams(param2);


        for (Pair<String, ArrayList<CarSeable>> pair : al) {
            TextView text = new TextView(mContext);
            text.setText("  "+ pair.first);
            text.setTextColor(Color.rgb(100, 100, 100));


            text.setBackgroundColor(Color.rgb(240,240,240));
            text.setTextSize(20);
            mLinearLayout.addView(text);
            MyListView listview = new MyListView(mContext);

            SimpleAdapter adapter = new SimpleAdapter(mContext, getUniformData(pair.second), R.layout.sea_list_layout,
                    new String[]{"title", "img"},
                    new int[]{R.id.title, R.id.img});
            adapter.setViewBinder(new ListViewBinder());
            listview.setDivider(res.getDrawable(R.drawable.list_divider));
            listview.setDividerHeight(1);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    ListView lv = (ListView) parent;

                    HashMap<String, Object> Info = (HashMap<String, Object>) lv.getItemAtPosition(position);//SimpleAdapter·???Map

                    carseable = CarSeableData.find((String) Info.get("title"));

                    carseable.LoadCarSeries();


                }

            });
            listarray.add(new Pair<String, MyListView>(pair.first, listview));
            mLinearLayout.addView(listview);
        }

        new AlphaShowData.GetPicData().start();

        mScrollView.addView(mLinearLayout);
        mRelativeLayout.addView(mScrollView);
        LinearLayout otherlinearlayout= new LinearLayout(mContext);
        RelativeLayout.LayoutParams param3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        param3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        param3.setMargins(0,0,30,0);
        param3.addRule(RelativeLayout.CENTER_VERTICAL);
        otherlinearlayout.setOrientation(LinearLayout.VERTICAL);

        otherlinearlayout.setBackgroundColor(Color.alpha(0));
        for(Pair<String,MyListView> pair : listarray){
            TextView text = new TextView(mContext);
            text.setText( pair.first);
            text.setClickable(true);
            text.setTextColor(Color.rgb(100,100 , 100));
            text.setBackgroundColor(Color.alpha(0));
            ViewGroup.LayoutParams param4  = new AbsListView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT );
            text.setLayoutParams(param4);
            text.setTextSize(18);
            otherlinearlayout.addView(text);
            text.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(alpha_index==null){
                        CreateIndex(mLinearLayout);
                    }
                    mScrollView.scrollTo(alpha_index.get(((TextView)v).getText()),alpha_index.get(((TextView)v).getText()));
                    mScrollView.smoothScrollTo(alpha_index.get(((TextView)v).getText()),alpha_index.get(((TextView)v).getText()));
                    showToast(((TextView)v).getText().toString());
                    Message delayMsg = m_Handler.obtainMessage(AIRPLAY_MESSAGE_HIDE_TOAST);
                    m_Handler.sendMessageDelayed(delayMsg, 1000);
                    return true;
                }
            });
        }
        mRelativeLayout.addView(otherlinearlayout,param3);
        System.out.println("ViewDone");
        isload = true;

    }

    final static int AIRPLAY_MESSAGE_HIDE_TOAST = 0x1000;



    public static void showToast(String text) {
        if(toast == null) {
            toast = new Toast(mContext);
            TextView textView = new TextView(mContext);
            textView.setText(text);
            textView.setGravity(Gravity.CENTER);
            textView.setWidth(160);
            textView.setHeight(160);
            textView.setTextSize(50);
            textView.setBackgroundColor(Color.rgb(100, 100, 100));
            textView.setTextColor(Color.rgb(240, 240, 240));
            toast.setView(textView);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        } else {
            ((TextView)toast.getView()).setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
        }
    }


    private static  Handler m_Handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case 0:
                    break;
                case AIRPLAY_MESSAGE_HIDE_TOAST: {
                    cancelToast();
                    break;
                }
            }
        }
    };


            private static  void CreateIndex(LinearLayout linearLayout){
        //TextView
        TextView textView = (TextView)linearLayout.getChildAt(0);
        int height = textView.getHeight();
        alpha_index = new HashMap<String, Integer>();
        int size = listarray.size();
        int tmp = 0;
        for(int i=0;i<size;i++){
                alpha_index.put(listarray.get(i).first,tmp );
                tmp+= height + listarray.get(i).second.getHeight();

        }
    }

    /**
     * 视图绑定器
     * 使SimpleAdapter可以绑定Bitmap对象到ImageView
     */
    private static class ListViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {
            // TODO Auto-generated method stub
            if((view instanceof ImageView) && (data instanceof Bitmap)) {
                ImageView imageView = (ImageView) view;
                Bitmap bmp = (Bitmap) data;
                imageView.setImageBitmap(bmp);
                return true;
            }
            return false;
        }


    }

    /**
     * 把车辆品牌数据装配成适合ListView绑定的形式
     * @param al_cs
     * @return
     */
    public static ArrayList<Map<String, Object>> getUniformData(ArrayList<CarSeable> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (CarSeable cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("title", cs.getCarSeableName());
            map.put("img", R.drawable.ic_launcher);
            list.add(map);

        }

        return list;

    }

    /**
     * 加载图片的Handler
     */
    final static Handler cwjHandler = new Handler();
    private static class UpdateRunnable implements  Runnable{
        SimpleAdapter simpleAdapter = null;
        public UpdateRunnable(SimpleAdapter sa){
            simpleAdapter = sa;
        }
        public void run() {
            simpleAdapter.notifyDataSetChanged();
        }
    };


    /**
     * 加载图片线程
     */
    public  static class GetPicData extends Thread {


        public GetPicData() {

        }

        public void run() {
            for (Pair<String, MyListView> pair : AlphaShowData.listarray) {

                HttpURLConnection con = null;
                FileOutputStream fos = null;
                BufferedOutputStream bos = null;
                BufferedInputStream bis = null;
                File imageFile = null;
                SimpleAdapter simpleAdapter = (SimpleAdapter) pair.second.getAdapter();
                for (int i = 0; i < simpleAdapter.getCount(); i++) {
                    Map<String, Object> map = (Map<String, Object>) simpleAdapter.getItem(i);
                    Bitmap bitmap =null;
                    String imageUrl=CarSeableData.find((String) map.get("title")).getPicPath();
                    imageFile = new File(getImagePath(imageUrl));
                    try {
                        if (!imageFile.exists()) {
                            URL url = new URL(GBK2UTF.Transform(imageUrl.replace(" ","%20")));
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
                            map.put("img", bitmap);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                cwjHandler.post(new UpdateRunnable(simpleAdapter));

            }
        }

        /**
         *获取SD卡路径
         * @return
         */
        private String getSDPath(){
            File sdDir = null;
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED);   //????sd?¨??·?????
            if   (sdCardExist)
            {
                sdDir = Environment.getExternalStorageDirectory();//?????ú????
            }
            return sdDir.toString();

        }

        /**
         * 通过图片的Url得到图片的唯一本地路径
         * @param imageUrl
         * @return
         */
        private String getImagePath(String imageUrl) {
            int lastSlashIndex = imageUrl.lastIndexOf("/");
            String imageTPath = imageUrl.substring(0,lastSlashIndex );
            String extra = imageUrl.substring(imageUrl.lastIndexOf("."));
            lastSlashIndex = imageTPath.lastIndexOf("/");
            String imageName = imageTPath.substring(lastSlashIndex+1);
            imageName += extra;

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
}