package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryBaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service

public class UserService extends AbstractService<User> {

    public UserService(InMemoryBaseStorage<User> inMemoryBaseStorage) {
        super(inMemoryBaseStorage);
    }

    public User addFriends(long idUser1, long idUser2) {
        User user1 = getId(idUser1);
        User user2 = getId(idUser2);
        Set<Long> list = user1.getFriends();
        list.add(idUser2);
        user1.setFriends(list);
        Set<Long> list2 = user2.getFriends();
        list2.add(idUser1);
        user2.setFriends(list2);
        return user2;
    }

    public User deleteFriends(long idUser1, long idUser2) {
        User user1 = getId(idUser1);
        User user2 = getId(idUser2);
        Set<Long> list1 = user1.getFriends();
        if (!list1.contains(idUser2)) {
            throw new EntityNotFoundException("Этого пользователя нет в друзьях");
        }
        list1.remove(idUser2);
        user1.setFriends(list1);
        Set<Long> list2 = user2.getFriends();
        list2.remove(idUser1);
        user2.setFriends(list2);
        return user2;
    }

    public List<User> commonFriend(long idUser1, long idUser2) {
        User user1 = getId(idUser1);
        User user2 = getId(idUser2);
        List<User> list1 = allFriends(idUser1);
        List<User> longList = new ArrayList<>();
        if (list1.isEmpty()) {
            return longList;
        }
        List<User> list2 = allFriends(idUser2);
        if (list2.isEmpty()) {
            return longList;
        }
        list1.retainAll(list2);
        longList.addAll(list1);
        return longList;
    }

    public List<User> allFriends(Long id) {
        User user = getId(id);
        Set<Long> list1 = user.getFriends();
        List<User> listFriends = new ArrayList<>();
        for (Long friend : list1) {
            listFriends.add(getId(friend));
        }
        return listFriends;
    }
}
