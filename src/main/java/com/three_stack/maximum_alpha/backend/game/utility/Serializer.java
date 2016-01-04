package com.three_stack.maximum_alpha.backend.game.utility;

import com.google.gson.Gson;

public class Serializer {
    private static Gson gson;
    static {
        gson = new Gson();
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }
}
