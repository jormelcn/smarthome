package sbt.smarthome.rooms;

import android.content.Context;
import android.graphics.Canvas;

import sbt.smarthome.rooms.core.RoomParameters;
import sbt.smarthome.rooms.core.RoomParent;

/**
 * Created by jormelcn on 1/05/17.
 *
 */

public class House extends RoomParent {

    static final String BACKGROUND_COLOR = "backgroundColor";
    private int backgroundColor;

    public House(RoomParameters parameters) {
        super(parameters);
        backgroundColor = parameters.getColor(BACKGROUND_COLOR, 0xff000000);
    }

    @Override
    protected void onDraw(Context context, Canvas canvas, float originLeft, float originTop, float density) {
        canvas.drawColor(backgroundColor);
    }
}
