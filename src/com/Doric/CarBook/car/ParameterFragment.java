package com.Doric.CarBook.car;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Sunyao_Will on 2014/4/3.
 */


import android.content.Context;

import android.os.AsyncTask;

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

import com.Doric.CarBook.R;

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
public class ParameterFragment extends Fragment implements OnItemClickListener,
        OnHeaderClickListener, OnHeaderLongClickListener {
    private static final String KEY_LIST_POSITION = "key_list_position";


    private static ArrayList<HeaderGridData> list = new ArrayList<HeaderGridData>();
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
    public ParameterFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.car_param_grid, container, false);

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
        mGridView = (StickyGridHeadersGridView) view.findViewById(R.id.param_grid);
        mGridView.setOnItemClickListener(this);


        mGridView.setAdapter(new StickyGridHeadersSimpleArrayAdapter(getActivity().getApplicationContext(),
                list, R.layout.sea_cmp_header, R.layout.sea_cmp_item));
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


        private String url = Constant.BASE_URL + "/showcar.php";
        private HashMap<String, String> E2C = new HashMap<String, String>();

        private  Context mContext;
        private View view;
        private  Bundle bundle;
        private ProgressDialog progressDialog;
        public GetCarInfor(Context context,View view, Bundle savedInstanceState) {
            mContext = context;
            list = new ArrayList<HeaderGridData>();
            this.view =view;
            this.bundle = savedInstanceState;
            fillMap();

        }

        protected void onPreExecute() {
            super.onPreExecute();



        }

        protected Void doInBackground(Void... params) {
            //向服务器发送请求

            JSONParser jsonParser = new JSONParser();
            carinfoParams.add(new BasicNameValuePair("tag", "configuration"));
            carinfoParams.add(new BasicNameValuePair("car_id", GBK2UTF.Transform("24")));

//
//
            carinfoObj = jsonParser.getJSONFromUrl(url, carinfoParams);

            return null;
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (carinfoObj != null) {
                try {
                    int success = carinfoObj.getInt("success");
                    if (success == 1) {
                        System.out.println(carinfoObj.toString());
                        fillCarInfor();
                        initPage(view,bundle);

                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else {

                Toast.makeText(mContext, "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();

            }


        }

        private void fillCarInfor() throws JSONException {
            Iterator iter = carinfoObj.keys();

            while (iter.hasNext()) {
                String k = (String) iter.next();
                if(k.equals("success")||k.equals("error")||k.equals("tag")){
                    continue;
                }
                JSONObject value = carinfoObj.getJSONObject(k);


                fillRows(value, k);
            }

        }

        private String avoidNull(String obj) {
            if (obj != null)
                return obj;
            else
                return "";
        }

        /**
         * 把某些英文翻译成中文
         *
         * @param s
         * @return
         */
        private String Translate(String s) {
            if (E2C.containsKey(s)) {
                return E2C.get(s);
            }

            return s;
        }


        private void fillRows(JSONObject one, String k) throws JSONException {

            JSONObject car_item = one;

            ArrayList<RowData> rows = new ArrayList<RowData>();
            String header = Translate(k);
            Iterator it = car_item.keys();

            while (it.hasNext()) {
                String key = (String) it.next();
                String value = avoidNull(car_item.getString(key));

                System.out.println(key + "  " + value);
                ArrayList<String> strings = new ArrayList<String>();
                strings.add(Translate(key));
                strings.add(value);
                rows.add(new RowData(strings));
            }
            HeaderGridData headerGridData = new HeaderGridData("  "+header, rows);

            list.add(headerGridData);

        }

        private void fillMap() {
            E2C.put("car_engine", "发动机");
            E2C.put("engine_model_number", "发动机型号");
            E2C.put("emission_amount", "排量(ml)");
            E2C.put("intake_form", "进气方式");
            E2C.put("cylinder_number", "汽缸数(个)");
            E2C.put("cylinder_arrangement", "气缸排列形式");
            E2C.put("value_per_cylinder_number", "每缸气门数(个)");
            E2C.put("compression_ration", "压缩比");
            E2C.put("maximum_horsepower", "最大马力(Ps)");
            E2C.put("maximum_power", "最大功率(Kw)");
            E2C.put("maximum_power_speed", "最大功率转速(rpm)");
            E2C.put("fuel_type", "燃料形式");
            E2C.put("fuel_grade", "燃油标号");
            E2C.put("environmental_level", "环保标准");

            E2C.put("car_body_structure", "车体结构");
            E2C.put("length", "长度(mm)");
            E2C.put("width", "宽度(mm)");
            E2C.put("height", "高度(mm)");
            E2C.put("weight", "整备质量(kg)");
            E2C.put("wheelbase", "轴距(mm)");
            E2C.put("minimum_ground_clearance", "最小离地间隙(mm)");
            E2C.put("door_number", "车门数(个)");
            E2C.put("seat_number", "座位数(个)");
            E2C.put("fuel_tank_capacity", "油箱容积(L)");
            E2C.put("luggage_compartment_volume", "行李箱容积(L)");

            E2C.put("car_multimedia", "多媒体设备");
            E2C.put("car_multimedia_configuration", "多媒体设备");
            E2C.put("GPS", "GPS导航系统");
            E2C.put("bluetooth", "蓝牙");
            E2C.put("car_phone", "车载电话");
            E2C.put("car_TV", "车载电视");
            E2C.put("rear_LCD_screen", "后排液晶屏(个)");
            E2C.put("external_audio_interface", "外接音源接口(个)");
            E2C.put("USB", "外接音源接口");
            E2C.put("AUX", "外接音源接口");
            E2C.put("iPod", "外接音源接口");
            E2C.put("Speaker_number", "扬声器数量");

            E2C.put("car_tech_configuration", "高科技设备");
            E2C.put("car_hightech", "高科技设备");
            E2C.put("automatic_parking", "自动泊车入位");
            E2C.put("night_version_system", "夜视系统");
            E2C.put("panoramic_camera", "全景摄像头");

            E2C.put("price", "价格");
        }
    }

}
    class GBK2UTF {
        public static String Transform(String str) {
            byte[] b = str.getBytes();
            char[] c = new char[b.length];
            for (int i = 0; i < b.length; i++) {
                if (b[i] != ' ')
                    c[i] = (char) (b[i] & 0x00FF);

            }
            return new String(c);
        }
    }

