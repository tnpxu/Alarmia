package com.cn434.alarmia.captcha;

import java.util.ArrayList;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

public class MathCaptcha extends Captcha {

    protected MathOptions options;

    public enum MathOptions{
        PLUS_MINUS,
        PLUS_MINUS_MULTIPLY
    }

    public MathCaptcha(int width, int height, MathOptions opt){
        this.setHeight(height);
        setWidth(width);
        this.options = opt;
        usedColors = new ArrayList<Integer>();
        this.image = image();
    }

    @Override
    protected Bitmap image() {
        int one = 0;
        int two = 0;
        int three = 0;
        int math = 0;
        int math2 = 0;

        LinearGradient gradient = new LinearGradient(0, 0, getWidth() / 2, this.getHeight() / 2, color(), color(), Shader.TileMode.MIRROR);
        Paint p = new Paint();
        p.setDither(true);
        p.setShader(gradient);
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), this.getHeight(), Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        c.drawRect(0, 0, getWidth(), this.getHeight(), p);

        LinearGradient fontGrad = new LinearGradient(0, 0, getWidth() / 2, this.getHeight() / 2, color(), color(), Shader.TileMode.CLAMP);
        Paint tp = new Paint();
        tp.setDither(true);
        tp.setShader(fontGrad);
        tp.setTextSize(getWidth() / this.getHeight() * 20);
        Random r = new Random(System.currentTimeMillis());
        one = r.nextInt(29)+2;
        two = r.nextInt(29)+2;
        three = r.nextInt(29) + 2;
        math = r.nextInt((options == MathOptions.PLUS_MINUS_MULTIPLY)?3:2);
        if(math == 2)
        {
            if((one*two)-three <= 0)
            {
                math2=0;
            }
            else
            {
                math2=r.nextInt(2);
            }
        }
        else
        {
            if (one < two) {
                Integer temp = one;
                one = two;
                two = temp;
            }
            if(math==1)
            {
                if(one-two-three<=0)
                {
                    while(math2 == 1)
                    {
                        math2 = r.nextInt(3);
                    }
                }
                else
                {
                    math2 = r.nextInt(3);
                }
            }
            else
            {
                if(one+two-three<=0)
                {
                    while(math2 == 1)
                    {
                        math2 = r.nextInt(3);
                    }
                }
                else
                {
                    math2 = r.nextInt(3);
                }
            }
        }

        int temp_ans = 0;

        switch (math) {
            case 0:
                temp_ans = (one+two);
                //this.answer = (one + two) + "";
                break;
            case 1:
                temp_ans = one-two;
                //this.answer = (one - two) + "";
                break;
            case 2:
                temp_ans = one*two;
                //this.answer = (one * two) + "";
                break;
        }

        switch (math2)
        {
            case 0:
                this.answer = temp_ans + three + "";
                break;
            case 1:
                this.answer = temp_ans - three + "";
                break;
            case 2:
                this.answer = temp_ans * three + "";
                break;
        }

        String[] data = new String[]{String.valueOf(one), oper(math), String.valueOf(two) , oper(math2) , String.valueOf(three)};
        int total = 0;
        for(int i=0;i<data.length;i++)
        {
            total += data[i].length();
        }
        int widthperword = (this.getWidth()-20)/total;
        x += 10;
        for (int i=0; i<data.length; i++) {
            y = 50 + Math.abs(r.nextInt()) % 50;
            Canvas cc = new Canvas(bitmap);
            if(i==1 || i==3)
            {
                x+=10;
            }
            cc.drawText(data[i], 0, data[i].length(), x, y, tp);
            x += widthperword;
            tp.setTextSkewX((float)-0.25);
        }
        return bitmap;
    }

    public static String oper(Integer math) {
        switch (math) {
            case 0:
                return "+";
            case 1:
                return "-";
            case 2:
                return "*";
        }
        return "+";
    }
}
