package sbt.smarthome.rooms;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import sbt.smarthome.R;
import sbt.smarthome.rooms.core.Room;
import sbt.smarthome.rooms.core.RoomParameters;

/**
 * Created by godi22 on 20/12/16.
 *
 */

public class Fan extends Room {

    public Fan(RoomParameters parameters) {
        super(parameters);

    }

    @Override
    protected void onDraw(Canvas canvas, float originLeft, float originTop, float density) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.fan);
        Matrix matrix = new Matrix();
        matrix.postScale((density*width)/bitmap.getWidth(), (density*height)/bitmap.getHeight());
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        canvas.drawBitmap(bitmap,originLeft, originTop, null);
    }

}
