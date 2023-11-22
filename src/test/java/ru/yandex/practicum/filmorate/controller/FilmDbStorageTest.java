package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.FilmDbStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindFilmById() {
        // Подготавливаем данные для теста
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        Genre genre = Genre.builder().id(1L).name("Комедия").build();
        genres.add(genre);
        Film newFilm = Film.builder()
                .id(1L)
                .name("titanic")
                .description("ship")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(100)
                .mpa(Mpa.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .genres(genres)
                .build();

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(newFilm);

        // вызываем тестируемый метод
        Film savedFilm = filmStorage.getId(newFilm.getId());

        // проверяем утверждения
        assertThat(savedFilm)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newFilm);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindFilmUpdate() {
        // Подготавливаем данные для теста
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        Genre genre = Genre.builder().id(1L).name("Комедия").build();
        genres.add(genre);
        Film newFilm = Film.builder()
                .id(1L)
                .name("titanic")
                .description("ship")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(100)
                .mpa(Mpa.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .genres(genres)
                .build();

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(newFilm);
        Film updateFilm = Film.builder()
                .id(1L)
                .name("titanicNew")
                .description("shipbuilding")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .mpa(Mpa.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .genres(genres)
                .build();
        // вызываем тестируемый метод

        Film savedFilm = filmStorage.update(updateFilm);

        // проверяем утверждения
        assertThat(savedFilm)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(updateFilm);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testFindFilmDelete() {
        // Подготавливаем данные для теста
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        Genre genre = Genre.builder().id(1L).name("Комедия").build();
        genres.add(genre);
        Film newFilm = Film.builder()
                .id(1L)
                .name("titanic")
                .description("ship")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(100)
                .mpa(Mpa.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .genres(genres)
                .build();

        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.create(newFilm);
        Film updateFilm = Film.builder()
                .id(1L)
                .name("titanicNew")
                .description("shipbuilding")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .mpa(Mpa.builder()
                        .id(1L)
                        .name("G")
                        .build())
                .genres(genres)
                .build();
        filmStorage.create(updateFilm);
        // вызываем тестируемый метод

        filmStorage.delete(newFilm);
        List<Film> list = filmStorage.getAll();

        // проверяем утверждения
        assertThat(list.get(0))
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(updateFilm);// и сохраненного пользователя - совпадают
        assertThat(list.size())
                .isEqualTo(1);
    }

}
