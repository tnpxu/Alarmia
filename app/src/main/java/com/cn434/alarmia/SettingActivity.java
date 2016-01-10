package com.cn434.alarmia;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by samsung on 24/2/2558.
 */
public class SettingActivity extends Activity {
    public static ImageButton happy;
    public static ImageButton veryHappy;
    public static ImageButton softShake;
    public static ImageButton hardShake;
    public static ImageButton lightOn;
    public static ImageButton lightOff;
    public static int stateShake;
    //prerefecrence
    public static boolean settingMem = true;

    boolean isVibrate;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_setting);
//        happy = (ImageButton) findViewById(R.id.button_happy);
//        veryHappy = (ImageButton) findViewById(R.id.button_veryhappy);
        softShake = (ImageButton) findViewById(R.id.button_softshake);
//        hardShake = (ImageButton) findViewById(R.id.button_hardshake);
//        lightOn = (ImageButton) findViewById(R.id.button_lighton);
//        lightOff = (ImageButton) findViewById(R.id.button_lightoff);

        //setting state checkCorrect
        SharedPreferences mPreferences = getSharedPreferences(DataProvider.currentSettingName,Activity.MODE_WORLD_READABLE);
        isVibrate = mPreferences.getBoolean("setting",true);
        checkCorrect(isVibrate);


        if(isVibrate){
            stateShake = 0;
        }
        else{
            stateShake = 1;
        }

        Button doneButton = (Button) findViewById(R.id.button_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mPreferences = getSharedPreferences(DataProvider.currentSettingName,Activity.MODE_WORLD_READABLE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean("setting",settingMem);
                editor.commit();
//                Intent intent  = new Intent(SettingActivity.this,SetTimerActivity.class);
//                startActivity(intent);
                finish();
            }
        });

//        happy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                happy.setImageResource(R.drawable.happy_red);
//                veryHappy.setImageResource(R.drawable.veryhappy_white);
//            }
//        });
//        veryHappy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                happy.setImageResource(R.drawable.happy);
//                veryHappy.setImageResource(R.drawable.veryhappy);
//            }
//        });
        softShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stateShake == 0){
                    softShake.setImageResource(R.drawable.softshake_white);
                    settingMem = false;
                    stateShake = 1;
                }
                else if(stateShake == 1)
                {
                    softShake.setImageResource(R.drawable.softshake);
                    settingMem = true;
                    stateShake = 0;
                }
            }
        });
//        hardShake.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                softShake.setImageResource(R.drawable.softshake_white);
//                hardShake.setImageResource(R.drawable.hardshake_red);
//            }
//        });
//        lightOn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lightOn.setImageResource(R.drawable.lighton);
//                lightOff.setImageResource(R.drawable.lightoff);
//            }
//        });
//        lightOff.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lightOn.setImageResource(R.drawable.lighton_white);
//                lightOff.setImageResource(R.drawable.lightoff_red);
//            }
//        });
    }

    public void checkCorrect(boolean is){
        if(is){
            softShake.setImageResource(R.drawable.softshake);
        }
        else
        {
            softShake.setImageResource(R.drawable.softshake_white);
        }
    }
}
