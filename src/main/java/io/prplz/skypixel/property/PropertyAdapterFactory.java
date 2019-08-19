package io.prplz.skypixel.property;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Flattens {@link Property} to its inner value
 */
public class PropertyAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (typeToken.getRawType() != Property.class) {
            return null;
        }
        Type type = typeToken.getType();
        Type valueType = ((ParameterizedType) type).getActualTypeArguments()[0];
        TypeAdapter<?> valueAdapter = gson.getAdapter(TypeToken.get(valueType));
        return (TypeAdapter<T>) newAdapter(valueAdapter);
    }

    private <T> TypeAdapter<Property<T>> newAdapter(TypeAdapter<T> valueAdapter) {
        return new TypeAdapter<Property<T>>() {
            @Override
            public void write(JsonWriter out, Property<T> value) throws IOException {
                valueAdapter.write(out, value.get());
            }

            @Override
            public Property<T> read(JsonReader in) throws IOException {
                return new Property<>(valueAdapter.read(in));
            }
        };
    }
}
