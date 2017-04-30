package sbt.smarthome.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by jormelcn on 7/12/16.
 *
 */

public class RedRoom extends Room {

    public void onDraw(Context context, Canvas canvas, int left, int top, float ratio) {
        //Cambio el color
        paint.setColor(Color.RED);

        //Lo pinto igual que un Room
        //super.onDraw(canvas, left, top, ratio);
    }
}
