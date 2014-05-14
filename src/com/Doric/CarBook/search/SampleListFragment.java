package com.Doric.CarBook.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.Doric.CarBook.R;

import java.io.ObjectInput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SampleListFragment extends ListFragment {
    private SampleAdapter adapter;
    public String carSeableName = "";
    public AlphaShow alphashow = null;
    public ListView listview  =null;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listview= (ListView)inflater.inflate(R.layout.list, null);
		return listview;

	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        adapter = new SampleAdapter(getActivity());
        adapter.clear();
		setListAdapter(adapter);

	}

    public void setData(ArrayList<Map<String,Object>> data)
    {
        adapter.clear();

        for (Map<String,Object> map : data) {
            Integer img = (Integer)map.get("img");
            adapter.add(new SampleItem((String)map.get("title"),(String)map.get("price"), img));
        }



    }

	private class SampleItem {
		public String tag;
        public String price;
		public int iconRes;
		public SampleItem(String tag,String price ,int iconRes) {
			this.tag = tag;
            this.price = price;
			this.iconRes = iconRes;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.sea_row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);
            TextView price = (TextView) convertView.findViewById(R.id.row_price);
            price.setText(getItem(position).price);
			return convertView;
		}

	}

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        SampleItem Info = (SampleItem) l.getItemAtPosition(position);//SimpleAdapter����Map
        Toast.makeText(alphashow, carSeableName+"  "+Info.tag,Toast.LENGTH_LONG).show();
        if (carSeableName.trim().equals("")) {
            return;
        }

        Intent it = new Intent();
        it.putExtra("CarSeableName", carSeableName);
        it.putExtra("CarSystemName", (String)Info.tag);
        it.setClass(alphashow, CarListShow.class);
        alphashow.startActivity(it);
    }
}


