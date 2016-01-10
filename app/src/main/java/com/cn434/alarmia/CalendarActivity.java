package com.cn434.alarmia;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by samsung on 24/2/2558.
 */
public class CalendarActivity extends Activity{

    int i = 0;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_calendar);
//        String[] values = new String[] { "Never", "Monday", "Wednesday",
//                "Tuesday", "Thursday", "Friday", "Saturday", "Sunday"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.layoutlistview, values);
//        final ListView myList = (ListView)findViewById(R.id.listview);
//        myList.setAdapter(adapter);
//        myList.setChoiceMode ( ListView.CHOICE_MODE_MULTIPLE );
//        myList.setItemChecked ( 0, true );

        TextView bug = (TextView)findViewById(R.id.bug);
        bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==9){
                    Toast.makeText(getApplicationContext(),"WELCOME :)",Toast.LENGTH_SHORT).show();
                    i++;
                }
                else if(i>9)
                {

                }
                else
                {
                    i++;
                }
            }
        });

        Button doneButton = (Button) findViewById(R.id.button_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(CalendarActivity.this,SetTimerActivity.class);
//                startActivity(intent);
                finish();
            }
        });

//        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1,
//                                    int position, long arg3) {
//                if(position == 0) {
//                    myList.setItemChecked(1,false);
//                    myList.setItemChecked(2,false);
//                    myList.setItemChecked(3,false);
//                    myList.setItemChecked(4,false);
//                    myList.setItemChecked(5,false);
//                    myList.setItemChecked(6,false);
//                    myList.setItemChecked(7,false);
//                }
//                else{
//                    myList.setItemChecked(0,false);
//                }
//            }
//        });
            }


        }
