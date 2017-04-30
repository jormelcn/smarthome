package sbt.smarthome.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

/**
 * Created by godi22 on 14/12/16.
 */

public class LightRoom extends Room {


    private float CX;
    private float CY;
    private int onColor=0xFFFFF59D;
    private int offColor=0xff9E9E9E;
    private boolean on=true;
    public void setOn(boolean on){
        this.on=on;
    }

    @Override
    protected void onDraw(Context context, Canvas canvas, float left, float top, float right, float bot, float ratio) {
        super.onDraw(context, canvas, left, top, right, bot, ratio);

        this.r=50;

        int LightColor;

        if(on)
            LightColor=onColor;
        else
            LightColor=offColor;

        paint.setColor(LightColor);
        paint.setStyle(Paint.Style.FILL);

        CX=(left+right)/2;
        CY=(top+bot)/2;
        canvas.drawCircle(CX,CY,this.r,paint);
        //Log.d("STATE", "left="+CX+"  top="+CY);


    }
}
