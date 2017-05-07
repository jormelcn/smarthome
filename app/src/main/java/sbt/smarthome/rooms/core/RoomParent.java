package sbt.smarthome.rooms.core;

import android.graphics.Canvas;
import android.graphics.Region;
import android.util.Log;

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

    @Override
    protected void setHouseView(HouseView houseView) {
        super.setHouseView(houseView);
        for(Room child: children){
            child.setHouseView(houseView);
        }
    }

    @Override
    public void draw(Canvas canvas, float originLeft, float originTop, float density){
        if(visible) {
            float xi = originLeft + this.left * density;
            float yi = originTop + this.top * density;
            float xf = xi + this.width * density;
            float yf = yi + this.height * density;
            canvas.clipRect(xi, yi, xf, yf, Region.Op.REPLACE);
            onDraw(canvas, xi, yi, density);
            for(Room child: children){
                child.draw(canvas, xi, yi, density);
            }
        }
    }

    @Override
    public boolean click(float x, float y, float originLeft, float originTop, float density){
        if(visible) {
            if (
                    (x > originLeft) &&
                    (x < originLeft + width * density) &&
                    (y > originTop) &&
                    (y < originTop + height * density)
            ) {
                for (Room child : children) {
                    float childLeft = originLeft + child.left * density;
                    float childTop = originTop + child.top * density;
                    if (child.click(x, y, childLeft, childTop, density)) {
                        return true;
                    }
                }
                Log.i("ROOM_INFORMER", "OnClick: " + name);
                onClick();
                return true;
            }
        }
       return false;
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
