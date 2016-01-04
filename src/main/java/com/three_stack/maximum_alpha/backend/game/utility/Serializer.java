package com.three_stack.maximum_alpha.backend.game.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Serializer {
    private static Gson gson;

    static {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(new TitleCaseEnumTypeAdapterFactory());
        gson = gsonBuilder.create();
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}
