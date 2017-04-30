package sbt.smarthome.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by godi22 on 16/12/16.
 *
 */

public class WallRoom extends Room{

    private int wallWidth;
    private int wallColor;
    private String wallPosition = "none";

    @Override
    protected void onDraw(Context context,Canvas canvas, float left, float top, float right, float bot, float ratio) {
        super.onDraw(context, canvas, left, top, right, bot, ratio);

        float lineWidth = wallWidth * ratio;

        paint.setColor(wallColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(lineWidth);

        String[] walls = wallPosition.split("\\|");

        float startX = 0, startY = 0, stopX = 0, stopY = 0;

        for(String wall : walls) {
            boolean isValidWall = true;
            switch (wall) {
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
                    startY = top;
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
                    isValidWall = false;
                    break;
            }
            if (isValidWall) {
                canvas.drawLine(startX, startY, stopX, stopY, paint);
            }
        }
    }
}
