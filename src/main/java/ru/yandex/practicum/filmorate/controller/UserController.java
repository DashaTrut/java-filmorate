package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User> {

    @GetMapping
    public List<User> getUsers() {  //    получение списка всех пользователей.
        return super.getMap();
    }

    @PostMapping //добавление пользователя;
    public User addUser(@Valid @RequestBody User user) {
        log.info("Add user{}", user);
        return super.create(user);
    }

    @PutMapping //обновление пользователя;
    public User updateUser(@Valid @RequestBody User user) throws EntityNotFoundException {
        log.info("Update user{}", user);
        return super.update(user);

    }
@Override
    public void validate(User user) {
    if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
        user.setName(user.getLogin());
    }
    }
}


