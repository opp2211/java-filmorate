package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.interfaces.UserLikeFilmStorage;

@Repository
@RequiredArgsConstructor
public class UserLikeFilmDbStorage implements UserLikeFilmStorage {
    private final JdbcTemplate jdbcTemplate;

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
}
