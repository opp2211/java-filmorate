package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("login", user.getLogin());
        parameters.put("name", user.getName());
        parameters.put("email", user.getEmail());
        parameters.put("birthday", Date.valueOf(user.getBirthday()));
        return simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET " +
                "login = ?, name = ?, email = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public User get(int id) {
        try {
            String sql = "SELECT user_id, login, name, email, birthday " +
                    "FROM users WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден!");
        }
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT user_id, login, name, email, birthday " +
                "FROM users";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public void addFriend(int userId, int friendId, boolean isAccepted) {
        String sql = "INSERT INTO user_friend (user_id, friend_id, is_accepted) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, friendId, isAccepted);
    }

    @Override
    public boolean hasFriendship(int userId, int friendId) {
        String sql = "SELECT count(*) FROM user_friend WHERE user_id = ? AND friend_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> rs.getInt(1), userId, friendId);

        return count != null && count > 0;
    }

    @Override
    public void updateFriendship(int userId, int friendId, boolean isAccepted) {
        String sql = "UPDATE user_friend SET " +
                "is_accepted = ? " +
                "WHERE user_id = ? AND friend_id = ? ";
        jdbcTemplate.update(sql, isAccepted, userId, friendId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sql = "DELETE FROM user_friend WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getFriends(int userId) {
        String sql = "SELECT f.user_id, f.login, f.name, f.email, f.birthday " +
                "FROM user_friend uf " +
                "JOIN users f ON uf.friend_id = f.user_id " +
                "AND uf.user_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToUser, userId);
    }

    @Override
    public List<User> getMutualFriends(int userId, int otherId) {
        String sql = "SELECT f.user_id, f.login, f.name, f.email, f.birthday " +
                "FROM user_friend uf " +
                "JOIN users f ON uf.friend_id = f.user_id " +
                "AND uf.user_id = ? " +
                "AND f.user_id IN ( " +
                "SELECT f.user_id " +
                "FROM user_friend uf " +
                "JOIN users f ON uf.friend_id = f.user_id " +
                "AND uf.user_id = ? " +
                ")";
        return jdbcTemplate.query(sql, this::mapRowToUser, userId, otherId);
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
