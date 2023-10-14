package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Slf4j
//@AllArgsConstructor
@RestController("/films")
public class FilmController extends BaseController<Film> {

    @GetMapping
    public List<Film> getFilms() {  // Обработка GET-запроса по пути "/films"  получение всех фильмов.
        log.info("Get films");
        return super.getMap();
    }

    @PostMapping //добавление фильма;
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Add film{}", film);
        validate(film);
        return super.create(film);
    }

    @PutMapping //обновление фильма;
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Update film{}", film);
        return super.update(film);
    }
    @Override
    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new EntityNotFoundException("Дата релиза раньше 28.12.1895");
        }
    }
}
