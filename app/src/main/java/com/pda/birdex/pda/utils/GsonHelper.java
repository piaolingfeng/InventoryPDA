package com.pda.birdex.pda.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chuming.zhuang on 2016/4/6.
 */
public class GsonHelper {
    public static <T> T getPerson(String jsonString, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return t;
    }

//    public static <T> List<T> getPersons(String jsonString, final Class<T> cls) {
//        List<T> list = new ArrayList<T>();
//        try {
//            Gson gson = new Gson();
//            list = gson.fromJson(jsonString, new TypeToken<List<cls>>() {
//            }.getType());
//        } catch (Exception e) {
//        }
//        return list;
//    }

    public static List<Map<String, Object>> listKeyMaps(String jsonString) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonString,
                    new TypeToken<List<Map<String, Object>>>() {
                    }.getType());
        } catch (Exception e) {
            // TODO: handle exception
        }
        return list;
    }

    public static String createJsonString(Object value)
    {
        Gson gson = new Gson();
        String str = gson.toJson(value);
        return str;
    }
}
