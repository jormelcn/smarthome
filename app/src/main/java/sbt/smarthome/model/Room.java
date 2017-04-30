package sbt.smarthome.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jormelcn on 4/11/16.
 *
 */
public class Room {

    private int id;
    private float left = -1;
    private float top = -1;
    private float width = -1;
    private float height = -1;
    private float xi,yi,xf,yf;
    private int backgroundColor;
    private String name;

    private Room parent;
    private List<Room> children;
    Paint paint;
    float r=0;

    Room() {
        children = new ArrayList<>();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        //paint.setAntiAlias(true);
        paint.setStrokeWidth(30);
        paint.setTextSize(20);
        paint.setColor(Color.BLUE);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Room) && ((Room) obj).id == id;
    }

    public void addChild(Room room){
        if(room.left == -1){
            room.left = 0;
        }
        if(room.top == -1){
            room.top = 0;
        }
        if(room.width == -1){
            room.width = this.width;
        }
        if(room.height == -1){
            room.height = this.height;
        }
        room.parent = this;
        children.add(room);
    }

    public void draw(Context context, Canvas canvas, float left, float top, float ratio){
        this.xi = left + this.left * ratio;
        this.yi = top + this.top * ratio;
        this.xf = this.xi + this.width * ratio;
        this.yf = this.yi + this.height * ratio;

        canvas.clipRect(this.xi, this.yi, this.xf,this.yf, Region.Op.REPLACE);

        onDraw(context, canvas, this.xi, this.yi, this.xf,this.yf, ratio);

        canvas.clipRect(this.xi, this.yi, this.xf, this.yf, Region.Op.REPLACE);

        onDraw(context, canvas, this.xi, this.yi, ratio);

        for(Room child : children){
            child.draw(context, canvas, this.xi, this.yi, ratio);
        }
    }

    protected void onDraw(Context context,Canvas canvas, float left, float top,float right, float bot, float ratio){
        canvas.drawColor(backgroundColor);
        //Log.d("STATE","left="+left+"top="+top+"right="+right+"bot="+bot+"ID="+this.id);

    }

    public Room findRoomById(int IdSearch) {
        Room roomSearch = null;
        if (this.id == IdSearch) {
            roomSearch = this;
        } else {
            for (Room child : children) {
                roomSearch = child.findRoomById(IdSearch);
                if (roomSearch != null)
                    break;
            }
        }
        return roomSearch;
    }

    public Room findRoomDim(float x, float y){

        return this;
    }

    void onDraw(Context context, Canvas canvas, float left, float top, float ratio){
        canvas.drawColor(backgroundColor);

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getLeft() {
        return left;
    }

    public float getTop() {
        return top;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Room getParent(){
        return parent;
    }

    public List<Room> getChildren() {
        return children;
    }

}
