package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Qualifier
@Primary
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    private long generatedID = 1;

    @Override
    public User create(User u) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES(?, ?, ?, ?);";
        u.setId(generatedID++);
        jdbcTemplate.update(sqlQuery, u.getEmail(), u.getLogin(), u.getName(), Date.valueOf(u.getBirthday()));
        if (getId(u.getId()) == null) {
            throw new EntityNotFoundException(String.format("Добавление не произошло %s ", u));
        }
        log.info("Пользователь с идентификатором {} добавлен.", u.getId());
        return u;
    }

    @Override
    public User update(User u) {
        if (getId(u.getId()) == null) {
            throw new EntityNotFoundException(String.format("Обновление невозможно %s не сущесвует", u));
        }
        String sqlQuery = "UPDATE USERS SET EMAIL=?, LOGIN=?, NAME=?, BIRTHDAY=? WHERE USER_ID=?;";
        jdbcTemplate.update(sqlQuery, u.getEmail(), u.getLogin(), u.getName(), u.getBirthday(), u.getId());
        return getId(u.getId());
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::createUser);
    }

    static User createUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("USER_ID"))
                .email(rs.getString("EMAIL"))
                .login(rs.getString("LOGIN"))
                .name(rs.getString("NAME"))
                .birthday((rs.getDate("BIRTHDAY")).toLocalDate())
                .build();
    }

    @Override
    public User getId(Long id) {
        String sqlQuery = "select * from USERS where USER_ID=?;";
        List<User> users = jdbcTemplate.query(sqlQuery, UserDbStorage::createUser, id);
        if (users.size() != 1) {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new EntityNotFoundException("Пользователь с идентификатором id не найден.");
        }
        log.info("Найден пользователь: {} {}", users.get(0).getId(), users.get(0).getLogin());
        return users.get(0);
    }

    @Override
    public boolean delete(User user) {
        String sqlQuery = "DELETE FROM USERS WHERE USER_ID=?;";
        return jdbcTemplate.update(sqlQuery, user.getId()) > 0;
    }
}