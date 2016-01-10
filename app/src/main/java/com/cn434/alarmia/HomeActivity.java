package com.cn434.alarmia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

//import android.text.format.DateFormat;

/**
 * Created by CBoom on 24/2/2558.
 */
public class HomeActivity extends Activity {
    public static SwipeListView cSwipeListView;
    public static ArrayList<HomeClockItem> cClock;
    public static HomeCustomAdaptor cAdapter;
    private boolean _24format = true;
    public static float s;
    public static float v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_swipe_listview);
        cSwipeListView = (SwipeListView) findViewById(R.id.list_view);



        Random rnd = new Random();
        int tempC = rnd.nextInt(360);
        s = (float)rnd.nextInt(100)/100F;
        if(s<0.8)
        {
            s+=0.8F;
        }
        v =(float)rnd.nextInt(100)/100F;
        if(v>0.7)
        {
            v-=0.3F;
        }
        if(v<0.3)
        {
            v+=0.3F;
        }

        //Generate setting lists
        int color = randomColor(((12 + tempC)>360)?(12 + tempC-360):(12 + tempC));
        cClock = new ArrayList<HomeClockItem>();
        int colorFactor = 1;

        if (!DateFormat.is24HourFormat(this))
        {
            _24format = false;
        }

        for(int i = 1;i <= 10;i++) {
            String prefName = i + "";
            SharedPreferences mPreferences = getSharedPreferences(prefName,Activity.MODE_WORLD_READABLE);
            if(mPreferences.getBoolean("status",false)) {
                HomeClockItem test1;
                color = randomColor(((12 * colorFactor + tempC) > 360) ? (12 * i + tempC - 360) : (12 * i + tempC));
                if (mPreferences.getBoolean("sleep_until_status", false)) {

                    //convert time format maybe we have to use static variable to tell that the time format is 24 or 12?
                    //then use is24format to help us convert for each preferences

                    //need to convert format to 24 hour
                    if(_24format){
                        //if the format of this element is 24, do nothing
                        if(mPreferences.getBoolean("is24format",false)){
                            String hr = mPreferences.getString("sleep_until_hr", "non");
                            String mn = mPreferences.getString("sleep_until_mn", "non");
                            long id = mPreferences.getLong("id",1);
                            String music = mPreferences.getString("music","non");

                            test1 = new HomeClockItem("SLEEP UNTIL " + hr + ":" + mn,
                                    color,prefName);
                        }
                        //if the format of this element is 12 then convert to 24
                        else{
                            String hr = mPreferences.getString("sleep_until_hr", "non");
                            String mn = mPreferences.getString("sleep_until_mn", "non");
                            String period = mPreferences.getString("sleep_until_period", "non").toLowerCase();

                            String s = hr+":"+mn+period;
                            SimpleDateFormat f1 = new SimpleDateFormat("HH:mm"); //HH for hour of the day (0 - 23)
                            SimpleDateFormat f2 = new SimpleDateFormat("h:mma");
                            try{
                                Date d = f2.parse(s);
                                s = f1.format(d).toLowerCase(); // "12:18am
                            }catch(Exception e){}

                            long id = mPreferences.getLong("id",1);
                            String music = mPreferences.getString("music","non");

                            test1 = new HomeClockItem("SLEEP UNTIL " + s,
                                    color,prefName);
                        }
                    }
                    //need to convert format to 12 hour
                    else{
                        //if this format of element is 24 then convert to 12
                        if(mPreferences.getBoolean("is24format",false)){
                            String hr = mPreferences.getString("sleep_until_hr", "non");
                            String mn = mPreferences.getString("sleep_until_mn", "non");


                            String s = hr+":"+mn;
                            SimpleDateFormat f1 = new SimpleDateFormat("HH:mm"); //HH for hour of the day (0 - 23)
                            SimpleDateFormat f2 = new SimpleDateFormat("h:mma");
                            try{
                                Date d = f1.parse(s);
                                s = f2.format(d); // "12:18
                            }catch(Exception e){}

                            String period  = s.substring(s.length()-2,s.length()).toUpperCase();
                            String time = s.split(":")[0] + ":" +s.split(":")[1].split("\\D")[0];


                            long id = mPreferences.getLong("id",1);
                            String music = mPreferences.getString("music","non");

                            test1 = new HomeClockItem("SLEEP UNTIL " +time+" "+period,
                                    color,prefName);
                        }
                        else{
                            String hr = mPreferences.getString("sleep_until_hr", "non");
                            String mn = mPreferences.getString("sleep_until_mn", "non");
                            String period = mPreferences.getString("sleep_until_period", "non");
                            long id = mPreferences.getLong("id",1);
                            String music = mPreferences.getString("music","non");

                            test1 = new HomeClockItem("SLEEP UNTIL " + hr + ":" + mn + " " + period ,
                                    color,prefName);
                        }
                    }


                } else {
                    String hr = mPreferences.getString("sleep_for_hr", "non");
                    String mn = mPreferences.getString("sleep_for_mn", "non");
                    long id = mPreferences.getLong("id",1);
                    String music = mPreferences.getString("music","non");

                    test1 = new HomeClockItem("SLEEP FOR " + hr + " hr:" + mn + " mn" ,
                             color,prefName);
                }

                colorFactor++;
                cClock.add(test1);
            }
        }


        cAdapter = new HomeCustomAdaptor(this, cClock);
        cSwipeListView.setAdapter(cAdapter);

        //end of initial data

        cSwipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    HomeClockItem clock = cClock.get(position);

                    cClock.remove(position);
                    cClock.add(clock);
                }

                cAdapter.notifyDataSetChanged();
            }
        });


        Button homeAddButton = (Button) findViewById(R.id.button_add_home);
        homeAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean settingIsFull = false;

                //search available setting file
                for(int i = 1;i <= 10;i++) {
                    String prefName = i + "";
                    SharedPreferences mPreferences = getSharedPreferences(prefName,Activity.MODE_WORLD_READABLE);
                    if(!mPreferences.getBoolean("status",false)) {
                        DataProvider.currentSettingName = prefName;
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putBoolean("status",false);
                        //default all setting
                        SettingActivity.settingMem = true;
                        GameActivity.gameMem = "captcha";
                        MusicActivity.musicMem = "Rock";
                        editor.putBoolean("setting",SettingActivity.settingMem);
                        editor.putString("game",GameActivity.gameMem);
                        editor.putString("music",MusicActivity.musicMem);

                        editor.commit();
                        settingIsFull = false;
                        break;

                    } else {
                        settingIsFull = true;
                    }
                }

                if(settingIsFull) {
                    Toast.makeText(getApplicationContext(), "Setting Items Is Full !!",
                            Toast.LENGTH_LONG).show();
                } else {
                    Intent settimer = new Intent(HomeActivity.this,SetTimerActivity.class);
                    startActivity(settimer);
                    finish();

                }
            }
        });

    }

    public int randomColor(float hue){
        float []hsv = new float[3];
        hsv[0] = hue;
        hsv[1] = s;
        hsv[2] = v;

        int rgb = Color.HSVToColor(hsv);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return Color.rgb(red, green, blue);
    }



}
