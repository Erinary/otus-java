package ru.otus.erinary.spring.controllers;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.erinary.model.User;
import ru.otus.erinary.orm.DBService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private Gson gson;
    private DBService<User> userDBService;

    @PostMapping
    public void createUser(User user) {
        userDBService.create(user);
    }

    @GetMapping
    public String getUsers() {
        List<User> users = userDBService.loadAll();
        return gson.toJson(users);
    }
}
