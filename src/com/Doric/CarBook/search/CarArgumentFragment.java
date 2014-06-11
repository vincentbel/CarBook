

package com.Doric.CarBook.search;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.inputmethodservice.Keyboard;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.*;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.utility.JSONParser;
import com.tonicartos.widget.stickygridheaders.*;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView.OnHeaderClickListener;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView.OnHeaderLongClickListener;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import com.Doric.CarBook.R;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * 车辆对比Fragment
 * 使用并修改了开源项目StickGridHeaders
 */
public class CarArgumentFragment extends Fragment implements OnItemClickListener,
        OnHeaderClickListener, OnHeaderLongClickListener {
    private static final String KEY_LIST_POSITION = "key_list_position";



    private static ArrayList<HeaderGridData> list =new ArrayList<HeaderGridData>();
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";
    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;
    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */

    private int mFirstVisible;

    private StickyGridHeadersGridView mGridView;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CarArgumentFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);




    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=  inflater.inflate(R.layout.sea_cmp_grid, container, false);

        return v;
    }

    @Override
    public void onDetach() {
        super.onDetach();


    }

    @Override
    public void onHeaderClick(AdapterView<?> parent, View view, long id) {

    }

    @Override
    public boolean onHeaderLongClick(AdapterView<?> parent, View view, long id) {


        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> gridView, View view, int position, long id) {
        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.

    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new GetCarInfor(getActivity().getApplicationContext(),view,savedInstanceState).execute();

    }



    private void initPage(View view, Bundle savedInstanceState){

        mGridView = (StickyGridHeadersGridView)view.findViewById(R.id.asset_grid);
        mGridView.setOnItemClickListener(this);



        //绑定数据为空，Fragment开启后异步去获取数据，染红动态填充
        mGridView.setAdapter(new StickyGridHeadersSimpleArrayAdapter(getActivity().getApplicationContext(),
                list,R.layout.sea_cmp_header,R.layout.sea_cmp_item));
        System.out.println(list.size());

        if (savedInstanceState != null) {
            mFirstVisible = savedInstanceState.getInt(KEY_LIST_POSITION);
        }

        mGridView.setSelection(mFirstVisible);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }


        setHasOptionsMenu(true);
    }



    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mGridView.setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                    : ListView.CHOICE_MODE_NONE);
        }
    }

    @SuppressLint("NewApi")
    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            mGridView.setItemChecked(mActivatedPosition, false);
        } else {
            mGridView.setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }






    /**
     * 获取车辆数据的异步类
     */
    public  class GetCarInfor extends AsyncTask<Void, Void, Void> {
        private JSONObject carinfoObj;

        private List<NameValuePair> carinfoParams = new ArrayList<NameValuePair>();

        private String url = Constant.BASE_URL + "/compare.php";
        private  HashMap<String ,String>E2C = new HashMap<String, String>();

        private View view;
        private Bundle bundle;
        private Context context;
        private ProgressDialog progressDialog;
        public GetCarInfor(Context context ,View view, Bundle savedInstanceState) {
            this.view=view;
            this.bundle=savedInstanceState;
            this.context = context;
            list = new ArrayList<HeaderGridData>();
            fillMap();

        }

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求

            JSONParser jsonParser = new JSONParser();
            carinfoParams.add(new BasicNameValuePair("tag", "compare"));
            carinfoParams.add(new BasicNameValuePair("car_id_1", GBK2UTF.Transform(CarComparison.carInfors.get(0).getCarId())));
            carinfoParams.add(new BasicNameValuePair("car_id_2", GBK2UTF.Transform(CarComparison.carInfors.get(1).getCarId())));
//            carinfoParams.add(new BasicNameValuePair("brand_1", GBK2UTF.Transform(CarCmpShow.carInfors.get(0).getCarSeable())));
//            carinfoParams.add(new BasicNameValuePair("series_1", GBK2UTF.Transform(CarCmpShow.carInfors.get(0).getCarSerie())));
//            carinfoParams.add(new BasicNameValuePair("model_number_1",GBK2UTF.Transform(CarCmpShow.carInfors.get(0).getCarName())));
//            carinfoParams.add(new BasicNameValuePair("brand_2", GBK2UTF.Transform(CarCmpShow.carInfors.get(1).getCarSeable())));
//            carinfoParams.add(new BasicNameValuePair("series_2", GBK2UTF.Transform(CarCmpShow.carInfors.get(1).getCarSerie())));
//            carinfoParams.add(new BasicNameValuePair("model_number_2",GBK2UTF.Transform(CarCmpShow.carInfors.get(1).getCarName())));
            carinfoObj = jsonParser.getJSONFromUrl(url, carinfoParams);

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (carinfoObj != null) {
                try {
                    int success = carinfoObj.getInt("success");
                    if (success == 1) {
                        fillCarInfor();
                        initPage(view,bundle);

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(SearchMain.searchmain, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(SearchMain.searchmain, "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();

            }


        }
        private void fillCarInfor() throws  JSONException {


            String[]  string_item = new String[2];
            JSONObject car1 = carinfoObj.getJSONObject("car_" + 1);
            JSONObject car2 = carinfoObj.getJSONObject("car_" + 2);
            Iterator[] iter = new  Iterator[2];
            iter[0] = car1.keys();
            iter[1] = car2.keys();
            while(iter[0].hasNext()&&iter[1].hasNext()){
                String k = (String)iter[0].next();

                JSONObject value1 =car1.getJSONObject(k);
                JSONObject value2 = car2.getJSONObject(k);

                fillRows(value1,value2,k);
            }

        }

        private String avoidNull(String obj){
            if(obj!=null)
                return obj;
            else
                return "";
        }

        /**
         * 把某些英文翻译成中文
         * @param s
         * @return
         */
        private String Translate(String s){
            if(E2C.containsKey(s)){
                return E2C.get(s);
            }

            return s;
        }


        private void fillRows(JSONObject one ,JSONObject two,String k)throws JSONException{
            JSONObject[] car_item = new JSONObject[2];
            car_item[0] = one;
            car_item[1] = two;
            ArrayList<RowData> rows =new ArrayList<RowData>();
            String header = Translate(k);
            Iterator[] it = new  Iterator[2];
            it[0] = car_item[0].keys();
            it[1] = car_item[1].keys();
            while(it[0].hasNext()&&it[1].hasNext()){
                String key = (String)it[0].next();
                String value1 = avoidNull(car_item[0].getString(key));
                String value2 = avoidNull(car_item[1].getString(key));
                System.out.println(key+"  "+value1+"  "+value2);
                ArrayList<String> strings = new ArrayList<String>();
                strings.add(Translate(key));
                strings.add(value1);
                strings.add(value2);
                rows.add(new RowData(strings));
            }
            HeaderGridData headerGridData = new HeaderGridData(header,rows);

            list.add(headerGridData);

        }
        private void fillMap(){
            E2C.put("car_engine","发动机");
            E2C.put("engine_model_number","发动机型号");
            E2C.put("emission_amount","排量(ml)");
            E2C.put("intake_form","进气方式");
            E2C.put("cylinder_number","汽缸数(个)");
            E2C.put("cylinder_arrangement","气缸排列形式");
            E2C.put("value_per_cylinder_number","每缸气门数(个)");
            E2C.put("compression_ration","压缩比");
            E2C.put("maximum_horsepower","最大马力(Ps)");
            E2C.put("maximum_power","最大功率(Kw)");
            E2C.put("maximum_power_speed","最大功率转速(rpm)");
            E2C.put("fuel_type","燃料形式");
            E2C.put("fuel_grade","燃油标号");
            E2C.put("environmental_level","环保标准");

            E2C.put("car_body_structure","车体结构");
            E2C.put("length","长度(mm)");
            E2C.put("width","宽度(mm)");
            E2C.put("height","高度(mm)");
            E2C.put("weight","整备质量(kg)");
            E2C.put("wheelbase","轴距(mm)");
            E2C.put("minimum_ground_clearance","最小离地间隙(mm)");
            E2C.put("door_number","车门数(个)");
            E2C.put("seat_number","座位数(个)");
            E2C.put("fuel_tank_capacity","油箱容积(L)");
            E2C.put("luggage_compartment_volume","行李箱容积(L)");

            E2C.put("car_multimedia","多媒体设备");
            E2C.put("car_multimedia_configuration","多媒体设备");
            E2C.put("GPS","GPS导航系统");
            E2C.put("bluetooth","蓝牙");
            E2C.put("car_phone","车载电话");
            E2C.put("car_TV","车载电视");
            E2C.put("rear_LCD_screen","后排液晶屏(个)");
            E2C.put("external_audio_interface","外接音源接口(个)");
            E2C.put("USB","外接音源接口");
            E2C.put("AUX","外接音源接口");
            E2C.put("iPod","外接音源接口");
            E2C.put("Speaker_number","扬声器数量");

            E2C.put("car_tech_configuration","高科技设备");
            E2C.put("car_hightech","高科技设备");
            E2C.put("automatic_parking","自动泊车入位");
            E2C.put("night_version_system","夜视系统");
            E2C.put("panoramic_camera","全景摄像头");

            E2C.put("price","价格");
        }
    }

}
