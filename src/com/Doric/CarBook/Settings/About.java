package com.Doric.CarBook.settings;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.*;
import android.widget.ImageView;
import android.widget.TextView;
import com.Doric.CarBook.R;

import java.util.Stack;

public class About extends Activity implements GestureDetector.OnGestureListener {

    /**
     * Hold a reference to the current animator, so that it can be canceled mid-way.
     */
    public static final String TAG = "About";
    private static final String DEBUG_TAG = "Gestures";
    private ImageView appImageView;
    private ImageView upIcon;
    private TextView appNameTextView;
    private ImageView doricImageView;
    private TextView copyRightTextView;

    Animation appIconAlphaAnimation;
    Animation upIconAnimation;
    Animation zoomOutAppIcon;
    Animation appNameSlideUp;
    Animation doricAnimation;
    Animation doricZoomOutLittle;
    Animation copyRightTextAlpha;


    int currentState = 0;
    public static final int ANIMATION_NUM = 3;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        getActionBar().hide();

        mGestureDetector = new GestureDetector(this, this);

        appImageView = (ImageView) findViewById(R.id.about_app_icon);
        upIcon = (ImageView) findViewById(R.id.about_up);
        appNameTextView = (TextView) findViewById(R.id.about_app_name);
        doricImageView = (ImageView) findViewById(R.id.about_doric);
        copyRightTextView = (TextView) findViewById(R.id.about_copyright_text);

        zoomOutAppIcon = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.about_zoom_out_app_icon);
        appNameSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.about_app_name);
        doricAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.about_doric);
        doricZoomOutLittle = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.about_doric_zoom_out_little);
        copyRightTextAlpha = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.about_copyright_text);

        appIconAlphaAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.about_app_icon_alpha);
        upIconAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.about_up);
        appImageView.startAnimation(appIconAlphaAnimation);
        upIcon.startAnimation(upIconAnimation);
    }


    private void animationSeries(int i, boolean reverse) {
        Interpolator interpolator;
        if (reverse) {
            interpolator = new ReverseInterpolator();
        } else {
            interpolator = new LinearInterpolator();
        }
        switch (i) {
            case 1:
                Log.d(TAG, "animation series 1");
                upIcon.clearAnimation();
                upIcon.setVisibility(View.INVISIBLE);

                zoomOutAppIcon.setInterpolator(interpolator);
                appNameSlideUp.setInterpolator(interpolator);
                appImageView.startAnimation(zoomOutAppIcon);
                appNameTextView.startAnimation(appNameSlideUp);
                break;
            case 2:
                doricImageView.setVisibility(View.VISIBLE);
                Log.d(TAG, "animation series 2");
                doricAnimation.setInterpolator(interpolator);
                doricImageView.startAnimation(doricAnimation);
                break;
            case 3:
                Log.d(TAG, "animation series 3");
                copyRightTextView.setVisibility(View.VISIBLE);
                doricZoomOutLittle.setInterpolator(interpolator);
                copyRightTextAlpha.setInterpolator(interpolator);
                doricImageView.startAnimation(doricZoomOutLittle);
                copyRightTextView.startAnimation(copyRightTextAlpha);
                break;
            default:
                break;
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            //zoomOutAppIcon();
        }
    }

    public void zoomOutAppIcon() {

        final Rect startBounds = new Rect(appImageView.getLeft(), appImageView.getTop(),
                appImageView.getRight(), appImageView.getBottom());
        final Point globalOffset = new Point();
        final int zoomDegree = 3;
        int finalHeight = startBounds.height() / zoomDegree;
        int finalWidth = startBounds.width() / zoomDegree;
        int finalLeft = startBounds.left + (startBounds.width() - finalWidth) / 2;
        int finalRight = finalLeft + finalWidth;
        int finalTop = startBounds.top;
        int finalBottom = finalTop + finalHeight;
        final Rect finalBounds = new Rect(finalLeft, finalTop, finalRight, finalBottom);
        Log.e(TAG, startBounds.toString());
        Log.e(TAG, finalBounds.toString());
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale = 10.0f;
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(appImageView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(appImageView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(appImageView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(appImageView, View.SCALE_Y, startScale, 1f));
        //set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        float dx = event2.getX() - event1.getX();
        float dy = event2.getY() - event1.getY();
        Log.d(DEBUG_TAG, "onFling: dx: " + dx + "   dy: " + dy + "   currentState:" + currentState);
        final float MIN_SWIPE = 64.0f;
        if (dy > MIN_SWIPE || dy < -MIN_SWIPE) {
            if (dy < 0) {
                if (currentState < ANIMATION_NUM) {
                    Log.d(DEBUG_TAG, "zoom out");
                    currentState++;
                    animationSeries(currentState, false);
                }
            } else {
                if (currentState > 0) {
                    Log.d(DEBUG_TAG, "zoom in");
                    animationSeries(currentState, true);
                    currentState--;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mGestureDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return false;
    }
}
