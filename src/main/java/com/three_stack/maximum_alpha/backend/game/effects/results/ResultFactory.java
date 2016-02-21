package com.three_stack.maximum_alpha.backend.game.effects.results;

import com.three_stack.maximum_alpha.database_client.pojos.DBResult;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;

public class ResultFactory {
    public static Result create(DBResult dbResult) {
        String className = getResultClassName(dbResult.getType());
        try {
            Class<?> resultClass = Class.forName(className);
            if(!Result.class.isAssignableFrom(resultClass)) {
                throw new IllegalArgumentException("Class found isn't a result");
            }
            Constructor<?> constructor = resultClass.getConstructor(DBResult.class);

            return (Result) constructor.newInstance(dbResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //fallback in case of failure
        return new EmptyResult(dbResult);
    }

    public static String getResultClassName(String type) {
        String[] splitTypeWords = type.split("_");
        StringBuilder stringBuilder = new StringBuilder("com.three_stack.maximum_alpha.backend.game.effects.results.");
        for(String splitTypeWord : splitTypeWords) {
            stringBuilder.append(StringUtils.capitalize(splitTypeWord.toLowerCase()));
        }
        stringBuilder.append("Result");
        return stringBuilder.toString();
    }
}
