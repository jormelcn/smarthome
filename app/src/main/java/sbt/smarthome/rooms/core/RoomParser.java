package sbt.smarthome.rooms.core;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.InputStream;
import java.lang.reflect.Constructor;

/**
 * Created by jormelcn on 4/11/16.
 *
 */

public class RoomParser {

    private static final String ROOM_PACKAGE = "sbt.smarthome.rooms";
    private static final String ROOM_PARSER = "ROOM PARSER";


    public RoomParent parse(InputStream input) {
        Room currentRoom = null;
        RoomParent mainRoom = null;
        RoomParent currentParent = null;
        RoomParameters currentParentParameters = null;
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
                            clazz = Class.forName(ROOM_PACKAGE + "." + tagName);
                        }catch (Exception e){
                            Log.e(ROOM_PARSER, "invalid room class");
                            return null;
                        }
                        Constructor<?> constructor = clazz.getConstructor(RoomParameters.class);
                        RoomParameters parameters = new RoomParameters(currentParentParameters);

                        int count = parser.getAttributeCount();
                        for (int i = 0; i < count; i++) {
                            String field = parser.getAttributeName(i);
                            String value = parser.getAttributeValue(i);
                            parameters.set(field, value);
                        }
                        try {
                            Room room = (Room) constructor.newInstance(parameters);
                            currentRoom = room;
                            if(currentParent != null){
                                currentParent.addChild(room);
                                if(RoomParent.class.isInstance(room)){
                                    ((RoomParent) room).setParent(currentParent);
                                    currentParent = (RoomParent) room;
                                    currentParentParameters = parameters;
                                }
                            }else {
                                if(RoomParent.class.isInstance(room)){
                                    currentParent = (RoomParent) room;
                                    currentParentParameters = parameters;
                                    if(mainRoom == null) {
                                        mainRoom = (RoomParent) room;
                                    }else {
                                        Log.e(ROOM_PARSER, "error, should one Main Room");
                                        return null;
                                    }
                                }else {
                                    Log.e(ROOM_PARSER, "error, main Room should be RoomParent");
                                    return null;
                                }
                            }
                        }catch (Exception e){
                            Log.e(ROOM_PARSER, "error to create room");
                            return null;
                        }

                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        if(RoomParent.class.isInstance(currentRoom)){
                            if(currentParent == null){
                                Log.e(ROOM_PARSER, "xml syntax error ");
                                return null;
                            }else {
                                currentParent = currentParent.getParent();
                                currentParentParameters = currentParentParameters.getParentParameters();
                            }
                        }else {
                            currentRoom = currentParent;
                        }
                        break;
                }
                eventType = parser.next();
            }

        }catch (Exception e){
            Log.e(ROOM_PARSER, "Error syntax error ");
            return null;
        }
        return mainRoom;
    }
}
