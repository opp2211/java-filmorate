package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre get(int id) {
        try {
            String sql = "SELECT genre_id, name FROM genre " +
                    "WHERE genre_id = ? ";
            return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с id = " + id + " не найден!");
        }
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT genre_id, name FROM genre ";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    @Override
    public List<Genre> getFilmGenres(int filmId) {
        String sql = "SELECT g.genre_id, g.name " +
                "FROM film_genre fg " +
                "JOIN genre g ON fg.genre_id = g.genre_id " +
                "AND fg.film_id = ? ";
        return jdbcTemplate.query(sql, this::mapRowToGenre, filmId);
    }

    @Override
    public void addFilmGenre(int filmId, int genreId) {
        if (getFilmGenres(filmId).stream().map(Genre::getId).noneMatch(id -> id == genreId)) {
            String sql = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?) ";
            jdbcTemplate.update(sql, filmId, genreId);
        }
    }

    @Override
    public void removeFilmGenres(int filmId) {
        String sql = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public void removeAllFilmGenres() {
        String sql = "DELETE FROM film_genre";
        jdbcTemplate.update(sql);
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("name"))
                .build();
    }
}
