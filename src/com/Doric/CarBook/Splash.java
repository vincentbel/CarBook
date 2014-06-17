package com.Doric.CarBook;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import com.Doric.CarBook.search.CarSeableData;

public class Splash extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 2000; //延迟时间

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        //如果车辆品牌信息已经保存了，就预读取了

        //隐藏Actionbar
        getActionBar().hide();

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                new ReadCache(getApplicationContext(),getResources()).run();
                Intent mainIntent = new Intent(Splash.this,MainActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }

        }, SPLASH_DISPLAY_LENGTH);

        TextView appNameTextView = (TextView) findViewById(R.id.splash_app_name);
        ImageView appImageView = (ImageView) findViewById(R.id.splash_app_image);
        Animation appImageAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_app_image);
        Animation appNameAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splash_app_name);
        appNameTextView.startAnimation(appNameAnimation);
        appImageView.startAnimation(appImageAnimation);
    }

    @Override
    protected void onPause() {
        JPushInterface.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        JPushInterface.onResume(this);
        super.onResume();
    }



    class ReadCache implements  Runnable {
        private Context context;
        private Resources resources;

        public ReadCache(Context context, Resources res) {
            this.context = context;
            this.resources = res;
        }

        @Override
        public void run() {
            if (!CarSeableData.isload) {
                CarSeableData.ReadCache(context, resources);

            }
        }
    }
}
