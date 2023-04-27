package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

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

    @Override
    public int add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
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
    public void delete(int id) {
        get(id);
        String sql = "DELETE FROM film WHERE film_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM film";
        jdbcTemplate.update(sql);
    }

    @Override
    public boolean update(Film film) {
        String sql = "UPDATE film SET " +
                "title = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? " +
                "WHERE film_id = ?";
        int rowAffected = jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        return rowAffected > 0;
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

    @Override
    public List<Film> getByDirector(int directorId, String sortBy) {
        String sort;
        if (sortBy.equals("year"))
            sort = "f.release_date ASC";
        else if (sortBy.equals("likes"))
            sort = "COUNT(uf.user_id) DESC";
        else
            throw new NotFoundException("Тип сортирорки " + sortBy + " не найден!");

        String sql = "SELECT f.film_id, f.title, f.description, f.release_date, f.duration, f.mpa_id " +
                "FROM film f " +
                "JOIN film_director fd ON fd.film_id = f.film_id AND fd.director_id = ? " +
                "LEFT JOIN user_like_film uf ON f.film_id = uf.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY " + sort;
        return jdbcTemplate.query(sql, this::mapRowToFilm, directorId);
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
