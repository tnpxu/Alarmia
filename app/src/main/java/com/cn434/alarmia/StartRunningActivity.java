package com.cn434.alarmia;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cn434.alarmia.util.SystemUiHider;

import java.util.Calendar;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class StartRunningActivity extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    //define counttimer instance
    private CustomCountDownTimer countDownTimer;
    private long startTime = 80000;
    private final long interval = 1000;
    private boolean timerHasStarted = false;
    private TextView currentWakeData;
    private TextView wakeData;
    private int timeSec = 60;
    private int timeMin = 59;
    private int timeHr = 24;
    private String prefName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_running);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        //show wake up time
        Intent myIntent = getIntent();
        prefName = myIntent.getStringExtra("timeData");
        wakeData = (TextView) findViewById(R.id.time_text);
        SharedPreferences mPreferences = getSharedPreferences(prefName,Activity.MODE_WORLD_READABLE);

        if (mPreferences.getBoolean("sleep_until_status", false)) {
            String hr = mPreferences.getString("sleep_until_hr", "non");
            String mn = mPreferences.getString("sleep_until_mn", "non");
            String period = mPreferences.getString("sleep_until_period", "non");
            wakeData.setText("Wake At: " + hr + ":" + mn + " " + period);
            setStartedTime(hr,mn,period);

        } else {
            String hr = mPreferences.getString("sleep_for_hr", "non");
            String mn = mPreferences.getString("sleep_for_mn", "non");
            wakeData.setText("Wake In: " + hr + " HR :" + mn + " MIN" );
            setStartedTime(hr,mn);
        }




        //counting timer
        countDownTimer = new CustomCountDownTimer(startTime, interval);
        currentWakeData = (TextView) findViewById(R.id.middle_text);
        countDownTimer.start();

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen.
                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });

        // Set up the user interaction to manually show or hide the system UI.
        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
       findViewById(R.id.cancel_button).setOnClickListener(
               new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countDownTimer.cancel();
                        finish();
                    }
                }
       );
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    //countdown timer class
    public class CustomCountDownTimer extends CountDownTimer {


        public CustomCountDownTimer(long startTime, long interval)
        {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            currentWakeData.setText("Waking!!!");
            SharedPreferences mPreferences = getSharedPreferences(prefName,Activity.MODE_WORLD_READABLE);
            String chooseGame = mPreferences.getString("game","none");

            if(chooseGame.equals("captcha")) {
                Intent intent = new Intent(StartRunningActivity.this,CaptchaActivity.class);
                intent.putExtra("timeData",prefName);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(StartRunningActivity.this,ShakeActivity.class);
                intent.putExtra("timeData",prefName);
                startActivity(intent);
                finish();
            }

        }

        @Override
        public void onTick(long millisUntilFinished) {

            int secUntilFinished = (int)millisUntilFinished/1000;

            timeSec = secUntilFinished % 60;
            timeMin = (secUntilFinished / 60) % 60;
            timeHr = (secUntilFinished / (60 * 60)) % 24;

            currentWakeData.setText("Time Elapsed: " + timeHr + " : " + timeMin  + " : " + timeSec);
        }
    }

    public void setStartedTime (String hr,String mn,String period) {
        Calendar getCalInstance = Calendar.getInstance();

        //current time
        long nowSec = getCalInstance.get(Calendar.SECOND);
        long nowMin = getCalInstance.get(Calendar.MINUTE) * 60;
        long nowHr = getCalInstance.get(Calendar.HOUR_OF_DAY) * 60 * 60;

        //setting time
        long toHr;
        long toMn = Long.parseLong(mn, 10) * 60;
        if(period.equals("PM")) {
            toHr = ( Long.parseLong(hr, 10) + 12 ) * 3600;
        } else {
            toHr = Long.parseLong(hr, 10) * 3600;
        }

        //total current time and setting tim9e to second
        long totalCurrentTime = nowSec + nowMin + nowHr;
        long totalSettingTime = toHr + toMn;

        //difference of setting and current
        long resultDiffTime;
        if(totalSettingTime >= totalCurrentTime) {
            resultDiffTime = totalSettingTime - totalCurrentTime;
        } else {
            resultDiffTime = 86400 - (totalCurrentTime - totalSettingTime);
        }

        startTime = resultDiffTime * 1000;


    }

    public void setStartedTime (String hr,String mn) {
        long toHr = Long.parseLong(hr, 10) * 3600 * 1000;
        long toMn = Long.parseLong(mn, 10) * 60 * 1000;
        startTime = toHr + toMn;
    }
}
