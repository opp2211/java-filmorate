package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Override
    public int add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("film")
                .usingGeneratedKeyColumns("film_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", film.getName());
        parameters.put("description", film.getDescription());
        parameters.put("release_date", Date.valueOf(film.getReleaseDate()));
        parameters.put("duration", film.getDuration());
        parameters.put("mpa_id", film.getMpa().getId());

        return simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    @Override
    public void remove(int id) {
        String sql = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM film";
        jdbcTemplate.update(sql);
    }

    @Override
    public void update(Film film) {
        //todo проверять наличие фильма?
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
        //todo проверить через возврат апдейта?
    }

    @Override
    public Film get(int id) {
        String sql = "SELECT film_id, title, description, release_date, duration, mpa_id " +
                "FROM film WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT film_id, title, description, release_date, duration, mpa_id " +
                "FROM film";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
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
                .mpa(Mpa.builder()
                        .id(rs.getInt("mpa_id"))
                        .build())
                .build();
    }
}
