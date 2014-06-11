package com.Doric.CarBook.car;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.Doric.CarBook.R;

/**
 * Created by Sunyao_Will on 2014/4/3.
 */
public class PictureFragment extends Fragment {


    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 关联对应布局
        rootView = inflater.inflate(R.layout.car_picture, container, false);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

