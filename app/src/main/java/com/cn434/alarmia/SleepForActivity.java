package com.cn434.alarmia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by CBoom on 24/2/2558.
 */
public class SleepForActivity extends Activity {
    private DrawerLayout modePanel;
    private TextView hrTime;
    private TextView mnTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepfor);
        modePanel = (DrawerLayout) findViewById(R.id.drawer_layout);

        final NumberPicker num1 = (NumberPicker)findViewById(R.id.num1);
        final NumberPicker num2 = (NumberPicker)findViewById(R.id.num2);
        hrTime = (TextView) findViewById(R.id.hr_time);
        mnTime = (TextView) findViewById(R.id.mn_time);
        setNumberPickerTextColor(num1, Color.WHITE);
        setNumberPickerTextColor(num2, Color.WHITE);
        num1.setMaxValue(24);
        num1.setMinValue(0);
        num1.setWrapSelectorWheel(true);
        num2.setMaxValue(59);
        num2.setMinValue(1);
        num2.setWrapSelectorWheel(true);



        num1.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                hrTime.setText(newVal+"");
            }
        });

        num2.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mnTime.setText(newVal+"");
            }
        });


        Button doneButton = (Button) findViewById(R.id.done);

       Button sleepUntilButton = (Button) findViewById(R.id.button_sleepuntil);
       Button sleepForButton = (Button) findViewById(R.id.button_sleepfor);


        //done button event
       doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences mPreferences = getSharedPreferences(DataProvider.currentSettingName,Activity.MODE_WORLD_READABLE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("sleep_for_hr",hrTime.getText()+"");
                editor.putString("sleep_for_mn",mnTime.getText()+"");
                editor.putBoolean("sleep_for_status",true);
                editor.putBoolean("sleep_until_status",false);
                editor.commit();


//                for (SharedPreferences p : StartActivity.pointPreferences) {
//                    if (!p.getBoolean("status",true)) {
//                        SharedPreferences.Editor editor = p.edit();
//                        editor.putString("sleep_for_hr",hrTime.getText()+"");
//                        editor.putString("sleep_for_mn",mnTime.getText()+"");
//                        editor.putBoolean("sleep_for_status",true);
//                        editor.putBoolean("sleep_until_status",false);
//                        editor.commit();
//                        break;
//                    }
//                }


                finish();
            }
        });

        //when user clicks sleepuntil mode
        sleepUntilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SleepForActivity.this,SleepUntilActivity.class);
                startActivity(intent);
                finish();

            }
        });

        //when user clicks sleepfor mode
        sleepForButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color)
    {
        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText){
                try{
                    Field selectorWheelPaintField = numberPicker.getClass()
                            .getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText)child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                }
                catch(NoSuchFieldException e){
                    Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalAccessException e){
                    Log.w("setNumberPickerTextColor", e);
                }
                catch(IllegalArgumentException e){
                    Log.w("setNumberPickerTextColor", e);
                }
            }
        }
        return false;
    }
}
