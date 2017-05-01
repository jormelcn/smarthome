package sbt.smarthome.rooms.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Region;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jormelcn on 1/05/17.
 *
 */

public abstract class RoomParent extends Room {

    private List<Room> children = new ArrayList<>();
    private RoomParent parent;

    public RoomParent(RoomParameters parameters) {
        super(parameters);
    }

    public void draw(Context context, Canvas canvas, float originLeft, float originTop, float density){
        if(visible) {
            float xi = originLeft + this.left * density;
            float yi = originTop + this.top * density;
            float xf = xi + this.width * density;
            float yf = yi + this.height * density;
            canvas.clipRect(xi, yi, xf, yf, Region.Op.REPLACE);
            onDraw(context, canvas, xi, yi, density);
            for(Room child: children){
                child.draw(context, canvas, xi, yi, density);
            }
        }
    }

    void addChild(Room room){
        children.add(room);
    }

    public Room findRoomById(int id) {
        Room roomSearch = null;
        for (Room child : children) {
            if(id == child.id){
                roomSearch = child;
            }else if(RoomParent.class.isInstance(child)){
                roomSearch = ((RoomParent) child).findRoomById(id);
            }
            if (roomSearch != null)
                break;
        }
        return roomSearch;
    }

    RoomParent getParent() {
        return parent;
    }

    void setParent(RoomParent parent) {
        this.parent = parent;
    }
}
