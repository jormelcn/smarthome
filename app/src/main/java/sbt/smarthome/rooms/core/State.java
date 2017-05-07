package sbt.smarthome.rooms.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jormelcn on 6/05/17.
 *
 */

public class State {

    private Map<String, Object> map = new HashMap<>();

    public State() {

    }

    public Map<String, Object> getMap() {
        return map;
    }

    public State(String key, int value) {
        map.put(key, value);
    }
    public State(String key, boolean value) {
        map.put(key, value);
    }
    public State(String key, float value) {
        map.put(key, value);
    }
    public State(String key, String value) {
        map.put(key, value);
    }


    public void put(String key, int value){
        map.put(key, value);
    }

    public void put(String key, boolean value){
        map.put(key, value);
    }

    public void put(String key, float value){
        map.put(key, value);
    }

    public void put(String key, String value){
        map.put(key, value);
    }


    public int get(String key, int value){
        Object newValue = map.get(key);
        if(newValue != null && newValue instanceof Integer){
            return (Integer) newValue;
        }
        return value;
    }

    public boolean get(String key, boolean value){
        Object newValue = map.get(key);
        if(newValue != null && newValue instanceof Boolean){
            return (Boolean) newValue;
        }
        return  value;
    }

    public float get(String key, float value){
        Object newValue = map.get(key);
        if(newValue != null && newValue instanceof Float){
            return (Float) newValue;
        }
        return  value;
    }

    public String get(String key, String value){
        Object newValue = map.get(key);
        if(newValue != null && newValue instanceof String){
            return (String) newValue;
        }
     return  value;
    }

}
