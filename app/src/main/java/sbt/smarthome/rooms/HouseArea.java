package sbt.smarthome.rooms;

import android.content.Context;
import android.graphics.Canvas;

import sbt.smarthome.rooms.core.RoomParameters;
import sbt.smarthome.rooms.core.RoomParent;

/**
 * Created by jormelcn on 1/05/17.
 *
 */

public class HouseArea extends RoomParent{

    static final String BACKGROUND_COLOR = "backgroundColor";
    static final String LIGHT = "light";

    private int backgroundColor;
    private float light;

    public HouseArea(RoomParameters parameters) {
        super(parameters);
        backgroundColor = parameters.getColor(BACKGROUND_COLOR, 0xffffffff);
        light = parameters.getFloat(LIGHT, 0.8f);
        if(light > 1) light = 1;
        if(light < 0) light = 0;
    }

    @Override
    public void draw(Canvas canvas, float originLeft, float originTop, float density) {
        super.draw(canvas, originLeft, originTop, density);
        int alpha = Math.round((1  - light)*255);
        int lightColor = (alpha<<24)&0xff000000;
        canvas.drawColor(lightColor);
    }

    @Override
    protected void onDraw(Canvas canvas, float originLeft, float originTop, float density) {
        canvas.drawColor(backgroundColor);
    }

}
