package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController extends BaseController<User> {

    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {  //    получение списка всех пользователей.
        return userService.getAll();
    }

    @PostMapping //добавление пользователя;
    public User addUser(@Valid @RequestBody User user) {
        log.info("Add user{}", user);
        validate(user);
        return userService.create(user);
    }


    @PutMapping //обновление пользователя;
    public User updateUser(@Valid @RequestBody User user) throws EntityNotFoundException {
        log.info("Update user{}", user);
        validate(user);
        return userService.update(user);
    }

    @GetMapping("{id}")
    public User getForId(@PathVariable Long id) {
        log.info("Get user id{}", id);
        return userService.getId(id);
    }

    @Override
    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    @GetMapping("{id}/friends") // возвращаем список пользователей, являющихся его друзьями.
    public List<User> allFriendsForId(@PathVariable Long id) {
        log.info("Friends user{}", id);
        return userService.allFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> commonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Common friends user{}", id);
        return userService.commonFriend(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Add friends{}", friendId);
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Delete friend{}", friendId);
        return userService.deleteFriends(id, friendId);
    }
}


