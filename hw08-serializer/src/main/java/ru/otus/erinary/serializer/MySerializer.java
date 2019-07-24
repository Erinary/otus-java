package ru.otus.erinary.serializer;

import ru.otus.erinary.entity.Person;

import javax.json.*;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

class MySerializer {

    private final static String STATIC = "static";
    private final static String TRANSIENT = "transient";

    String toJson(Person person) {
        JsonObject jsonObject = prepareJsonObject(person);

        StringWriter strWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(strWriter)) {
            jsonWriter.writeObject(jsonObject);
        }
        return strWriter.getBuffer().toString();
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
