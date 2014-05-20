package com.Doric.CarBook.car;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;
import com.Doric.CarBook.utility.JSONParser;
import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * Created by Administrator on 2014/5/11.
 */
class Comment {
    private String name;
    private String time;
    private String text;

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

}


public class CommentFragement extends Fragment {

    private ListView listView;
    private static ArrayList<Comment> comments = new ArrayList<Comment>();
    private ScrollView mScrollView = new ScrollView(getActivity().getApplicationContext());
    private EditText editText;
    private ImageButton btn;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        new CommentData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //获取车辆品牌信息

        Initialize();
        return mScrollView;

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

    public void onClick(View v){
        //  点击删除
    }

    //根据已有数据构造Fragment
    private void Initialize() {
        mScrollView.removeAllViews();
        LinearLayout l = new LinearLayout(getActivity().getApplicationContext());
        listView = new ListView(getActivity().getApplicationContext());
        SimpleAdapter adapter = new SimpleAdapter(getActivity().getApplicationContext(), getUniformData(comments), R.layout.car_comment,
                new String[]{"comment_name", "comment_time", "comment_text"},
                new int[]{R.id.comment_name, R.id.comment_time, R.id.comment_text});
        View v= View.inflate(getActivity().getApplicationContext(),R.layout.car_comment,null);
        TextView t_delete = (TextView)v.findViewById(R.id.delete);
        t_delete.setOnClickListener(new DeleteListener());
        listView.setDivider(getResources().getDrawable(R.drawable.list_divider));
        listView.setDividerHeight(1);
        listView.setAdapter(adapter);
        l.addView(listView);


        View view = View.inflate(getActivity().getApplicationContext(), R.layout.car_inputcomment, null);
        btn = (ImageButton) view.findViewById(R.id.mycomment_btn);
        editText = (EditText)view.findViewById(R.id.mycomment_text);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable ed = editText.getText();
                if(ed.toString().trim().equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),"别什么都不说啊亲",Toast.LENGTH_LONG).show();
                }
                else{
                    //把新的评论发送给服务器。

                    //服务器做出反应

                }
            }
        });
        l.addView(view);
        mScrollView.addView(l);
    }




    class DeleteListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //点击删除时的操作
        }
    }











    private class CommentData{
        public   JSONObject commentObj;
        public   List<NameValuePair> commentParams = new ArrayList<NameValuePair>();

        public    String url = Constant.BASE_URL + "showcar/.php";

        public  void CommentData(){

            //commentParams
            //Undone..
            new GetCommentData().execute();

        }

        class GetCommentData extends AsyncTask<Void, Void, Void> {

            public GetCommentData(){
                super();

            }

            protected void onPreExecute() {
                super.onPreExecute();
                //加载时弹出

            }

            protected Void doInBackground(Void... params) {
                //向服务器发送请求
                JSONParser jsonParser = new JSONParser();
                commentObj = jsonParser.getJSONFromUrl(url, commentParams);
                return null;
            }

            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (commentObj != null) {
                    ArrayList<com.Doric.CarBook.search.CarInfor> carlist =new ArrayList<com.Doric.CarBook.search.CarInfor>();
                    try {
                        int success = commentObj.getInt("success");
                        if(success==0){

                            Toast.makeText(getActivity().getApplicationContext(), "没有评论", Toast.LENGTH_LONG).show();


                        }
                        else if (success == 1) {
                            int num = commentObj.getInt("search_number");






                        }
                    } catch (JSONException e) {

                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }



                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "无法连接网络，请检查您的手机网络设置", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
