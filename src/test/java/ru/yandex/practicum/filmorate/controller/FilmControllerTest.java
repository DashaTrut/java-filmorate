package ru.yandex.practicum.filmorate.controller;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class FilmControllerTest {
    private FilmController filmController;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();

    }

    @Test
    void validateNegativeFilm() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(1800, 10, 15))
                .duration_min(100)
                .build();
        //Film newFilm = filmController.addFilm(film);
        //  Assertions.assertEquals(film.getName(), newFilm.getName());

        Assertions.assertThrows(EntityNotFoundException.class, () -> filmController.addFilm(film));
    }

    @Test
    void validatePositiveFilm() {
        Film film = Film.builder()
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(1950, 10, 15))
                .duration_min(100)
                .build();
        Film newFilm = filmController.addFilm(film);
        Assertions.assertEquals(film.getName(), newFilm.getName());
    }
}
