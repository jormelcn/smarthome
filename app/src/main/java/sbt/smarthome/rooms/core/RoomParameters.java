package sbt.smarthome.rooms.core;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by jormelcn on 1/05/17.
 *
 */

public class RoomParameters {

    private Map<String, String> parameters = new HashMap<>();
    private RoomParameters parentParameters;

    RoomParameters(RoomParameters parentParameters) {
        this.parentParameters = parentParameters;
    }

    void set(String field, String value){
        parameters.put(field, value);
    }

    boolean getBoolean(String field, boolean def){
        if(field.startsWith("parent.")){
            return parentParameters.getBoolean(field.substring(7), def);
        }
        String value = parameters.get(field);
        if(value == null){
            return def;
        }
        try {
            return Boolean.parseBoolean(value);
        }catch (Exception e){
            return def;
        }
    }

    public int getInt(String field, int def){
        if(field.startsWith("parent.")){
            return parentParameters.getInt(field.substring(7), def);
        }
        String value = parameters.get(field);
        try {
            return Integer.parseInt(value);
        }catch (Exception e){
            return def;
        }
    }

    public float getFloat(String field, float def){
        if(field.startsWith("parent.")){
            return parentParameters.getFloat(field.substring(7), def);
        }
        String value = parameters.get(field);
        try {
            return Float.parseFloat(value);
        }catch (Exception e){
            return def;
        }
    }

    public int getColor(String field, int def){
        if(field.startsWith("parent.")){
            return parentParameters.getColor(field.substring(7), def);
        }
        String value = parameters.get(field);
        try {
            return Color.parseColor(value);
        }catch (Exception e){
            return def;
        }
    }

    String getString(String field, String def){
        if(field.startsWith("parent.")){
            return parentParameters.getString(field.substring(7), def);
        }
        String value = parameters.get(field);
        if(value == null){
            return  def;
        }
        return value;
    }

    RoomParameters getParentParameters(){
        return parentParameters;
    }

}
