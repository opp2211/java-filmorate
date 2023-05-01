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

    @Override
    public List<Film> getUsersRecommendations(int userId) {

        final String sqlGetFilmsByUsersWithSimilarLikes =
                "SELECT f.film_id, f.title, f.description, f.release_date, f.duration, f.mpa_id " +
                        "FROM film f " +
                        "JOIN user_like_film ulf ON ulf.film_id = f.film_id " +
                        "AND ulf.user_id IN " +
                        "( " +
                        "SELECT ufl1.user_id " +
                        "FROM user_like_film ufl1 " +
                        "WHERE ufl1.film_id IN " +
                        "( " +
                        "SELECT ulf2.film_id " +
                        "FROM user_like_film ulf2 " +
                        "WHERE ulf2.user_id = ? " +
                        ") " +
                        "AND ufl1.user_id <> ? " +
                        "GROUP BY ufl1.user_id " +
                        "HAVING COUNT(ufl1.user_id) >= 1 " +
                        ") " +
                        "AND f.film_id NOT IN " +
                        "( " +
                        "SELECT ufl3.film_id " +
                        "FROM user_like_film ufl3 " +
                        "WHERE ufl3.user_id = ? " +
                        ") " +
                        "GROUP BY f.film_id " +
                        "ORDER BY COUNT(ulf.user_id) DESC";

        return jdbcTemplate.query(sqlGetFilmsByUsersWithSimilarLikes, this::mapRowToFilm, userId, userId, userId);
    }

    @Override
    public List<Film> getCommonFilms(int userId, int friendId) {
        String sql = "SELECT f.film_id, f.title, f.description, f.release_date, f.duration, f.mpa_id, COUNT(ulf1.user_id) " +
                "FROM film f JOIN user_like_film ulf ON f.film_id = ulf.film_id " +
                "AND ulf.user_id = ? AND f.film_id in " +
                "(SELECT film_id from user_like_film WHERE user_id = ?) " +
                "JOIN user_like_film ulf1 ON f.film_id = ulf1.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(ulf1.user_id) DESC";
        return jdbcTemplate.query(sql, this::mapRowToFilm, userId, friendId);
    }

    @Override
    public List<Film> findByDirectorAndName(String query) {
        String sql = "SELECT f.film_id, f.title, f.description, f.release_date, f.duration, f.mpa_id " +
                "FROM film f LEFT JOIN user_like_film ulf on f.film_id = ulf.film_id " +
                "    LEFT JOIN film_director fd on f.film_id = fd.film_id " +
                " JOIN director d on fd.director_id = d.director_id and LOWER (d.name) LIKE LOWER(?) " +
                "OR LOWER(f.title) LIKE LOWER(?) " +
                "GROUP BY f.film_id, d.name " +
                "ORDER BY COUNT(ulf.user_id) DESC";

        return jdbcTemplate.query(sql, this::mapRowToFilm, query, query);
    }

    @Override
    public List<Film> findByDirector(String query) {
        String sql = "SELECT f.film_id, f.title, f.description, f.release_date, f.duration, f.mpa_id " +
                "FROM film f LEFT JOIN user_like_film ulf on f.film_id = ulf.film_id " +
                "LEFT JOIN film_director fd on f.film_id = fd.film_id " +
                "JOIN director d on fd.director_id = d.director_id and LOWER (d.name) LIKE LOWER(?) " +
                "GROUP BY f.film_id, d.name " +
                "ORDER BY COUNT(ulf.user_id) DESC";

        return jdbcTemplate.query(sql, this::mapRowToFilm, query);
    }

    @Override
    public List<Film> findByName(String query) {
        String sql = "SELECT f.film_id, f.title, f.description, f.release_date, f.duration, f.mpa_id " +
                "FROM film f LEFT JOIN user_like_film ULF on f.film_id = ULF.film_id where LOWER (f.title) LIKE LOWER (?) " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(ulf.user_id) DESC";

        return jdbcTemplate.query(sql, this::mapRowToFilm, query);
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
