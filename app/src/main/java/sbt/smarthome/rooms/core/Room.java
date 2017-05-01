package sbt.smarthome.rooms.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Region;

/**
 * Created by jormelcn on 4/11/16.
 *
 */
public abstract class Room {

    static final String ID = "id";
    static final String NAME = "name";
    static final String VISIBLE = "visible";
    static final String LEFT = "left";
    static final String TOP = "top";
    static final String WIDTH = "width";
    static final String HEIGHT = "height";

    int id;
    protected float left = 0;
    protected float top = 0;
    protected float width = 0;
    protected float height = 0;
    boolean visible;
    private String name;


    public Room(RoomParameters parameters) {
        id = parameters.getInt(ID, 0);
        name = parameters.getString(NAME,"");
        visible = parameters.getBoolean(VISIBLE, true);
        left = parameters.getFloat(LEFT, 0);
        top = parameters.getFloat(TOP, 0);
        width = parameters.getFloat(WIDTH, 0);
        height = parameters.getFloat(HEIGHT, 0);
    }


    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Room) && ((Room) obj).id == id;
    }


    public void draw(Context context, Canvas canvas, float originLeft, float originTop, float density){
        if(visible) {
            float xi = originLeft + left * density;
            float yi = originTop + top * density;
            float xf = xi + width * density;
            float yf = yi + height * density;
            canvas.clipRect(xi, yi, xf, yf, Region.Op.REPLACE);
            onDraw(context, canvas, xi, yi, density);
        }
    }

    protected abstract void onDraw(Context context, Canvas canvas, float originLeft, float originTop, float density);

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

}
