package com.cn434.alarmia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sleepbot.datetimepicker.time.RadialPickerLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by CBoom on 24/2/2558.
 */
public class SleepUntilActivity extends FragmentActivity {
    private DrawerLayout modePanel;
    private TextView untilTime;
    private TextView pm_or_am;
    private String period;
    private String hr;
    private String mn;
    private boolean _24format = true;
    private TimePickerDialog mTimePicker;
    private TimePickerDialog.OnTimeSetListener onTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(RadialPickerLayout radialPickerLayout, int hourOfDay, int minute) {

                    if(hourOfDay<10&&minute<10){
                        untilTime.setText("0"+hourOfDay+":0"+minute);
                    }
                    else if(hourOfDay<10){
                        untilTime.setText("0"+hourOfDay+":"+minute);
                    }
                    else if(minute<10){
                        untilTime.setText(hourOfDay+":0"+minute);
                    }
                    else{
                        untilTime.setText(hourOfDay+":"+minute);
                    }



                    //if system format is 12 hour
                    //it contains some bugs that make the format is not valid in 12 hour
                    //such as 10:30pm is choosen, but its actually value is in 24 format
                    //then convert to actually 12 format
                    if(!mTimePicker.mIs24HourMode) {
                        pm_or_am.setVisibility(View.VISIBLE);
                        // am doesnt contains bug
                        if (radialPickerLayout.getIsCurrentlyAmOrPm() == 0) {
                            pm_or_am.setText("AM");
                            period = "AM";
                            hr = hourOfDay+"";
                            mn = minute+"";
                        }
                        //pm contains bug then convert
                        else {

                            String s = hourOfDay+":"+minute+"pm";
                            SimpleDateFormat f1 = new SimpleDateFormat("HH:mm"); //HH for hour of the day (0 - 23)
                            SimpleDateFormat f2 = new SimpleDateFormat("h:mma");
                            try{
                                Date d = f1.parse(s);
                                s = f2.format(d).toLowerCase();
                            }catch(Exception e){}

                            String a[] = s.split(":");
                            String b[] = a[1].split("\\D");
                            untilTime.setText(a[0]+":"+b[0]);
                            pm_or_am.setText("PM");
                            period = "PM";

                            hr = a[0];
                            mn = b[0];
                        }
                    }
                    //24 hour format
                    else{
                        hr = hourOfDay+"";
                        mn = minute+"";
                    }

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepuntil);
        modePanel = (DrawerLayout) findViewById(R.id.drawer_layout);


        if (!DateFormat.is24HourFormat(this))
        {
            _24format = false;
        }


        mTimePicker = TimePickerDialog.newInstance(onTimeSetListener,
                10,     // หน่วยเข็มชั่วโมง
                30,     // เข็มนาที
                _24format,   // ใช้ระบบนับแบบ 24-Hr หรือไม่? (0 - 23 นาฬิกา)
                false); // ให้สั่นหรือไม่?

        untilTime = (TextView)findViewById(R.id.until_time);
        untilTime.setText("00:00");
        pm_or_am = (TextView)findViewById(R.id.pm_or_am);
        pm_or_am.setVisibility(View.INVISIBLE);

        Button fragmentButton = (Button) findViewById(R.id.fragment_setting);
        Button fragmentOK = (Button) findViewById(R.id.fragment_ok);

        Button sleepUntilButton = (Button) findViewById(R.id.button_sleepuntil);
        Button sleepForButton = (Button) findViewById(R.id.button_sleepfor);



        fragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimePicker.show(getSupportFragmentManager(), "timePicker");
            }
        });

        fragmentOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SharedPreferences mPreferences = getSharedPreferences(DataProvider.currentSettingName, Activity.MODE_WORLD_READABLE);
                SharedPreferences.Editor editor = mPreferences.edit();
                if(!_24format) {
                    editor.putString("sleep_until_period", period);
                    editor.putBoolean("is24format",false);
                }
                else{
                    editor.putBoolean("is24format",true);
                }
                editor.putString("sleep_until_hr", hr);
                editor.putString("sleep_until_mn", mn);
                editor.putBoolean("sleep_for_status",false);
                editor.putBoolean("sleep_until_status",true);
                editor.commit();


                finish();
            }
        });

        //when user clicks sleepuntil mode
        sleepUntilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        //when user clicks sleepfor mode
        sleepForButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SleepUntilActivity.this,SleepForActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
