package ru.otus.erinary.serializer;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.entity.Person;

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
        assertEquals(person, gson.fromJson(json, Person.class));
    }
}