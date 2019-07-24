package ru.otus.erinary.serializer;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import ru.otus.erinary.entity.Person;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MySerializerTest {

    @Test
    void testToJson() {
        Gson gson = new Gson();
        MySerializer serializer = new MySerializer();

        String[] hobbies = {"travel", "board games"};
        Person person = new Person(
                "Jack",
                25,
                "London",
                List.of("Cat Martin", "Parrot Duke"),
                hobbies,
                false
        );

        String json = serializer.toJson(person);
        System.out.println(json);
        assertEquals(person, gson.fromJson(json, Person.class));
    }
}