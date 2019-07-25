package ru.otus.erinary.serializer;

import org.apache.commons.lang3.ClassUtils;

import javax.json.*;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

class MySerializer {

    private final static String STATIC = "static";
    private final static String TRANSIENT = "transient";

    String toJson(Object object) {
        JsonValue jsonObject = prepareJsonValue(object);
        StringWriter strWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(strWriter)) {
            jsonWriter.write(jsonObject);
        }
        return strWriter.getBuffer().toString();
    }

    private JsonValue prepareJsonValue(Object object) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        if (object == null) {
            return JsonValue.NULL;
        } else if (ClassUtils.isPrimitiveOrWrapper(object.getClass()) || object instanceof String) {
            return handlePrimitivesAndString(object);
        } else if (Collection.class.isAssignableFrom(object.getClass())) {
            for (Object element : (Collection) object) {
                arrayBuilder.add(handlePrimitivesAndString(element));
            }
            return arrayBuilder.build();
        } else if (object.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(object); ++i) {
                arrayBuilder.add(handlePrimitivesAndString(Array.get(object, i)));
            }
            return arrayBuilder.build();
        } else {
            return prepareJsonObject(object);
        }
    }

    private JsonValue handlePrimitivesAndString(Object object) {
        if (object instanceof Integer) {
            return Json.createValue((int) object);
        } else if (object instanceof Short) {
            return Json.createValue((short) object);
        } else if (object instanceof Byte) {
            return Json.createValue((byte) object);
        } else if (object instanceof Long) {
            return Json.createValue((long) object);
        } else if (object instanceof Double) {
            return Json.createValue((double) object);
        } else if (object instanceof Float) {
            return Json.createValue((float) object);
        } else if (object instanceof Boolean) {
            if ((Boolean) object) {
                return JsonValue.TRUE;
            } else {
                return JsonValue.FALSE;
            }
        } else {
            return Json.createValue(object.toString());
        }
    }

    private JsonObject prepareJsonObject(Object object) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            String modifiers = Modifier.toString(field.getModifiers());
            if (!modifiers.contains(STATIC) && !modifiers.contains(TRANSIENT)) {
                try {
                    field.setAccessible(true);
                    if (Collection.class.isAssignableFrom(field.getType())) {
                        builder.add(field.getName(), handleCollection(object, field));
                    } else if (field.getType().isArray()) {
                        builder.add(field.getName(), handleArray(object, field));
                    } else {
                        builder.add(field.getName(), field.get(object).toString());
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } finally {
                    field.setAccessible(false);
                }
            }
        }
        return builder.build();
    }

    private JsonArrayBuilder handleCollection(Object object, Field field) throws IllegalAccessException {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        Collection collection = (Collection) field.get(object);
        for (Object o : collection) {
            arrayBuilder.add(o.toString());
        }
        return arrayBuilder;
    }

    private JsonArrayBuilder handleArray(Object object, Field field) throws IllegalAccessException {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        Object array = field.get(object);
        for (int i = 0; i < Array.getLength(array); ++i) {
            arrayBuilder.add(Array.get(array, i).toString());
        }
        return arrayBuilder;
    }

}
