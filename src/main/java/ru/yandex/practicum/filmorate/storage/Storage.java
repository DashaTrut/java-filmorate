package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseUnit;

import java.util.List;

public interface Storage<T extends BaseUnit> {
    T create(T data);

    T update(T data);

    List<T> getAll();

    T getId(Long id);

    boolean delete(T data);

}
