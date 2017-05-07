package sbt.smarthome.rooms.core;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Region;
import android.util.Log;

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
    private HouseView houseView;
    protected float left = 0;
    protected float top = 0;
    protected float width = 0;
    protected float height = 0;
    boolean visible;
    String name;


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

    protected void refresh(){
        houseView.invalidate();
    }

    void setHouseView(HouseView houseView){
        this.houseView = houseView;
    }

    public void draw(Canvas canvas, float originLeft, float originTop, float density){
        if(visible) {
            float xi = originLeft + left * density;
            float yi = originTop + top * density;
            float xf = xi + width * density;
            float yf = yi + height * density;
            canvas.clipRect(xi, yi, xf, yf, Region.Op.REPLACE);
            onDraw(canvas, xi, yi, density);
        }
    }

    protected abstract void onDraw(Canvas canvas, float originLeft, float originTop, float density);

    protected boolean click(float x, float y, float originLeft, float originTop, float density){
        if(visible){
            if(
                    (x > originLeft) &&
                    (x < originLeft + width * density) &&
                    (y > originTop) &&
                    (y < originTop + height * density)
            ){
                Log.i("ROOM_INFORMER", "OnClick: " + name);
                onClick();
                return true;
            }
        }
        return false;
    }

    protected void onClick(){

    }

    protected final void sync(State state){
        houseView.getServer().syncDevice(id, state);
    }

    protected void onSync(State state){

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    protected float getWidth() {
        return width;
    }

    protected float getHeight() {
        return height;
    }

    public Context getContext(){
        return houseView.getContext();
    }

}
