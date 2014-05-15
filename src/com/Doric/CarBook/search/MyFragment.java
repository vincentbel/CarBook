package com.Doric.CarBook.search;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2014/5/7.
 */
public class MyFragment extends Fragment {
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(SearchMain.searchmain);
        return null;

    }

    public void loading() {
        if (!progressDialog.isShowing()) {

            progressDialog.setMessage("������..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }
    }

    public void stopLoading() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void initPage() {
    }
}
