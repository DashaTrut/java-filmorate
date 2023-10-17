package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User> {

    @GetMapping
    public List<User> getUsers() {  //    получение списка всех пользователей.
        return getMap();
    }

    @PostMapping //добавление пользователя;
    public User addUser(@Valid @RequestBody User user) {
        log.info("Add user{}", user);
        return create(user);
    }

    @PutMapping //обновление пользователя;
    public User updateUser(@Valid @RequestBody User user) throws EntityNotFoundException {
        log.info("Update user{}", user);
        return update(user);

    }

    @Override
    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}


