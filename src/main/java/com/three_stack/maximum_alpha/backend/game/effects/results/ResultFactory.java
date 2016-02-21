package com.three_stack.maximum_alpha.backend.game.effects.results;

import com.three_stack.maximum_alpha.database_client.pojos.DBResult;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;

public class ResultFactory {
    public static Result create(DBResult dbResult) {
        String className = StringUtils.capitalize(dbResult.getType().replace("_", ""))+"Result";
        try {
            Class<?> resultClass = Class.forName(className);
            if(!Result.class.isAssignableFrom(resultClass)) {
                throw new IllegalArgumentException("Class found isn't a result");
            }
            Constructor<?> constructor = resultClass.getConstructor(String.class);
            Result result = (Result) constructor.newInstance(dbResult);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //fallback in case of failure
        return new EmptyResult(dbResult);
    }
}
