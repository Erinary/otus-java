package ru.otus.erinary.spring.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.erinary.model.User;
import ru.otus.erinary.orm.DBService;
import ru.otus.erinary.spring.model.ApiUser;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final DBService<User> userDBService;

    @PostMapping("/users")
    public void createUser(@RequestBody ApiUser apiUser) {
        try {
            log.info("Got request for user creating. New user: {}", apiUser);
            userDBService.create(apiUser.toUser());
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/users")
    public List<ApiUser> getUsers() {
        List<ApiUser> result;
        try {
            log.info("Got request for user loading");
            List<User> users = userDBService.loadAll();
            result = users.stream().map(ApiUser::new).collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw e;
        }
        log.info("Users to load: {}", result.size());
        return result;
    }
}
