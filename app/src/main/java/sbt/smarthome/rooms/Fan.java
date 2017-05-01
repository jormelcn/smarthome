package sbt.smarthome.rooms;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import sbt.smarthome.R;
import sbt.smarthome.rooms.core.Room;
import sbt.smarthome.rooms.core.RoomParameters;

/**
 * Created by godi22 on 20/12/16.
 *
 */

public class Fan extends Room {

    public Fan(RoomParameters parameters) {
        super(parameters);

    }

    @Override
    protected void onDraw(Context context, Canvas canvas, float originLeft, float originTop, float density) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.fan);
        Matrix matrix = new Matrix();
        matrix.postScale((density*width)/bitmap.getWidth(), (density*height)/bitmap.getHeight());
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        canvas.drawBitmap(bitmap,originLeft, originTop, null);
    }


    /*protected void onDraw(Context context,Canvas canvas, float left, float top, float right, float bot, float ratio) {

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

    }*/
}
