package com.doors.myplacetourist.rest.pojo;

import com.doors.myplacetourist.common.Tools;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

public final class EmptyStringAsNullTypeAdapter<T>
        implements JsonDeserializer<T> {
    private static final String TAG = "EmptyStringAsNullTypeAdapterTag";

    // Let Gson instantiate it itself
    private EmptyStringAsNullTypeAdapter() {
    }

    @Override
    public T deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext context)
            throws JsonParseException {
        if ( jsonElement.isJsonPrimitive() ) {
            Tools.log(TAG,"deserialize","jsonElement.isJsonPrimitive()=true");
            final JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            if ( jsonPrimitive.isString()) {
                Tools.log(TAG,"deserialize","jsonPrimitive.isString()=true");
                if(jsonPrimitive.getAsString().isEmpty()){
                    Tools.log(TAG,"deserialize","jsonPrimitive.getAsString().isEmpty()=true");
                    return null;
                }
                Tools.log(TAG,"deserialize","jsonPrimitive.getAsString().isEmpty()=false");
            }
            Tools.log(TAG,"deserialize","jsonPrimitive.isString()=false");
        }
        Tools.log(TAG,"deserialize","jsonElement.isJsonPrimitive()=false");
        return context.deserialize(jsonElement, type);
    }

}