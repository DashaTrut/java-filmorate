package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryBaseStorage;

import java.util.*;

@AllArgsConstructor
public abstract class AbstractService<T extends BaseUnit> implements AbstractStorage<T> {

    private final InMemoryBaseStorage<T> inMemoryBaseStorage;

    @Override
    public T create(T data) {
        return inMemoryBaseStorage.create(data);
    }

    @Override
    public T update(T data) {
        return inMemoryBaseStorage.update(data);
    }

    @Override
    public List<T> getAll() {
        return inMemoryBaseStorage.getAll();
    }

    @Override
    public T getId(Long id) {
        return inMemoryBaseStorage.getId(id);
    }

    @Override
    public boolean delete(T data) {
        return inMemoryBaseStorage.delete(data);
    }

}