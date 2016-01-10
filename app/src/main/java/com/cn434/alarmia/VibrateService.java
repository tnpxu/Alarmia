package com.cn434.alarmia;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

/**
 * Created by tnpxu on 28/4/2558.
 */
public class VibrateService extends Service {


    Vibrator v;
    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();



        v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        // pass the number of millseconds fro which you want to vibrate the phone here we
        // have passed 2000 so phone will vibrate for 2 seconds.

//        long[] pattern = {0,10000,100};
//        v.vibrate(pattern,0);

        v.vibrate(20000);
        // If you want to vibrate  in a pattern
        //  long pattern[]={0,800,200,1200,300,2000,400,4000};
        // 2nd argument is for repetition pass -1 if you do not want to repeat the Vibrate
        // v.vibrate(pattern,-1);

        Toast.makeText(getApplicationContext(), "Phone is Vibrating", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy(){
        v.cancel();
        super.onDestroy();
    }
}
