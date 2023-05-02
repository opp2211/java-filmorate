package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmDirectorStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
@RequiredArgsConstructor
public class FilmDirectorDbStorage implements FilmDirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void batchAddFilmDirector(int filmId, int[] directorIds) {
        String sql = "INSERT INTO film_director (film_id, director_id) VALUES (?, ?) ";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, filmId);
                ps.setInt(2, directorIds[i]);
            }

            @Override
            public int getBatchSize() {
                return directorIds.length;
            }
        });
    }

    @Override
    public void removeFilmDirectorsByFilmId(int filmId) {
        String sql = "DELETE FROM film_director WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }
}
