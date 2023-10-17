package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends BaseController<Film> {
    private final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);

    @GetMapping
    public List<Film> getFilms() {  // Обработка GET-запроса по пути "/films" получение всех фильмов.
        log.info("Get films");
        return getMap();
    }

    @PostMapping //добавление фильма;
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Add film{}", film);
        return create(film);
    }

    @PutMapping //обновление фильма;
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Update film{}", film);
        return update(film);
    }

    @Override
    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            throw new EntityNotFoundException(String.format("Дата релиза раньше %s", minReleaseDate));
        }
    }
}
