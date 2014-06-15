package com.Doric.CarBook.member;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.Doric.CarBook.Constant;
import com.Doric.CarBook.R;
import com.Doric.CarBook.car.CarShow;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyComments extends Activity {

    UserFunctions userFunctions;
    JSONObject commentsData;
    JSONObject comments;
    ListView commentList;
    ProgressBar progressBar;
    TextView noCommentsTextView;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private List<ImageView> imageViews = new ArrayList<ImageView>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_comments);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        progressBar = (ProgressBar) findViewById(R.id.comment_progress_bar);
        noCommentsTextView = (TextView) findViewById(R.id.no_comments);

        userFunctions = new UserFunctions(getApplicationContext());
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.ic_launcher);
        imageViews.add(imageView);
        imageViews.add(imageView);

        commentList = (ListView) findViewById(R.id.my_comments_list);
        new GetMyComments().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetMyComments extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            commentsData = userFunctions.getMyComments();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.GONE);
            try {
                comments = commentsData.getJSONObject("comments");
                if (comments.length() == 0) {
                    noCommentsTextView.setVisibility(View.VISIBLE);
                } else {
                    commentList.setAdapter(new CommentsAdapter(MyComments.this, comments));
                    commentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            try {
                                Intent intent = new Intent(MyComments.this, CarShow.class);
                                String carId = comments.getJSONObject(position + "").getString("car_id");
                                Bundle bundle = new Bundle();
                                bundle.putString("car_id", carId);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class CommentsAdapter extends ArrayAdapter<String> {

        private Context context;
        private JSONObject values;

        public CommentsAdapter(Context context, JSONObject values) {
            super(context, R.layout.my_comments_item);
            this.context = context;
            this.values = values;
        }

        @Override
        public int getCount() {
            return values.length();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            final ViewHolder viewHolder;
            if (rowView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                rowView = inflater.inflate(R.layout.my_comments_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.carNameTextView = (TextView) rowView.findViewById(R.id.my_comments_car_name);
                viewHolder.commentsTextView = (TextView) rowView.findViewById(R.id.my_comments_comment);
                viewHolder.imageView = (ImageView) rowView.findViewById(R.id.my_comments_list_image);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) rowView.getTag();
            }
            String carName = null;
            String comment = null;
            String imageUrl = null;
            try {
                carName = values.getJSONObject(position + "").getString("carname");
                comment = values.getJSONObject(position + "").getString("short_comments");
                imageUrl = Constant.BASE_URL + values.getJSONObject(position + "").getString("pictures_url");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            viewHolder.carNameTextView.setText(carName);
            viewHolder.commentsTextView.setText(comment);
            imageLoader.displayImage(imageUrl, viewHolder.imageView);
            return rowView;
        }
    }

    static class ViewHolder {
        TextView carNameTextView;
        TextView commentsTextView;
        ImageView imageView;
    }
}
