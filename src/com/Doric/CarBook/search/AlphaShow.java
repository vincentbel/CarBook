package com.Doric.CarBook.search;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.Doric.CarBook.R;

import android.app.Fragment;


import java.io.*;

import java.net.HttpURLConnection;

import java.net.URL;
import java.util.*;


public class AlphaShow extends Fragment {


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        SearchMain.searchmain.stopLoading();
        SearchMain.searchmain.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        if(!AlphaShowData.isload){
            AlphaShowData.initPage(SearchMain.searchmain,getResources());
        }


        return AlphaShowData.mRelativeLayout;

    }

    @Override
    public void onStart() {
        super.onStart();
        // new AlphaShow.GetPicData().start();
    }

    @Override
    public void onResume() {
        super.onResume();

        SearchMain.searchmain.mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }





}









