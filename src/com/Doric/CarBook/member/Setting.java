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
    // ���ͷ���Ŀ���switch
    Switch acceptPushSwitch = null;
    // ���ý�������ʱ��İ�ť
    Button startPushTimeBtn = null;
    // ����ֹͣ����ʱ��İ�ť
    Button endPushTimeBtn = null;
    // ���ÿ�ʼʱ���calendar
    Calendar startPushTime = null;
    // ���ý���ʱ���calendar
    Calendar endPushTime = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.setting, container, false);
        // Ϊswitch��ʼ��
        acceptPushSwitch = (Switch) (rootView != null ? rootView.findViewById(R.id.acceptPushSwitch) : null);
        if (acceptPushSwitch!=null) {
            acceptPushSwitch.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            // �������ͷ���
                            if (isChecked) {
                                if (getActivity() != null) {
                                    ((MainActivity)getActivity()).pushOnResume();
                                }
                            }else{
                            // �ر����ͷ���
                                if (getActivity() != null) {
                                    ((MainActivity)getActivity()).pushOnPause();
                                }
                            }
                        }
                    }
            );
        }
        // ��ʼ��calendar�ؼ�
        startPushTime = Calendar.getInstance();
        endPushTime = Calendar.getInstance();

        startPushTime.set(Calendar.HOUR_OF_DAY,6);
        endPushTime.set(Calendar.HOUR_OF_DAY,23);

        startPushTime.set(Calendar.MINUTE,00);
        endPushTime.set(Calendar.MINUTE,00);

        // ��һ�����ս�������
        final Set<Integer> days = new HashSet<Integer>();
        for (int i=0;i<7;i++)
            days.add(i);
        // Button��ʾ������
        final String startPushText = "��ʼ��������ʱ��:"+"       ";
        final String endPushText = "ֹͣ��������ʱ��:"+"       ";
        // ΪButton��ʼ��
        startPushTimeBtn = (Button) (rootView != null ? rootView.findViewById(R.id.startPushTimeBtn) : null);
        startPushTimeBtn.setText(startPushText+startPushTime.get(Calendar.HOUR_OF_DAY)
                +":"+startPushTime.get(Calendar.MINUTE));
        // ��������¼������ڵ���󵯳�TimePickerDialog
        startPushTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(),new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        int endTimeHour = endPushTime.get(Calendar.HOUR_OF_DAY);
                        int endTimeMin = endPushTime.get(Calendar.MINUTE);

                        // �Կ�ʼʱ��ͽ���ʱ������жϣ�����������������
                        if (check(hourOfDay,minute,endTimeHour,endTimeMin)) {
                            startPushTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            startPushTime.set(Calendar.MINUTE, minute);
                            startPushTime.set(Calendar.SECOND, 0);
                            startPushTime.set(Calendar.MILLISECOND, 0);
                            // ��ȡ�û����õ�ʱ��
                            int startTimeHour = startPushTime.get(Calendar.HOUR_OF_DAY);

                            //����JPush api����Pushʱ��
                            JPushInterface.setPushTime(getActivity().getApplication().getApplicationContext(),
                                   days, startTimeHour, endTimeHour);
                            startPushTimeBtn.setText(startPushText + startPushTime.get(Calendar.HOUR_OF_DAY)
                                    + ":" + startPushTime.get(Calendar.MINUTE));
                            Toast.makeText(getActivity(), "���óɹ�", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getActivity(), "��ȷ����ʼʱ�����ڽ���ʱ��", Toast.LENGTH_LONG).show();
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

                        // �Կ�ʼʱ��ͽ���ʱ������жϣ�����������������
                        if (check(startTimeHour,startTimeMin,hourOfDay,minute)) {
                            endPushTime.set(Calendar.HOUR_OF_DAY,hourOfDay);
                            endPushTime.set(Calendar.MINUTE,minute);
                            endPushTime.set(Calendar.SECOND, 0);
                            endPushTime.set(Calendar.MILLISECOND, 0);
                            // ��ȡ�û����õ�ʱ��
                            int endTimeHour = endPushTime.get(Calendar.HOUR_OF_DAY);

                            //����JPush api����Pushʱ��
                            JPushInterface.setPushTime(getActivity().getApplication().getApplicationContext(),
                                    days, startTimeHour, endTimeHour);
                            endPushTimeBtn.setText(endPushText + endPushTime.get(Calendar.HOUR_OF_DAY)
                                    + ":" + endPushTime.get(Calendar.MINUTE));
                            Toast.makeText(getActivity(), "���óɹ�", Toast.LENGTH_LONG).show();

                        }else{
                            Toast.makeText(getActivity(), "��ȷ����ʼʱ�����ڽ���ʱ��", Toast.LENGTH_LONG).show();
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
