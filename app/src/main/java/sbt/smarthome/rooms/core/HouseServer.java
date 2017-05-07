package sbt.smarthome.rooms.core;


import android.util.Log;

/**
 * Created by jormelcn on 6/05/17.
 *
 */

public class HouseServer {

    SyncListener listener;


    void syncDevice(int id, State state){
        Log.i("ROOM_SYNC","id: " + String.valueOf(id) + ", " + state.getMap().toString());
    }

    void setSyncListener(SyncListener listener){
        this.listener = listener;
    }

    public interface SyncListener {
        void onSync(int id, State state);
    }

}
