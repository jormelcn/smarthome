package sbt.smarthome.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by godi22 on 16/12/16.
 */

public class DoorRoom extends Room {

    private int DoorWidth;
    private String DoorPosition = "none";

    @Override
    protected void onDraw(Context context, Canvas canvas, float left, float top, float right, float bot, float ratio) {
        super.onDraw(context, canvas, left, top, right, bot, ratio);

        boolean isValidDoor;
        float lineWidth = DoorWidth * ratio;
        float startX = 0, startY = 0, stopX = 0, stopY = 0;
        String[] Doors = DoorPosition.split("\\|");

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);

        for(String Door : Doors){
            isValidDoor = true;

            switch (Door){
                case "top":
                    startX = left;
                    startY = top + lineWidth / 2;
                    stopX = right;
                    stopY = startY;
                    break;
                case "bottom":
                    startX = left;
                    startY = bot - lineWidth / 2;
                    stopX = right;
                    stopY = startY;
                    break;
                case "left":
                    startX = left + lineWidth / 2;
                    startY = top ;
                    stopX = startX;
                    stopY = bot;
                    break;
                case "right":
                    startX = right - lineWidth / 2;
                    startY = top;
                    stopX = startX;
                    stopY = bot;
                    break;
                default:
                    isValidDoor = false;
                    break;
            }
            if (isValidDoor) {
                canvas.drawLine(startX, startY, stopX, stopY, paint);
            }
        }

    }
}
