package com.Doric.CarBook.member;

import android.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import cn.jpush.android.api.JPushInterface;
import com.Doric.CarBook.MainActivity;
import com.Doric.CarBook.R;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sunyao_Will on 2014/5/27.
 */
public class Setting extends Fragment {
    // 推送服务的开关switch
    Switch acceptPushSwitch = null;
    // 设置接收推送时间的按钮
    Button startPushTimeBtn = null;
    // 设置停止推送时间的按钮
    Button endPushTimeBtn = null;
    // 设置开始时间的calendar
    Calendar startPushTime = null;
    // 设置结束时间的calendar
    Calendar endPushTime = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting, container, false);
        // 为switch初始化
        acceptPushSwitch = (Switch) (rootView != null ? rootView.findViewById(R.id.acceptPushSwitch) : null);
        if (acceptPushSwitch!=null) {
            acceptPushSwitch.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            // 开启推送服务
                            if (isChecked) {
                                if (getActivity() != null) {
                                    ((MainActivity)getActivity()).pushOnResume();
                                }
                            }else{
                            // 关闭推送服务
                                if (getActivity() != null) {
                                    ((MainActivity)getActivity()).pushOnPause();
                                }
                            }
                        }
                    }
            );
        }
        // 初始化calendar控件
        startPushTime = Calendar.getInstance();
        endPushTime = Calendar.getInstance();

        startPushTime.set(Calendar.HOUR_OF_DAY,6);
        endPushTime.set(Calendar.HOUR_OF_DAY,23);

        startPushTime.set(Calendar.MINUTE,00);
        endPushTime.set(Calendar.MINUTE,00);

        // 周一到周日接收推送
        final Set<Integer> days = new HashSet<Integer>();
        for (int i=0;i<7;i++)
            days.add(i);
        // Button显示的内容
        final String startPushText = "开始接收推送时间:"+"       ";
        final String endPushText = "停止接收推送时间:"+"       ";
        // 为Button初始化
        startPushTimeBtn = (Button) (rootView != null ? rootView.findViewById(R.id.startPushTimeBtn) : null);
        startPushTimeBtn.setText(startPushText+startPushTime.get(Calendar.HOUR_OF_DAY)
                +":"+startPushTime.get(Calendar.MINUTE));
        // 监听点击事件，并在点击后弹出TimePickerDialog
        startPushTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int endTimeHour = endPushTime.get(Calendar.HOUR_OF_DAY);
                        int endTimeMin = endPushTime.get(Calendar.MINUTE);

                        // 对开始时间和结束时间进行判断，满足条件才能设置
                        if (check(hourOfDay,minute,endTimeHour,endTimeMin)) {
                            startPushTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            startPushTime.set(Calendar.MINUTE, minute);
                            startPushTime.set(Calendar.SECOND, 0);
                            startPushTime.set(Calendar.MILLISECOND, 0);
                            // 获取用户设置的时间
                            int startTimeHour = startPushTime.get(Calendar.HOUR_OF_DAY);

                            //调用JPush api设置Push时间
                            JPushInterface.setPushTime(getActivity().getApplication().getApplicationContext(),
                                   days, startTimeHour, endTimeHour);
                            startPushTimeBtn.setText(startPushText + startPushTime.get(Calendar.HOUR_OF_DAY)
                                    + ":" + startPushTime.get(Calendar.MINUTE));
                            Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getActivity(), "请确保开始时间早于结束时间", Toast.LENGTH_LONG).show();
                        }
                    }
                },startPushTime.get(Calendar.HOUR_OF_DAY),startPushTime.get(Calendar.MINUTE),true).show();
            }
        });

        endPushTimeBtn = (Button) rootView.findViewById(R.id.endPushTimeBtn);
        endPushTimeBtn.setText(endPushText+endPushTime.get(Calendar.HOUR_OF_DAY)
                +":"+endPushTime.get(Calendar.MINUTE));
        endPushTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int startTimeHour = startPushTime.get(Calendar.HOUR_OF_DAY);
                        int startTimeMin = startPushTime.get(Calendar.MINUTE);

                        // 对开始时间和结束时间进行判断，满足条件才能设置
                        if (check(startTimeHour,startTimeMin,hourOfDay,minute)) {
                            endPushTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                            endPushTime.set(Calendar.MINUTE,minute);
                            endPushTime.set(Calendar.SECOND, 0);
                            endPushTime.set(Calendar.MILLISECOND, 0);
                            // 获取用户设置的时间
                            int endTimeHour = endPushTime.get(Calendar.HOUR_OF_DAY);

                            //调用JPush api设置Push时间
                            JPushInterface.setPushTime(getActivity().getApplication().getApplicationContext(),
                                    days, startTimeHour, endTimeHour);
                            endPushTimeBtn.setText(endPushText + endPushTime.get(Calendar.HOUR_OF_DAY)
                                    + ":" + endPushTime.get(Calendar.MINUTE));
                            Toast.makeText(getActivity(), "设置成功", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getActivity(), "请确保开始时间早于结束时间", Toast.LENGTH_LONG).show();
                        }
                    }
                },endPushTime.get(Calendar.HOUR_OF_DAY),endPushTime.get(Calendar.MINUTE),true).show();
            }
        });


        return rootView;
    }

    boolean check (int aHour, int aMin,int bHour, int bMin){
        if (aHour<bHour)
            return true;
        if (aHour>bHour)
            return false;
        if (aHour==bHour){
            if(aMin>bMin)
                return false;
            else
                return true;
        }
        return true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
