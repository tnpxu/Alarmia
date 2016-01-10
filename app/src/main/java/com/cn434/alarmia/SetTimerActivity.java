package com.cn434.alarmia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by CBoom on 23/2/2558.
 */
public class SetTimerActivity extends Activity  {
    private static TextView pmamText;
    private static TextView clock;
    private DrawerLayout modePanel;
    GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent myIntent = getIntent();
        String temp = myIntent.getStringExtra("pmamText");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settimer);
        modePanel = (DrawerLayout) findViewById(R.id.drawer_layout);

        clock = (TextView) findViewById(R.id.text_clock);
        pmamText = (TextView) findViewById(R.id.text_pmam);


        //when user click mode
        Button sleepUntilButton = (Button) findViewById(R.id.button_sleepuntil);
        Button sleepForButton = (Button) findViewById(R.id.button_sleepfor);
        ImageButton doneToHome = (ImageButton) findViewById(R.id.button_donetohome);
        ImageButton musicButton = (ImageButton) findViewById(R.id.button_music);
        ImageButton calendarButton = (ImageButton) findViewById(R.id.button_calendar);
        ImageButton gameButton = (ImageButton) findViewById(R.id.button_game);
        ImageButton settingButton = (ImageButton) findViewById(R.id.button_setting);



        doneToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences mPreferences = getSharedPreferences(DataProvider.currentSettingName,Activity.MODE_WORLD_READABLE);
                if(mPreferences.getBoolean("sleep_until_status",false)||mPreferences.getBoolean("sleep_for_status",false)) {
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putBoolean("status",true);
                    editor.commit();
                }





                Intent intent = new Intent(SetTimerActivity.this,HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });



        //when user clicks sleepuntil mode
        sleepUntilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetTimerActivity.this,SleepUntilActivity.class);
                startActivity(intent);
            }
        });

        //when user clicks sleepfor mode
        sleepForButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetTimerActivity.this,SleepForActivity.class);
                startActivity(intent);
            }
        });


        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(SetTimerActivity.this,MusicActivity.class);
                startActivity(intent);
            }
        });


        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(SetTimerActivity.this,CalendarActivity.class);
                startActivity(intent);
            }
        });


        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(SetTimerActivity.this,GameActivity.class);
                startActivity(intent);
            }
        });


        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(SetTimerActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences mPreferences = getSharedPreferences(DataProvider.currentSettingName,Activity.MODE_WORLD_READABLE);
        if(mPreferences.getBoolean("sleep_until_status",false)) {
            if(mPreferences.getBoolean("is24format",false)){
                String hr = mPreferences.getString("sleep_until_hr", "non");
                String mn = mPreferences.getString("sleep_until_mn", "non");

                clock.setText(hr+":"+mn);
            }
            else{
                String hr = mPreferences.getString("sleep_until_hr", "non");
                String mn = mPreferences.getString("sleep_until_mn", "non");
                String period = mPreferences.getString("sleep_until_period", "non");
                clock.setText(hr+":"+mn);
                pmamText.setText(period);
            }
        }
        if(mPreferences.getBoolean("sleep_for_status",false)){
            String hr = mPreferences.getString("sleep_for_hr", "non");
            String mn = mPreferences.getString("sleep_for_mn", "non");

            clock.setText(hr+":"+mn);

        }

    }
}
