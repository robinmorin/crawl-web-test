package com.personal.backend.context;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Static context to store attributes that can be accessed from anywhere in the application.
 *
 * */
public final class StaticContext {

    private static final ConcurrentMap<String, Object> attributesHolder = new ConcurrentHashMap<>();

    /**
     * It's a good practice to make the constructor private with throw exception for avoiding instantiation by reflection.
     */
    private StaticContext() {
        throw new UnsupportedOperationException("This context should not be instantiated.");
    }

    public static <T> T getAttribute(String attributeName){
        return (T) attributesHolder.get(attributeName);
    }
    public static Integer getAttributeAsInt(String attributeName){
        return (int) attributesHolder.get(attributeName);
    }
    public static Long getAttributeAsLong(String attributeName){
        return (long) attributesHolder.get(attributeName);
    }
    public static String getAttributeAsString(String attributeName){
        return (String) attributesHolder.get(attributeName);
    }

    public static <T> T setAttribute(String attributeName, T attribute){
        return (T) attributesHolder.putIfAbsent(attributeName, attribute);
    }

    public static void init() {
        attributesHolder.clear();
    }

}
