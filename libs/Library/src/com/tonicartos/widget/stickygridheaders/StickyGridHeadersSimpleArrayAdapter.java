/*
 Copyright 2013 Tonic Artos

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.tonicartos.widget.stickygridheaders;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.apache.http.Header;




public class StickyGridHeadersSimpleArrayAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {
    private int mHeaderResId;
    private LayoutInflater mInflater;
    private int mItemResId;
    private List<String> mItems;
    private List<String> mHeaders;

    public StickyGridHeadersSimpleArrayAdapter(Context context, ArrayList<HeaderGridData> items, int headerResId, int itemResId) {
        init(context, items, headerResId, itemResId);
    }

    public StickyGridHeadersSimpleArrayAdapter(Context context, HeaderGridData[] items, int headerResId, int itemResId) {
        init(context, Arrays.asList(items), headerResId, itemResId);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public String getHeaderId(int position) {
            //here
        return mHeaders.get(position);
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mHeaderResId, parent, false);
            holder = new HeaderViewHolder();
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        String item = mHeaders.get(position);


        // set header text as first char in string
        holder.textView.setText(item);

        return convertView;
    }

    @Override
    public String getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    @SuppressWarnings("unchecked")
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mItemResId, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String item = getItem(position);

        holder.textView.setText(item);


        return convertView;
    }

    private void init(Context context, List<HeaderGridData> items, int headerResId, int itemResId) {
        this.mItems = new ArrayList<String>();
        this.mHeaders = new ArrayList<String>();
        for(HeaderGridData item : items){
            ArrayList<String> tmp =item.getRowDatas();
            for(String str : tmp){
                this.mItems.add(str);
                this.mHeaders.add("      "+item.getHeader());
            }
        }
        this.mHeaderResId = headerResId;
        this.mItemResId = itemResId;
        mInflater = LayoutInflater.from(context);
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }

    protected class ViewHolder {
        public TextView textView;
    }




}





