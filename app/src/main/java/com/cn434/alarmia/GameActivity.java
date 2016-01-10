package com.cn434.alarmia;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by samsung on 24/2/2558.
 */
public class GameActivity extends Activity {
    //prerefecrence
    public static String gameMem = "captcha";



    public static String[] values = new String[] {"Captcha" , "Kayao"};

    ListView mGame;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_game);
        Button doneButton = (Button) findViewById(R.id.button_done);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.layoutlistview, values);
        mGame = (ListView)findViewById(R.id.listview);
        mGame.setAdapter(adapter);
        mGame.setChoiceMode ( ListView.CHOICE_MODE_SINGLE );
        //setting state checkCorrect
        SharedPreferences mPreferences = getSharedPreferences(DataProvider.currentSettingName,Activity.MODE_WORLD_READABLE);
        String getGame = mPreferences.getString("game","none");

        checkCorrect(getGame);

        mGame.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                String g = GameActivity.values[position];
                if(g.equals("Captcha")) {
                    gameMem = "captcha";
//                    Toast.makeText(getApplicationContext(),gameMem,Toast.LENGTH_SHORT).show();
                }
                else if(g.equals("Kayao")) {
                    gameMem = "kayao";
//                    Toast.makeText(getApplicationContext(),gameMem,Toast.LENGTH_SHORT).show();
                }

            }
        });


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mPreferences = getSharedPreferences(DataProvider.currentSettingName,Activity.MODE_WORLD_READABLE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putString("game",gameMem);
                editor.commit();

                Intent intent = new Intent(GameActivity.this,SetTimerActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    public void checkCorrect(String game){
        if(game.equals("captcha")) {
            mGame.setItemChecked ( 0, true );
        }
        else if(game.equals("kayao"))
        {
            mGame.setItemChecked ( 1, true );
        }
    }
}
