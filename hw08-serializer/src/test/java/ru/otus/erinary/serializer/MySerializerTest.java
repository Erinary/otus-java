package ru.otus.erinary.serializer;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.entity.Person;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MySerializerTest {

    @Test
    void testToJson() {
        Gson gson = new Gson();
        MySerializer serializer = new MySerializer();

        Person person = new Person(
                "Jack",
                25,
                "London",
                List.of("Cat Martin", "Parrot Duke"),
                new String[]{"travel", "board games"},
                new int[]{1, 2, 3},
                false
        );

        String json = serializer.toJson(person);
        System.out.println(json);
        assertEquals(gson.fromJson(json, Person.class), person);
    }

    @Test
    void testDifferentTypesToJson() {
        Gson gson = new Gson();
        MySerializer serializer = new MySerializer();

        assertEquals(gson.toJson(null), serializer.toJson(null));
        assertEquals(gson.toJson((byte) 1), serializer.toJson((byte) 1));
        assertEquals(gson.toJson((short) 1), serializer.toJson((short) 1));
        assertEquals(gson.toJson(1), serializer.toJson(1));
        assertEquals(gson.toJson(1L), serializer.toJson(1L));
        assertEquals(gson.toJson(1f), serializer.toJson(1f));
        assertEquals(gson.toJson(1d), serializer.toJson(1d));
        assertEquals(gson.toJson("aaa"), serializer.toJson("aaa"));
        assertEquals(gson.toJson('a'), serializer.toJson('a'));
        assertEquals(gson.toJson(true), serializer.toJson(true));
        assertEquals(gson.toJson(new int[]{1, 2, 3}), serializer.toJson(new int[]{1, 2, 3}));
        assertEquals(gson.toJson(List.of(1, 2, 3)), serializer.toJson(List.of(1, 2, 3)));
        assertEquals(gson.toJson(Collections.singletonList(1)), serializer.toJson(Collections.singletonList(1)));
    }
}