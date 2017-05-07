package sbt.smarthome.rooms.core;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import sbt.smarthome.rooms.House;

/**
 * Created by jormelcn on 7/12/16.
 *
 */
public class HouseView
        extends View
        implements ScaleGestureDetector.OnScaleGestureListener, GestureDetector.OnGestureListener, HouseServer.SyncListener {

    private House houseRoom;
    private HouseServer houseServer;

    ScaleGestureDetector scaleGestureDetector;
    GestureDetector clickDetector;

    private float prevPointerX;
    private float prevPointerY;
    private int prevPointerId;

    private float scaleFocusX;
    private float scaleFocusY;

    private float translateX;
    private float translateY;

    private float displayCenterX;
    private float displayCenterY;

    private float initScale;
    private int mainRoomLeft;
    private int mainRoomTop;

    private float currentScale = 0;

    Canvas canvas;

    public HouseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
        clickDetector = new GestureDetector(context, this);
    }

    public HouseView(Context context) {
        super(context);
        scaleGestureDetector = new ScaleGestureDetector(context, this);
    }

    public void paintHouse(House houseRoom){
        this.houseRoom = houseRoom;
        houseRoom.setHouseView(this);
    }

    public void connectToServer(HouseServer houseServer){
        this.houseServer = houseServer;
        houseServer.setSyncListener(this);
    }

    protected HouseServer getServer(){
        return houseServer;
    }

    protected void onDraw(Canvas canvas) {
        if(houseRoom == null)
            return;

        if(this.canvas == null || this.canvas != canvas){

            float scaleHorizontal = (float) canvas.getWidth()/ houseRoom.getWidth();
            float scaleVertical = (float) canvas.getHeight()/ houseRoom.getHeight();
            if(scaleHorizontal < scaleVertical){
                initScale = scaleHorizontal;
                mainRoomTop = Math.round ((canvas.getHeight() - houseRoom.getHeight() * initScale)/2);
            }else{
                initScale = scaleVertical;
                mainRoomLeft = Math.round ((canvas.getWidth() - houseRoom.getWidth() * initScale)/2);
            }

            if(currentScale == 0){
                currentScale = initScale;
                translateX = 0;
                translateY = 0;
            }else {
                float zoom = currentScale/initScale;
                if(zoom < 1){
                    currentScale = initScale;
                    zoom = 1;
                }
                translateX = canvas.getWidth()/2f - (displayCenterX * initScale + mainRoomLeft) * zoom;
                translateY = canvas.getHeight()/2f - (displayCenterY * initScale + mainRoomTop) * zoom;
            }

            this.canvas = canvas;
        }

        canvas.save();

        float zoom = currentScale/initScale;
        canvas.scale(zoom, zoom);

        if(translateX > 0){
            translateX = 0;
        }
        else if(translateX < (1 - zoom) * canvas.getWidth()) {
            translateX = (1 - zoom) * canvas.getWidth();
        }

        if(translateY > 0){
            translateY = 0;
        }
        else if(translateY < (1 - zoom) * canvas.getHeight()) {
            translateY = (1 - zoom) * canvas.getHeight();
        }

        canvas.translate(translateX / zoom, translateY / zoom);

        houseRoom.draw(canvas, mainRoomLeft, mainRoomTop, initScale);

        canvas.restore();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(canvas == null){
            return true;
        }

        float currentPointerX = event.getX();
        float currentPointerY = event.getY();
        int currentPointerId = event.getPointerId(event.getActionIndex());

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_MOVE:
                if(currentPointerId == prevPointerId) {
                    translateX += currentPointerX - prevPointerX;
                    translateY += currentPointerY - prevPointerY;
                    invalidate();
                }
                break;
        }

        prevPointerX = currentPointerX;
        prevPointerY = currentPointerY;
        prevPointerId = currentPointerId;

        scaleGestureDetector.onTouchEvent(event);
        clickDetector.onTouchEvent(event);

        return true;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        if(canvas == null){
            return true;
        }

        float prevScale = currentScale;

        currentScale *= detector.getScaleFactor();
        currentScale = Math.max(initScale, Math.min(currentScale, 5.0f));

        float zoom = currentScale/initScale;

        float translateFocusX = scaleFocusX / zoom - translateX;
        float translateFocusY = scaleFocusY / zoom - translateY;

        float scaleChange = currentScale /prevScale;
        translateX += translateFocusX - translateFocusX * scaleChange;
        translateY += translateFocusY - translateFocusY * scaleChange;

        invalidate();
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        scaleFocusX = detector.getFocusX();
        scaleFocusY = detector.getFocusY();
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);

        if(canvas != null) {
            savedState.scale = currentScale;
            float zoom = currentScale/initScale;
            savedState.displayCenterX = ((canvas.getWidth()/2f - translateX)/ zoom - mainRoomLeft)/initScale;
            savedState.displayCenterY = ((canvas.getHeight()/2f - translateY)/ zoom - mainRoomTop)/initScale;
        }
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(!(state instanceof SavedState)){
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        currentScale = savedState.scale;
        displayCenterX = savedState.displayCenterX;
        displayCenterY = savedState.displayCenterY;

        super.onRestoreInstanceState(savedState.getSuperState());

    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        if(canvas != null) {
            float zoom = currentScale/initScale;
            float x = (motionEvent.getX() - translateX)/zoom;
            float y = (motionEvent.getY() - translateY)/zoom;
           houseRoom.click(x, y, mainRoomLeft, mainRoomTop, initScale);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onSync(int id, State state) {
        Room room = houseRoom.findRoomById(id);
        if(room != null){
            room.onSync(state);
        }
    }

    private static class SavedState extends BaseSavedState {

        float scale;
        float displayCenterX;
        float displayCenterY;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            scale = in.readFloat();
            displayCenterX = in.readFloat();
            displayCenterY = in.readFloat();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeFloat(scale);
            out.writeFloat(displayCenterX);
            out.writeFloat(displayCenterY);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>(){

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
    }
}