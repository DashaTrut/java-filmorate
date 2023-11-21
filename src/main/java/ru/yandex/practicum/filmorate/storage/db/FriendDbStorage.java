package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;


@Primary
@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final Logger log = LoggerFactory.getLogger(FriendDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(long userId, long friendId) {
        String sqlQuery = "INSERT INTO FRIEND (USER_ID, FRIEND_ID, FRIENDSHIP_STATUS) VALUES(?, ?, 'false');";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }


    @Override
    public void removeFriend(long userId, long friendId) {
        String sqlQuery = "DELETE FROM FRIEND WHERE USER_ID=? and FRIEND_ID =?";
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    @Override
    public List<User> getFriends(long userId) {
        String sqlQuery = "SELECT  U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY, U.USER_ID FROM Users as U where U.USER_ID in(SELECT F.FRIEND_ID FROM FRIEND AS F WHERE F.USER_ID=?);";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::createUser, userId);
    }

    @Override
    public List<User> getCommonFriends(long uId, long fId) {
        String sqlQuery = "SELECT U.USER_ID, U.EMAIL, U.LOGIN, U.NAME, U.BIRTHDAY" +
                " FROM USERS AS U " +
                "INNER JOIN FRIEND f1 ON u.USER_ID = f1.FRIEND_ID AND f1.USER_ID =?" +
                "INNER JOIN FRIEND f2 ON u.USER_ID = f2.FRIEND_ID AND f2.USER_ID=?;";
        return jdbcTemplate.query(sqlQuery, UserDbStorage::createUser, fId, uId);
    }
}
