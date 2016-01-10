package com.cn434.alarmia;

import com.cn434.alarmia.captcha.Captcha;
import com.cn434.alarmia.captcha.MathCaptcha;
import com.cn434.alarmia.captcha.MathCaptcha.MathOptions;
import com.cn434.alarmia.captcha.TextCaptcha;
import com.cn434.alarmia.captcha.TextCaptcha.TextOptions;
import com.cn434.alarmia.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.SyncStateContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Darkbabyz on 4/25/2015.
 */
public class CaptchaActivity extends Activity implements MediaPlayer.OnCompletionListener {

    private SystemUiHider mSystemUiHider;
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final boolean TOGGLE_ON_CLICK = true;
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;


    private MediaPlayer mPlayer;
    ImageView im;
    TextView ans;
    Button btn;
    Button btnans;
    EditText ansin;
    public boolean isVibrate;
    public Vibrator v;

    DisplayMetrics displaymetrics;
    int h;
    int w;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha);
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
        isVibrate = mPreferences.getBoolean("setting", true);
        String getMusic = mPreferences.getString("music", "none");

        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if(isVibrate)
        {
            long[] pattern = {0,10000,100};
            v.vibrate(pattern,0);
        }
        playSound(getMusic);
        btn = (Button)findViewById(R.id.button1);
        im = (ImageView)findViewById(R.id.imageView1);
        ans = (TextView)findViewById(R.id.textView1);
        btnans = (Button) findViewById(R.id.ansbut);
        ansin = (EditText)findViewById(R.id.ansin);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

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

        displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        h = displaymetrics.heightPixels;
        w = displaymetrics.widthPixels;

        Captcha c = new MathCaptcha(w/2, 150, MathOptions.PLUS_MINUS_MULTIPLY);
        //Captcha c = new TextCaptcha(220, 100, 5, TextOptions.NUMBERS_AND_LETTERS);
        im.setImageBitmap(c.image);
        im.setLayoutParams(new LinearLayout.LayoutParams(c.getWidth() * 2, c.getHeight() * 2));
        ans.setText(c.answer);

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Captcha c = new MathCaptcha(w/2, 150, MathOptions.PLUS_MINUS_MULTIPLY);
                //Captcha c = new TextCaptcha(220, 100, 5, TextOptions.NUMBERS_AND_LETTERS);
                im.setImageBitmap(c.image);
                im.setLayoutParams(new LinearLayout.LayoutParams(c.getWidth()*2, c.getHeight()*2));
                ans.setText(c.answer);
            }
        });

        btnans.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        if(ans.getText().toString().equals(ansin.getText().toString()))
                        {
                            if(isVibrate) {
                                v.cancel();
                            }
                            mPlayer.stop();
                            mPlayer.release();
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "false",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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

    public void playSound(String music)
    {
        if(music.equals("Rock"))
        {
            mPlayer = MediaPlayer.create(CaptchaActivity.this,R.raw.sound);
            mPlayer.start();
        }
        else if(music.equals("Pop"))
        {
            mPlayer = MediaPlayer.create(CaptchaActivity.this,R.raw.sound2);
            mPlayer.start();
        }
        else if(music.equals("Classic"))
        {
            mPlayer = MediaPlayer.create(CaptchaActivity.this,R.raw.sound3);
            mPlayer.start();
        }
        else if(music.equals("Jazz"))
        {
            mPlayer = MediaPlayer.create(CaptchaActivity.this,R.raw.sound4);
            mPlayer.start();
        }
        else if(music.equals("EDM"))
        {
            mPlayer = MediaPlayer.create(CaptchaActivity.this,R.raw.sound5);
            mPlayer.start();
        }
        else if(music.equals("HipHop"))
        {
            mPlayer = MediaPlayer.create(CaptchaActivity.this,R.raw.sound6);
            mPlayer.start();
        }
        else if(music.equals("Veela"))
        {
            mPlayer = MediaPlayer.create(CaptchaActivity.this,R.raw.sound7);
            mPlayer.start();
        }
    }


    public void onDestroy() {
        super.onDestroy();
        if(mPlayer != null) {
            mPlayer.release();
        }
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }



    @Override
    public void onCompletion(MediaPlayer mp) {
        //    mPlayer.release();
        //    mPlayer = null;
    }

}
