package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.LikeDbStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service

public class FilmService extends AbstractService<Film> {
    private final LikeDbStorage likeDbStoragge;

    private final UserService userService;

    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    public FilmService(LikeDbStorage likeDbStoragge, @Qualifier("filmDbStorage") FilmStorage filmStorage, UserService userService) {
        super(filmStorage);
        this.likeDbStoragge = likeDbStoragge;
        this.userService = userService;
    }


    public void addLike(long idFilm, long idUser) {
        likeDbStoragge.addLike(idFilm, idUser);
    }

    public List<Film> popularList(Integer count) {
        return likeDbStoragge.popularListFilm(count);
    }


    public void deleteLike(long idFilm, long idUser) {
        userService.getId(idUser);
        likeDbStoragge.likeDelete(idFilm, idUser);
    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            throw new ValidationException(String.format("Дата релиза раньше %s", minReleaseDate));
        }
    }

}
