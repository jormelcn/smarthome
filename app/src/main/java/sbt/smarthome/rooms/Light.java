package sbt.smarthome.rooms;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import sbt.smarthome.rooms.core.Room;
import sbt.smarthome.rooms.core.RoomParameters;
import sbt.smarthome.rooms.core.State;

/**
 * Created by godi22 on 14/12/16.
 *
 */

public class Light extends Room {

    static final String RADIUS = "radius";
    static final String LIGHT = "light";
    static final String LIGHT_COLOR = "lightColor";

    private int radius;
    private float light;
    private int lightColor;
    Paint paint;

    public Light(RoomParameters parameters) {
        super(parameters);
        radius = parameters.getInt(RADIUS, 0);
        width = radius * 2;
        height = radius * 2;
        light = parameters.getFloat(LIGHT,0);
        if(light > 1) light = 1;
        if(light < 0) light = 0;
        lightColor = parameters.getColor(LIGHT_COLOR, 0xffffff20);
        paint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas, float originLeft, float originTop, float density) {
        int alpha = Math.round(light*0xA0);
        int color = ((alpha<<24)&0xff000000)|(lightColor&0x00ffffff);
        paint.setColor(color);
        canvas.drawOval(new RectF(originLeft, originTop, originLeft + width * density, originTop + height * density), paint);
    }

    @Override
    protected void onClick() {
        if(light <= 0.4f){
            light = 1;
        }else {
            light = 0.4f;
        }
        sync(new State(LIGHT, light));
        refresh();
    }

    @Override
    protected void onSync(State state) {
        light = state.get(LIGHT, light);
        refresh();
    }
}
