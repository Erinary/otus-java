package ru.otus.erinary.serializer;

import ru.otus.erinary.entity.Person;

import javax.json.*;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

class MySerializer {

    private final static String STATIC = "static";
    private final static String TRANSIENT = "transient";

    String toJson(Person person) {
        JsonObject jsonObject = handleClass(person);

        StringWriter strWriter = new StringWriter();
        try (JsonWriter jsonWriter = Json.createWriter(strWriter)) {
            jsonWriter.writeObject(jsonObject);
        }
        return strWriter.getBuffer().toString();
    }

    private JsonObject handleClass(Person person) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Field[] fields = person.getClass().getDeclaredFields();
        for (Field field : fields) {
            String modifiers = Modifier.toString(field.getModifiers());
            if (!modifiers.contains(STATIC) && !modifiers.contains(TRANSIENT)) {
                try {
                    field.setAccessible(true);
                    if (Collection.class.isAssignableFrom(field.getType())) {
                        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                        Collection collection = (Collection) field.get(person);
                        for (Object o : collection) {
                            arrayBuilder.add(o.toString());
                        }
                        builder.add(field.getName(), arrayBuilder);
                    } else if (field.getType().isArray()) {
                        //TODO массивы
                    } else {
                        builder.add(field.getName(), field.get(person).toString());
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

}
