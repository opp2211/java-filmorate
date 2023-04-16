package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Primary
@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final GenreStorage genreStorage;

    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("release_date", Date.valueOf(film.getReleaseDate()));
        parameters.put("duration", film.getDuration());
        parameters.put("mpa_id", film.getMpa().getId());
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);

        if (film.getGenres() != null && film.getGenres().size() > 0) {
            for (Genre genre : film.getGenres()) {
                genreStorage.addFilmGenre(newId.intValue(), genre.getId());
            }
        }

        return get(newId.intValue());
    }

    @Override
    public void remove(int id) {
        String sql = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film update(Film film) {
        try {
            get(film.getId());
        } catch (Exception e) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден!");
        }

        String sql = "UPDATE film SET " +
                "title = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        genreStorage.removeFilmGenres(film.getId());
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            for (Genre genre : film.getGenres()) {
                genreStorage.addFilmGenre(film.getId(), genre.getId());
            }
        }
        return get(film.getId());
    }

    @Override
    public Film get(int id) {

        try {
            String sql = "SELECT film_id, title, description, release_date, duration, mpa_id " +
                    "FROM film WHERE film_id = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        } catch (Exception e) {
            throw new NotFoundException("Фильм с id = " + id + " не найден!");
        }
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT film_id, title, description, release_date, duration, mpa_id " +
                "FROM film";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "INSERT INTO user_like_film (film_id, user_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sql,
                filmId,
                userId);
    }

    @Override
    public void removeLike(int filmId, int userId) {
        String sql = "DELETE FROM user_like_film WHERE film_id = ? AND user_id = ?";
        if (jdbcTemplate.update(sql, filmId, userId) == 0) {
            throw new NotFoundException("Лайк пользователя c id=" + userId + " фильму c id=" + filmId + " не найден!");
        }
    }

    @Override
    public List<Film> getMostPopulars(int count) {
        String sql = "SELECT f.film_id, f.title, f.description, f.release_date, f.duration, f.mpa_id " +
                "FROM film f " +
                "LEFT JOIN user_like_film ulf ON f.film_id = ulf.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(ulf.user_id) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("title"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(getMpa(rs.getInt("mpa_id")))
                .genres(genreStorage.getFilmGenres(rs.getInt("film_id")))
                .build();
    }

    private Mpa getMpa(int mpaId) {
        String sql = "SELECT mpa_id, name " +
                "FROM mpa WHERE mpa_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, mpaId);
    }

    private Mpa mapRowToMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getInt("mpa_id"))
                .name(rs.getString("name"))
                .build();
    }
}
