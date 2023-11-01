package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryBaseStorage;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService extends AbstractService<Film> {

    private InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();

    public FilmService(InMemoryBaseStorage<Film> inMemoryBaseStorage) {
        super(inMemoryBaseStorage);
    }

    public List<Film> popularList(Integer count) {
        List<Film> list = getAll();
        if (list.isEmpty()) {
            return list;
        }
        List<Film> result = list
                .stream()
                .sorted((o1, o2) -> o2.getLike().size() - o1.getLike().size())
                .limit(count)
                //преобразуем стрим обратно в список
                .collect(Collectors.toList());

        return result;
    }

    public Film addLike(long idFilm, long idUser) {
        Film film = getId(idFilm);
        Set<Long> list = film.getLike();
        list.add(idUser);
        film.setLike(list);
        return film;
    }

    public Film deleteLike(long idFilm, long idUser) {
        Film film = getId(idFilm);
        Set<Long> list = film.getLike();
        if (list.contains(idUser)) {
            list.remove(idUser);
            film.setLike(list);
            return film;
        }
        throw new EntityNotFoundException("Нет объекта удаления"); //ошибка
    }


}
