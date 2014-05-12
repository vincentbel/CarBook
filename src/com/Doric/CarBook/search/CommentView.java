package com.Doric.CarBook.search;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.*;
import android.widget.SimpleAdapter;
import com.Doric.CarBook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2014/5/11.
 */
class Comment{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private  String name;
    private  String time;
    private  String text;

}


public class CommentView extends LinearLayout{
    private ListView listView;
    private ArrayList<Comment> comments;
    private Context mycontext;
    public CommentView(Context context){
        super(context);
        this.mycontext = context;

        //∆¿¬€¡–±Ì
        Initialize();

    }

    private ArrayList<Map<String, Object>> getUniformData(ArrayList<Comment> al_cs) {
        ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (Comment cs : al_cs) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("comment_name", cs.getName());
            //Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(cs.getPicPath());
            map.put("comment_time", cs.getTime());
            map.put("comment_text", cs.getText());
            list.add(map);

        }

        return list;

    }
    private void Initialize(){
        this.removeAllViews();
        listView = new ListView(mycontext);
        SimpleAdapter adapter = new SimpleAdapter(mycontext, getUniformData(comments), R.layout.sea_comment,
                new String[]{"comment_name", "comment_time","comment_text"},
                new int[]{R.id.comment_name, R.id.comment_time,R.id.comment_text});
        listView.setDivider(getResources().getDrawable(R.drawable.list_divider));
        listView.setDividerHeight(1);
        listView.setAdapter(adapter);
        this.addView(listView);

        View view=View.inflate(mycontext,R.layout.sea_inputcomment,null);
        Button btn = (Button)view.findViewById(R.id.mycomment_btn);
        btn.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Resources r= getResources();
                v.setBackground(r.getDrawable(R.drawable.btn_1));
                return true;
            }
        });
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.addView(view);
    }

    public void setComment(ArrayList<Comment> comments) {

    }


}
