package com.cn434.alarmia;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.cn434.alarmia.util.SystemUiHider;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class ShakeActivity extends Activity implements MediaPlayer.OnCompletionListener{

    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final boolean TOGGLE_ON_CLICK = true;
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
    private static int countShake = 0;
    private static TextView countText;
    private SystemUiHider mSystemUiHider;
    private boolean isVibrate;
    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;

    private MediaPlayer mPlayer;

    public Vibrator v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();
        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock =  keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();


        //get preference
        Intent myIntent = getIntent();
        String prefName = myIntent.getStringExtra("timeData");
        SharedPreferences mPreferences = getSharedPreferences(prefName,Activity.MODE_WORLD_READABLE);
        isVibrate = mPreferences.getBoolean("setting",true);
        String getMusic = mPreferences.getString("music","none");

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(isVibrate)
        {
            long[] pattern = {0,10000,100};
            v.vibrate(pattern,0);
        }
        playSound(getMusic);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);
        countText =(TextView) findViewById(R.id.count_shake);
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

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
               // Toast.makeText(ShakeActivity.this, (countShake++)+"", Toast.LENGTH_SHORT).show();
                countText.setText("count = "+(countShake++));
                if(countShake==50){
                    countShake = 0;
                    if(isVibrate) {
//                        Intent stopIntent = new Intent(ShakeActivity.this, VibrateService.class);
//                        stopIntent.setAction("com.cn434.alarmia.action.startforeground");
//                        startService(stopIntent);
                        v.cancel();
                    }
                    mPlayer.stop();
                    mPlayer.release();
                    finish();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
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


    public void playSound(String music)
    {
        if(music.equals("Rock"))
        {
            mPlayer = MediaPlayer.create(ShakeActivity.this, R.raw.sound);
            mPlayer.start();
        }
        else if(music.equals("Pop"))
        {
            mPlayer = MediaPlayer.create(ShakeActivity.this,R.raw.sound2);
            mPlayer.start();
        }
        else if(music.equals("Classic"))
        {
            mPlayer = MediaPlayer.create(ShakeActivity.this,R.raw.sound3);
            mPlayer.start();
        }
        else if(music.equals("Jazz"))
        {
            mPlayer = MediaPlayer.create(ShakeActivity.this,R.raw.sound4);
            mPlayer.start();
        }
        else if(music.equals("EDM"))
        {
            mPlayer = MediaPlayer.create(ShakeActivity.this,R.raw.sound5);
            mPlayer.start();
        }
        else if(music.equals("HipHop"))
        {
            mPlayer = MediaPlayer.create(ShakeActivity.this,R.raw.sound6);
            mPlayer.start();
        }
        else if(music.equals("Veela"))
        {
            mPlayer = MediaPlayer.create(ShakeActivity.this,R.raw.sound7);
            mPlayer.start();
        }
        mPlayer.setLooping(true);
    }

    public void onDestroy() {
        super.onDestroy();
        if(mPlayer != null) {
            mPlayer.release();
        }
    }

    private View.OnClickListener playListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            if(mPlayer == null) {
                try {
                    mPlayer = MediaPlayer.create(ShakeActivity.this,R.raw.sound);
                    mPlayer.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
            }

        }

    };

    @Override
    public void onCompletion(MediaPlayer mp) {
        //    mPlayer.release();
        //    mPlayer = null;
    }
}
