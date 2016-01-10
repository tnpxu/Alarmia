package com.cn434.alarmia;

/**
 * Created by CBoom on 24/2/2558.
 */
public class HomeClockItem {
    String clock;
    //int imageResourceId;
    int color;
    String id;

    public HomeClockItem(String clock, int color,String id) {
        this.clock = clock;
        //this.imageResourceId = imgId;
        this.color = color;
        this.id = id;
    }

    public String getClock() {
        return clock;
    }
//    public int getImageResourceId() {
//        return imageResourceId;
//    }
    public int getColor(){return color;}
    public String getId(){ return id;}
}
