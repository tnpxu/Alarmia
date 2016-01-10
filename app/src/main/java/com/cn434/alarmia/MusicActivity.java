package com.cn434.alarmia;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MusicActivity extends Activity implements MediaPlayer.OnCompletionListener{

    private MediaPlayer mPlayer;
    public static String[] values = new String[] { "Rock", "Pop", "Classic",
            "Jazz", "EDM", "HipHop" , "Veela", "Kygo","Radio","TV"};
    public static int tog = 0;
    public static int state = 0;
    //preference
    public static String musicMem = "Rock";

    ListView mPlay;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_music);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.layoutlistview, values);
        mPlay = (ListView)findViewById(R.id.listview);

        mPlay.setAdapter(adapter);
        mPlay.setChoiceMode ( ListView.CHOICE_MODE_SINGLE );
        //setting state checkCorrect
        SharedPreferences mPreferences = getSharedPreferences(DataProvider.currentSettingName,Activity.MODE_WORLD_READABLE);
        String getMusic = mPreferences.getString("music","none");
        checkCorrect(getMusic);



        // For ListItem Click
        mPlay.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                String s = MusicActivity.values[position];
                if(s.equals("Rock")) {
                    playSound("1");
                }
                else if(s.equals("Pop")){
                    playSound("2");
                }
                else if(s.equals("Classic")){
                    playSound("3");
                }
                else if(s.equals("Jazz")){
                    playSound("4");
                }
                else if(s.equals("EDM")){
                    playSound("5");
                }
                else if(s.equals("HipHop")){
                    playSound("6");
                } else if(s.equals("Veela")){
                    playSound("7");
                }

            }
        });




        Button doneButton = (Button) findViewById(R.id.button_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPlayer != null) {
                    mPlayer.release();
                }


                MusicActivity.tog = 0;
                MusicActivity.state = 0;

                SharedPreferences mPreferences = getSharedPreferences(DataProvider.currentSettingName,Activity.MODE_WORLD_READABLE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("music",musicMem);
                editor.commit();


//                for (SharedPreferences p : StartActivity.pointPreferences) {
//                    if (!p.getBoolean("status",true)) {
//                        SharedPreferences.Editor editor = p.edit();
//                        editor.putString("music",musicMem);
//                        editor.commit();
//                        break;
//                    }
//                }
                finish();
            }
        });

        //mPlay = (ImageButton) findViewById(R.id.button_done);
        //mPlay.setOnClickListener(playListener);


    }


    @Override
    public void onBackPressed() {
        if(mPlayer != null) {
            mPlayer.release();
        }
        MusicActivity.tog = 0;
        MusicActivity.state = 0;
        finish();
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
                    mPlayer = MediaPlayer.create(MusicActivity.this,R.raw.sound);
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

    public void playSound(String select) {
        if(MusicActivity.state == 1) {
            if (Integer.parseInt(select) != MusicActivity.tog) {
                mPlayer.stop();
                mPlayer.release();
                mPlayer = null;
                MusicActivity.tog = 0;
            }
        }
        MusicActivity.state=1;
        if(mPlayer == null) {
            try {
                if(select.equals("1")) {
                    mPlayer = MediaPlayer.create(MusicActivity.this, R.raw.sound);
                    mPlayer.start();
                    MusicActivity.tog=1;
                    musicMem = "Rock";
                }
                else if(select.equals("2"))
                {
                    mPlayer = MediaPlayer.create(MusicActivity.this, R.raw.sound2);
                    mPlayer.start();
                    MusicActivity.tog=2;
                    musicMem = "Pop";
                }
                else if(select.equals("3"))
                {
                    mPlayer = MediaPlayer.create(MusicActivity.this, R.raw.sound3);
                    mPlayer.start();
                    MusicActivity.tog=3;
                    musicMem = "Classic";
                }
                else if(select.equals("4"))
                {
                    mPlayer = MediaPlayer.create(MusicActivity.this, R.raw.sound4);
                    mPlayer.start();
                    MusicActivity.tog=4;
                    musicMem = "Jazz";
                }
                else if(select.equals("5"))
                {
                    mPlayer = MediaPlayer.create(MusicActivity.this, R.raw.sound5);
                    mPlayer.start();
                    MusicActivity.tog=5;
                    musicMem = "EDM";
                }
                else if(select.equals("6"))
                {
                    mPlayer = MediaPlayer.create(MusicActivity.this, R.raw.sound6);
                    mPlayer.start();
                    MusicActivity.tog=6;
                    musicMem = "HipHop";
                }
                else if(select.equals("7"))
                {
                    mPlayer = MediaPlayer.create(MusicActivity.this, R.raw.sound7);
                    mPlayer.start();
                    MusicActivity.tog=7;
                    musicMem = "Veela";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
            MusicActivity.state=0;
            MusicActivity.tog=0;
        }
    }

    public void checkCorrect(String music){
        if(music.equals("Rock")) {
            mPlay.setItemChecked ( 0, true );
        }
        else if(music.equals("Pop"))
        {
            mPlay.setItemChecked ( 1, true );
        }
        else if(music.equals("Classic"))
        {
            mPlay.setItemChecked (2, true );
        }
        else if(music.equals("Jazz"))
        {
            mPlay.setItemChecked ( 3, true );
        }
        else if(music.equals("EDM"))
        {
            mPlay.setItemChecked ( 4, true );
        }
        else if(music.equals("HipHop"))
        {
            mPlay.setItemChecked ( 5, true );
        }
        else if(music.equals("Veela"))
        {
            mPlay.setItemChecked ( 6, true );
        }
    }
}
