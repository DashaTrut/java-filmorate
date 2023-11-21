package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;


import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Qualifier
@Primary
@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final GenreDbStorage genreDbStorage;
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private long generatedID = 1;

    private final LikeDbStorage likeDbStorage;

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO FILM (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) VALUES(?, ?, ?, ?, ?, ?);";
        if (film.getId() == null) {
            film.setId(generatedID++);
        } else {
            generatedID++;
        }
        jdbcTemplate.update(sqlQuery, film.getId(), film.getName(), film.getDescription(), Date.valueOf(film.getReleaseDate()),
                film.getDuration(), film.getMpa().getId());
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String genreSql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES(?, ?)";
                jdbcTemplate.update(genreSql, film.getId(), genre.getId());
            }
        }
        for (long id : film.getLike()) {
            likeDbStorage.addLike(film.getId(), id);
        }
        return getId(film.getId());
    }


    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILM SET FILM_ID=?, NAME=?, DESCRIPTION=?, RELEASE_DATE=?, DURATION=?, RATING_ID=? " +
                "WHERE FILM_ID=?;";
        jdbcTemplate.update(sqlQuery, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        String sqlDeleteLike = "DELETE FROM LIKES WHERE FILM_ID=?;";
        jdbcTemplate.update(sqlDeleteLike, film.getId());
        for (long id : film.getLike()) {
            likeDbStorage.addLike(film.getId(), id);
        }
        String sqlDelete = "DELETE FROM FILM_GENRE WHERE FILM_ID=?;";
        jdbcTemplate.update(sqlDelete, film.getId());
        if (!film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                String genreSql = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES(?, ?)";
                jdbcTemplate.update(genreSql, film.getId(), genre.getId());
            }
        }
        return getId(film.getId());
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.RATING_ID, R.RATING_TITLE " +
                "FROM FILM AS F INNER JOIN RATING AS R ON F.RATING_ID = R.RATING_ID; ";
        List<Film> list = jdbcTemplate.query(sqlQuery, FilmDbStorage::createFilm);
        for (int i = 0; i < list.size(); i++) {
            genreDbStorage.genresForFilm(list.get(i));
        }
        return list;
    }

    @Override
    public Film getId(Long id) {
        String sqlQuery = "select F.FILM_ID, F.NAME, F.DESCRIPTION, F.RELEASE_DATE, F.DURATION, F.RATING_ID, R.RATING_TITLE" +
                " from FILM AS F LEFT JOIN RATING AS R ON F.RATING_ID=R.RATING_ID" +
                " where F.FILM_ID = ?;";
        List<Film> films = jdbcTemplate.query(sqlQuery, FilmDbStorage::createFilm, id);
        if (films.size() != 1) {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new EntityNotFoundException("Пользователь с идентификатором id не найден.");
        }
        log.info("Найден фильм: {} {}", id, films.get(0).getName());
        genreDbStorage.genresForFilm(films.get(0));
        return films.get(0);


    }

    static Film createFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate((rs.getDate("RELEASE_DATE")).toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(Mpa.builder()
                        .id(rs.getLong("RATING_ID"))
                        .name(rs.getString("RATING_TITLE"))
                        .build())
                .build();
    }

    @Override
    public boolean delete(Film data) {
        String sqlQuery = "DELETE FROM FILM WHERE FILM_ID=?;";
        return jdbcTemplate.update(sqlQuery, data.getId()) > 0;
    }
}
