package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Primary
@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "SELECT * FROM RATING";
        return jdbcTemplate.query(sqlQuery, MpaDbStorage::createMpa);
    }

    @Override
    public Mpa getId(Long id) {
        String sqlQuery = "SELECT * FROM RATING WHERE RATING_ID = ?";
        List<Mpa> mpas = jdbcTemplate.query(sqlQuery, MpaDbStorage::createMpa, id);
        if (mpas.size() != 1) {
            throw new EntityNotFoundException("Genre with id is not single");
        }
        return mpas.get(0);
    }

    static Mpa createMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("RATING_ID"))
                .name(rs.getString("RATING_TITLE"))
                .build();
    }

    @Override
    public Mpa create(Mpa data) {
        throw new EntityNotFoundException();
    }

    @Override
    public Mpa update(Mpa data) {
        throw new EntityNotFoundException();
    }

    @Override
    public boolean delete(Mpa data) {
        throw new EntityNotFoundException();
    }

}

