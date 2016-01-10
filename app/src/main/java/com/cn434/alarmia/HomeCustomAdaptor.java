package com.cn434.alarmia;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by CBoom on 24/2/2558.
 */
public class HomeCustomAdaptor extends BaseAdapter {
    private Context cContext;
    private LayoutInflater cInflater;
    private ArrayList<HomeClockItem> cClock;
    public HomeCustomAdaptor(Context context, ArrayList<HomeClockItem> clockItem) {
        cContext = context;
        cInflater = LayoutInflater.from(context);
        cClock = clockItem;
    }

    @Override
    public int getCount() {
        return cClock.size();
    }

    @Override
    public Object getItem(int position) {
        return cClock.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = cInflater.inflate(R.layout.activity_home_listitem, parent, false);

            holder.frontList = (RelativeLayout) convertView.findViewById(R.id.front);
            holder.clock = (TextView) convertView.findViewById(R.id.text_home_clock);
            holder.buttonDel = (Button) convertView.findViewById(R.id.button_home_del);
            holder.buttonClockStart = (Button) convertView.findViewById(R.id.button_clock_start);
            holder.tran = (ImageButton) convertView.findViewById(R.id.button_tran);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final HomeClockItem myClock = cClock.get(position);

        holder.frontList.setBackgroundColor(myClock.getColor());
        holder.clock.setText(myClock.getClock());
        Drawable d = cContext.getResources().getDrawable(R.drawable.start_clock_button);
        holder.buttonClockStart.setBackgroundColor(R.drawable.start_clock_button);
        holder.buttonClockStart.setBackground(d);

        holder.buttonClockStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(cContext,StartRunningActivity.class);
                intent.putExtra("timeData",myClock.getId());
                cContext.startActivity(intent);

            }
        });

        holder.buttonDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeActivity.cClock.remove(position);
                HomeActivity.cSwipeListView.closeAnimate(position);
                notifyDataSetChanged();


                String deleteSettingName = myClock.getId();
                SharedPreferences mPreferences = cContext.getSharedPreferences(deleteSettingName,Activity.MODE_WORLD_READABLE);
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.clear();
                editor.commit();



                Toast.makeText(cContext,"Deleted",
                        Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }



    static class ViewHolder {
        TextView clock;
        Button buttonDel;
        Button buttonClockStart;
        ImageView tran;
        RelativeLayout frontList;
    }
}
