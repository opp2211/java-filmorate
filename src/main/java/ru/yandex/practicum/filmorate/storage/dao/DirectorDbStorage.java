package ru.yandex.practicum.filmorate.storage.dao;


import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> getAll() {
        String sql = "SELECT director_id, name " +
                "FROM director";
        return jdbcTemplate.query(sql, this::mapRowToDirector);
    }

    @Override
    public Director getById(int id) {
        String sql = "SELECT director_id, name " +
                "FROM director WHERE director_id = ? ";
        return jdbcTemplate.queryForObject(sql, this::mapRowToDirector, id);
    }

    @Override
    public int add(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("director")
                .usingGeneratedKeyColumns("director_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("director_id", director.getId());
        parameters.put("name", director.getName());

        return simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    @Override
    public boolean update(Director director) {
        String sql = "UPDATE director SET " +
                "name = ? " +
                "WHERE director_id = ?";
        int rowAffected = jdbcTemplate.update(sql,
                director.getName(),
                director.getId()
        );
        return rowAffected > 0;
    }

    @Override
    public void removeById(int id) {
        String sql = "DELETE FROM director WHERE director_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Director> getFilmDirectors(int filmId) {
        String sql = "SELECT d.director_id, d.name " +
                "FROM film_director fd " +
                "JOIN director d ON fd.director_id = d.director_id " +
                "AND fd.film_id = ? ";
        return jdbcTemplate.query(sql, this::mapRowToDirector, filmId);
    }

    private Director mapRowToDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getInt("director_id"))
                .name(rs.getString("name"))
                .build();
    }
}
