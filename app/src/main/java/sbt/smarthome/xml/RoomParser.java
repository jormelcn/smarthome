package sbt.smarthome.xml;

import android.graphics.Color;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import sbt.smarthome.model.Room;

/**
 * Created by jormelcn on 4/11/16.
 *
 */

public class RoomParser {

    private enum Type {INT, LONG, DOUBLE, STRING, BOOLEAN, FLOAT, UNKNOWN}
    private static Map<String, Map<String, Type>> typesParsingCache = new HashMap<>();

    public RoomParser() {
        typesParsingCache = new HashMap<>();
    }

    private Map<String, Type> parseTypesForFields(Field[] fields, Class<?> clazz){
        String className = clazz.getSimpleName();

        Map<String, Type> types = typesParsingCache.get(className);
        if(types != null){
            return types;
        }
        types = new HashMap<>();
        for(Field field: fields){
            String type = field.getType().getSimpleName();
            String fieldName = field.getName();
            switch (type){
                case "int":
                case "Integer":
                    types.put(fieldName,Type.INT);
                    break;
                case "long":
                case "Long":
                    types.put(fieldName, Type.LONG);
                    break;
                case "double":
                case "Double":
                    types.put(fieldName, Type.DOUBLE);
                    break;
                case "float":
                case "Float":
                    types.put(fieldName, Type.FLOAT);
                    break;
                case "String":
                    types.put(fieldName, Type.STRING);
                    break;
                case "boolean":
                case "Boolean":
                    types.put(fieldName, Type.BOOLEAN);
                    break;
                default:
                    types.put(fieldName, Type.UNKNOWN);
            }
        }
        typesParsingCache.put(className, types);
        return types;
    }


    public Room parse(InputStream input) {
        Room mainRoom = null;
        Room currentRoom = null;
        XmlPullParserFactory factory;
        XmlPullParser parser;

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            parser.setInput(input, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tagName = parser.getName();
                        Class<?> clazz;
                        try {
                            clazz = Class.forName("sbt.smarthome.model." + tagName);
                        }catch (Exception e){
                            //
                            return null;
                        }
                        Constructor<?> constructor = clazz.getConstructor();
                        Room room;
                        try {
                            room = (Room) constructor.newInstance();
                        }catch (Exception e){
                            //
                            return null;
                        }

                        while(clazz.getSuperclass()!=null){ // we don't want to process Object.class

                            Field[] fields = clazz.getDeclaredFields();
                            Map<String, Type> types = parseTypesForFields(fields, clazz);

                            int count = parser.getAttributeCount();
                            for (int i = 0; i < count; i++) {
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                Type type = types.get(name);
                                Field field;

                                try {
                                    field = clazz.getDeclaredField(name);
                                }catch (Exception e){
                                    //
                                    continue;
                                }

                                field.setAccessible(true);
                                try {
                                    switch (type) {
                                        case INT:
                                            if (value.startsWith("#"))
                                                field.setInt(room, Color.parseColor(value));
                                            else
                                                field.setInt(room, Integer.parseInt(value));
                                            if(value.startsWith("#")){
                                                field.setInt(room, Color.parseColor(value));

                                            }else {
                                                field.setInt(room, Integer.parseInt(value));
                                            }
                                            break;
                                        case LONG:
                                            field.setLong(room, Long.parseLong(value));
                                            break;
                                        case STRING:
                                            field.set(room, value);
                                            break;
                                        case DOUBLE:
                                            field.setDouble(room, Double.parseDouble(value));
                                            break;
                                        case FLOAT:
                                            field.setFloat(room, Float.parseFloat(value));
                                            break;
                                        case BOOLEAN:
                                            field.setBoolean(room, Boolean.parseBoolean(value));
                                            break;
                                        default:
                                    }
                                }catch (Exception e){
                                    //
                                    e.printStackTrace();
                                }
                            }

                            clazz = clazz.getSuperclass();
                        }

                        if (currentRoom == null) {
                            currentRoom = room;
                            mainRoom = currentRoom;
                        } else {
                            currentRoom.addChild(room);
                            currentRoom = room;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        if(currentRoom == null){
                            return null;
                        }
                        currentRoom = currentRoom.getParent();
                        break;
                }
                eventType = parser.next();
            }

        }catch (Exception e){
            //
            return null;
        }
        return mainRoom;
    }
}
