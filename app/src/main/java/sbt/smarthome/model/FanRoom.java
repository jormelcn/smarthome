package sbt.smarthome.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.Log;

import sbt.smarthome.PaperView;
import sbt.smarthome.R;

/**
 * Created by godi22 on 20/12/16.
 */

public class FanRoom extends Room {
    private Bitmap img;
    private float scaleX=100;
    private int onColor=0xFFFFF59D;
    private int offColor=0xff9E9E9E;
    private boolean on=false;
    public void setOn(boolean on){
        this.on=on;
    }

    @Override
    protected void onDraw(Context context,Canvas canvas, float left, float top, float right, float bot, float ratio) {
        super.onDraw(context, canvas, left, top, right, bot, ratio);
        img=BitmapFactory.decodeResource(context.getResources(),R.mipmap.fan);
        Matrix matrix = new Matrix();
        int width = img.getWidth();
        int height = img.getHeight();
        float sctwidth = (scaleX*ratio)/width;
        float sctHeigt = (scaleX*ratio)/height;
        matrix.postScale(sctwidth,sctHeigt);
        int LightColor;

        if(on)
            LightColor=onColor;
        else
            LightColor=offColor;

        paint.setColor(LightColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(left,top,left+scaleX*ratio,top+scaleX*ratio,paint);
        try {
            img=Bitmap.createBitmap(img,0,0,width,height,matrix,false);
            canvas.drawBitmap(img,left,top,paint);
        }catch (Exception e){
            e.printStackTrace();
        }



        //Log.d("STATE", "left="+left+"  top="+top);

    }
}
