

package com.Doric.CarBook.car;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;

import android.text.format.Time;
import android.util.Log;
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

public class CommentFragment extends Fragment  {

    private ListView listView;
    private static ArrayList<Comment> comments = new ArrayList<Comment>();
    private LinearLayout layout;
    private EditText editText;
    private ImageButton btn;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //��ȡ����Ʒ����Ϣ

        Initialize();
        //new CommentData();
        return layout;

    }



    //�����������ݹ���Fragment
    private void Initialize() {


        View view = View.inflate(getActivity().getApplicationContext(), R.layout.car_inputcomment, null);
        layout = (LinearLayout)view;
        btn = (ImageButton) layout.findViewById(R.id.mycomment_btn);
        editText = (EditText)layout.findViewById(R.id.mycomment_text);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable ed = editText.getText();
                if(ed.toString().trim().equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),"��ʲô����˵����",Toast.LENGTH_LONG).show();
                }
                else{

                    CommentListAdapter commentListAdapter = (CommentListAdapter)listView.getAdapter();
                    Comment comment = new Comment();
                    comment.setName("id"); //�û���
                    comment.setText(ed.toString());//����
                    Time t=new Time(); // or Time t=new Time("GMT+8"); ����Time Zone���ϡ�

                    t.setToNow(); // ȡ��ϵͳʱ�䡣
                    int year = t.year;
                    int month = t.month;
                    int date = t.monthDay;
                    int hour = t.hour; // 0-23
                    int minute = t.minute;
                    int second = t.second;
                    comment.setTime(year+"."+month+"."+date+" "+hour+":"+minute+":"+second);
                    commentListAdapter.add(comment);
                    editText.setText("");
                    handler.post(new UpdateRunnable(commentListAdapter));
                    //new AddComment(comment);

                }
            }
        });

        listView = (ListView)layout.findViewById(R.id.comment_list);
        CommentListAdapter adapter = new CommentListAdapter(getActivity().getApplicationContext(),R.layout.car_comment,comments);
        listView.setAdapter(adapter);
        listView.setDivider(getResources().getDrawable(R.drawable.list_divider));
        listView.setDividerHeight(1);




    }





    class DeleteListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {


            RelativeLayout relativeLayout = (RelativeLayout)v.getParent();
            Comment comment = new Comment();
            comment.setText(((TextView)relativeLayout.findViewById(R.id.comment_text)).getText().toString());
            comment.setName(((TextView) relativeLayout.findViewById(R.id.comment_name)).getText().toString());
            comment.setTime(((TextView) relativeLayout.findViewById(R.id.comment_time)).getText().toString());
            //����Ǳ��˵�����
            //ɾ��
            CommentListAdapter commentListAdapter = (CommentListAdapter)listView.getAdapter();
            commentListAdapter.remove(comment);
            handler.post(new UpdateRunnable(commentListAdapter));
            //new DeleteComment(comment);
            //�������
            //ɶ�¶�����

        }
    }







    final android.os.Handler handler = new android.os.Handler();
    class UpdateRunnable implements  Runnable{
        CommentListAdapter commentListAdapter  = null;
        public UpdateRunnable(CommentListAdapter sa){
            commentListAdapter = sa;
        }
        public void run() {

            commentListAdapter.notifyDataSetChanged();
            System.out.println(comments.size());
        }
    };




    //��ȡ���۵��첽����
    //������������Tag�����ɹ���ȡ�������listView
    //������ʾ��ȡʧ��

    private class CommentData{
        public   JSONObject commentObj;
        public   List<NameValuePair> commentParams = new ArrayList<NameValuePair>();

        public    String url = Constant.BASE_URL + "showcar/.php";

        public  void CommentData(){

            new GetCommentData().execute();
        }
        //���������ȡ��������

        //����������ݣ����������ݷ��͸����ݿ�

        private class GetCommentData extends AsyncTask<Void, Void, Void> {

            public GetCommentData(){
                super();

            }

            protected void onPreExecute() {
                super.onPreExecute();
                //����ʱ����

            }

            protected Void doInBackground(Void... params) {
                //���������������
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

                            Toast.makeText(getActivity().getApplicationContext(), "û������", Toast.LENGTH_LONG).show();


                        }
                        else if (success == 1) {
                            //��ȡ��������






                        }
                    } catch (JSONException e) {

                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }



                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "�޷��������磬���������ֻ���������", Toast.LENGTH_LONG).show();
                }
            }
        }


    }

    //������۵��첽����
    //������������Ҫ��ӵ����ۣ����ɹ���ӣ������listView
    //������ʾ���ʧ��
    class AddComment {
        private Comment comment;
        List<NameValuePair> addParams = new ArrayList<NameValuePair>();
        JSONObject addObject;
        public    String url = Constant.BASE_URL + "showcar/.php";
        public AddComment(Comment comment){
            this.comment = comment;
            new Add().execute();
        }
        private class Add extends AsyncTask<Void, Void, Void> {



            protected Void doInBackground(Void... params) {
                //���������������
                JSONParser jsonParser = new JSONParser();
                addObject = jsonParser.getJSONFromUrl(url, addParams);
                return null;
            }

            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (addObject != null) {
                    ArrayList<com.Doric.CarBook.search.CarInfor> carlist = new ArrayList<com.Doric.CarBook.search.CarInfor>();
                    try {
                        int success = addObject.getInt("success");
                        if (success == 0) {

                            Toast.makeText(getActivity().getApplicationContext(), "�������ʧ��", Toast.LENGTH_LONG).show();


                        } else if (success == 1) {
                            CommentListAdapter commentListAdapter = (CommentListAdapter)listView.getAdapter();
                            commentListAdapter.add(comment);
                            handler.post(new UpdateRunnable(commentListAdapter));
                        }
                    } catch (JSONException e) {

                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "�޷��������磬���������ֻ���������", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //ɾ�����۵��첽����
    //������������Ҫɾ�������ۣ����ɹ�ɾ���������listView
    //������ʾɾ��ʧ��
    class DeleteComment {
        private Comment comment;
        List<NameValuePair> deleteParams = new ArrayList<NameValuePair>();
        JSONObject deleteObject;
        public    String url = Constant.BASE_URL + "showcar/.php";
        public DeleteComment(Comment comment){
            this.comment = comment;
            new Add().execute();
        }
        private class Add extends AsyncTask<Void, Void, Void> {



            protected Void doInBackground(Void... params) {
                //���������������
                JSONParser jsonParser = new JSONParser();
                deleteObject = jsonParser.getJSONFromUrl(url, deleteParams);
                return null;
            }

            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (deleteObject != null) {
                    ArrayList<com.Doric.CarBook.search.CarInfor> carlist = new ArrayList<com.Doric.CarBook.search.CarInfor>();
                    try {
                        int success = deleteObject.getInt("success");
                        if (success == 0) {

                            Toast.makeText(getActivity().getApplicationContext(), "ɾ������ʧ��", Toast.LENGTH_LONG).show();


                        } else if (success == 1) {
                            CommentListAdapter commentListAdapter = (CommentListAdapter)listView.getAdapter();
                            commentListAdapter.remove(comment);
                            handler.post(new UpdateRunnable(commentListAdapter));
                        }
                    } catch (JSONException e) {

                        Toast.makeText(getActivity().getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "�޷��������磬���������ֻ���������", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    class CommentListAdapter extends ArrayAdapter<Comment>{
        private int resource;
        public CommentListAdapter(Context context, int resourceId, List<Comment> objects) {
            super(context, resourceId, objects);
            resource = resourceId;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            RelativeLayout relativeLayout;
            Comment comment = getItem(position);
            String name = comment.getName();
            String text = comment.getText();
            String time = comment.getTime();

            if(convertView == null) {
                relativeLayout = new RelativeLayout(getContext());
                String inflater = Context.LAYOUT_INFLATER_SERVICE;
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
                vi.inflate(resource, relativeLayout, true);
                Log.d("Adapter", "convertView is null now");
            } else {
                relativeLayout = (RelativeLayout)convertView;
                Log.d("Adapter", "convertView is not null now");
            }
            //������
            TextView nameView = (TextView) relativeLayout.findViewById(R.id.comment_name);
            TextView textView = (TextView) relativeLayout.findViewById(R.id.comment_text);
            TextView timeView = (TextView) relativeLayout.findViewById(R.id.comment_time);
            nameView.setText(name);
            textView.setText(text);
            timeView.setText(time);
            TextView deleteView = (TextView)relativeLayout.findViewById(R.id.delete);
            deleteView.setOnClickListener(new DeleteListener());


            return relativeLayout;
        }


    }

}
