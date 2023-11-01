package ru.yandex.practicum.filmorate.storage.memory;

import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.storage.AbstractStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryBaseStorage<T extends BaseUnit> implements AbstractStorage<T> {
    private final Map<Long, T> storage = new HashMap<>();
    private long generationId;

    @Override
    public T create(T data) {
        data.setId(++generationId);
        storage.put(data.getId(), data);
        return data;
    }

    @Override
    public T update(T data) {
        if (!storage.containsKey(data.getId())) {
            throw new EntityNotFoundException(String.format("Обновление невозможно %s не сущесвует", data));
        }
        storage.put(data.getId(), data);
        return data;
    }

    @Override
    public List<T> getAll() {
        ArrayList<T> list = new ArrayList<>();
        if (storage.isEmpty()) {
            return list;
        }
        for (T data : storage.values()) {
            list.add(data);
        }
        return list;
    }

    @Override
    public T getId(Long id) {
        if (storage.containsKey(id)) {
            return storage.get(id);
        }
        throw new EntityNotFoundException("Нет объекта  с таким id");
    }

    @Override
    public boolean delete(T data) {
        if (storage.containsKey(data.getId())) {
            storage.remove(data.getId());
            return true;
        }
        throw new EntityNotFoundException(String.format("Удаление невозможно %s не сущесвует", data));
    }


}
