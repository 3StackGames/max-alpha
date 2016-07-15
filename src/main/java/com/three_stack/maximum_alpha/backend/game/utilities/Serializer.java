package com.three_stack.maximum_alpha.backend.game.utilities;

import io.gsonfire.GsonFireBuilder;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.three_stack.maximum_alpha.backend.game.cards.Card;

public class Serializer {
    private static Gson gson;
    private static Gson gsonCard;

    static {
        GsonFireBuilder fireBuilder = new GsonFireBuilder();
        fireBuilder.enableExposeMethodResult();

        GsonBuilder gsonBuilder = fireBuilder.createGsonBuilder();
        gson = gsonBuilder.create();
        
        gsonBuilder.setExclusionStrategies(new CardExclStrat());
        gsonCard = gsonBuilder.create();
    }

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static Object fromJson(String json, Class aClass) {
        return gson.fromJson(json, aClass);
    }
    
    public static String toJsonCard(Object object) {
    	return gsonCard.toJson(object);
    }
    
    /**
     * Converts cards into classes which only hold their IDs.
     */
    public static class CardExclStrat implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {

            return (Card.class.isAssignableFrom(f.getDeclaringClass()) && !f.getName().equals("id"));
        }

    }
}
