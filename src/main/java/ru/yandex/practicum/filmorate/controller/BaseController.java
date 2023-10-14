package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseController<T extends BaseUnit> {
    private final Map<Long, T> storage = new HashMap<>();
    private long generationId;

    public List<T> getMap() {  // Обработка GET-запроса по пути "/films"  получение всех фильмов.
        return new ArrayList<T>(storage.values());
    }

    public T update(T data) {
        validate(data);
        if (!storage.containsKey(data.getId())) {
            throw new EntityNotFoundException(String.format("Обновление невозможно %s не сущесвует", data));
        }
        storage.put(data.getId(), data);
        return data;

    }

    public T create(T data) {
        validate(data);
        data.setId(++generationId);
        storage.put(data.getId(), data);
        return data;
    }
    public abstract void validate(T data);
}
