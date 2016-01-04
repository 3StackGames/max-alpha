package com.three_stack.maximum_alpha.backend.game.utilities;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TitleCaseEnumTypeAdapterFactory implements TypeAdapterFactory {
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<T> rawType = (Class<T>) type.getRawType();
        if (!rawType.isEnum()) {
            return null;
        }

        final Map<String, T> titleCaseToConstant = new HashMap<>();
        for (T constant : rawType.getEnumConstants()) {
            titleCaseToConstant.put(toTitleCase(constant), constant);
        }

        return new TypeAdapter<T>() {
            public void write(JsonWriter out, T value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(toTitleCase(value));
                }
            }

            public T read(JsonReader reader) throws IOException {
                if (reader.peek() == JsonToken.NULL) {
                    reader.nextNull();
                    return null;
                } else {
                    return titleCaseToConstant.get(reader.nextString());
                }
            }
        };
    }

    private String toTitleCase(Object o) {
        String noUnderscore = o.toString().replace('_', ' ');
        String capitalized = StringUtils.capitalize(noUnderscore.toLowerCase());
        return capitalized;
    }
}