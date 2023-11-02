package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController extends BaseController<Film> {
    private final FilmService filmService;

    @GetMapping("/popular")
    public List<Film> popularFilm(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Popular film");
        return filmService.popularList(count);
    }

    @GetMapping
    public List<Film> getFilms() {  // Обработка GET-запроса по пути "/films" получение всех фильмов.
        log.info("Get films");
        return filmService.getAll();
    }

    @PostMapping //добавление фильма;
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Add film{}", film);
        return filmService.create(film);
    }

    @PutMapping //обновление фильма;
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Update film{}", film);
        return filmService.update(film);
    }

//    @Override
//    public void validate(Film film) {
//        if (film.getReleaseDate().isBefore(minReleaseDate)) {
//            throw new ValidationException(String.format("Дата релиза раньше %s", minReleaseDate));
//        }
//    }


    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Like film{}", id);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLikeFilm(@PathVariable long id, @PathVariable long userId) {
        log.info("Delete like film{}", id);
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("{id}")
    public Film getForId(@PathVariable Long id) {
        log.info("Get film id{}", id);
        return filmService.getId(id);
    }
}
