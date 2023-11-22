package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.FriendDbStorage;
import ru.yandex.practicum.filmorate.storage.db.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        // Подготавливаем данные для теста
        User newUser = User.builder()
                .id(1L)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);

        // вызываем тестируемый метод
        User savedUser = userStorage.getId(newUser.getId());

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindUserAddFriend() {
        // Подготавливаем данные для теста
        User newUser = User.builder()
                .id(1L)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        FriendDbStorage friendStorage = new FriendDbStorage(jdbcTemplate);
        userStorage.create(newUser);
        User userFriend = User.builder()
                .id(2L)
                .email("userFriend@email.ru")
                .login("vanyaFriend123")
                .name("Ivan Friendov")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();
        userStorage.create(userFriend);
        friendStorage.addFriend(newUser.getId(), userFriend.getId());
        // вызываем тестируемый метод
        List<User> savedFriend = friendStorage.getFriends(newUser.getId());

        // проверяем утверждения
        assertThat(savedFriend.get(0))
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(userFriend);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindUserDeleteFriend() {
        // Подготавливаем данные для теста
        User newUser = User.builder()
                .id(1L)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        FriendDbStorage friendStorage = new FriendDbStorage(jdbcTemplate);
        userStorage.create(newUser);
        User userFriend = User.builder()
                .id(2L)
                .email("userFriend@email.ru")
                .login("vanyaFriend123")
                .name("Ivan Friendov")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();
        userStorage.create(userFriend);
        friendStorage.addFriend(newUser.getId(), userFriend.getId());
        friendStorage.removeFriend(newUser.getId(), userFriend.getId());
        // вызываем тестируемый метод
        List<User> savedFriend = friendStorage.getFriends(newUser.getId());

        // проверяем утверждения
        assertThat(savedFriend.size())
                .isEqualTo(0);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindUserCommonFriend() {
        // Подготавливаем данные для теста
        User newUser = User.builder()
                .id(1L)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        FriendDbStorage friendStorage = new FriendDbStorage(jdbcTemplate);
        userStorage.create(newUser);
        User userCommonFriend = User.builder()
                .id(2L)
                .email("CommonFriend@email.ru")
                .login("vanyaCommonFriend123")
                .name("Common Friendov")
                .birthday(LocalDate.of(1985, 1, 1))
                .build();
        userStorage.create(userCommonFriend);
        User userFriend = User.builder()
                .id(2L)
                .email("userFriend@email.ru")
                .login("vanyaFriend123")
                .name("Ivan Friendov")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();
        userStorage.create(userFriend);
        friendStorage.addFriend(newUser.getId(), userCommonFriend.getId());
        friendStorage.addFriend(userFriend.getId(), userCommonFriend.getId());

        // вызываем тестируемый метод
        List<User> savedFriend = friendStorage.getCommonFriends(newUser.getId(), userFriend.getId());

        // проверяем утверждения
        assertThat(savedFriend.get(0))
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(userCommonFriend);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindUserUpdate() {
        // Подготавливаем данные для теста
        User newUser = User.builder()
                .id(1L)
                .email("user@email.ru")
                .login("vanya123")
                .name("Ivan Petrov")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.create(newUser);
        User userUpdate = User.builder()
                .id(newUser.getId())
                .email("userFriend@email.ru")
                .login("vanyaFriend123")
                .name("Ivan Friendov")
                .birthday(LocalDate.of(1995, 1, 1))
                .build();
        userStorage.update(userUpdate);
        // вызываем тестируемый метод
        User saved = userStorage.getId(newUser.getId());

        // проверяем утверждения
        assertThat(saved)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(userUpdate);        // и сохраненного пользователя - совпадают
    }
}

