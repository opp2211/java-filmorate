package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Primary
@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    @Override
    public User add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("login", user.getLogin());
        parameters.put("name", user.getName());
        parameters.put("email", user.getEmail());
        parameters.put("birthday", Date.valueOf(user.getBirthday()));
        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters);
        user.setId(newId.intValue());
        return get(user.getId());
    }

    @Override
    public void remove(int id) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
    }

    @Override
    public User update(User user) {
        try {
            get(user.getId());
        } catch (Exception e) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден!");
        }
        String sql = "UPDATE users SET " +
                "login = ?, name = ?, email = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        return get(user.getId());
    }

    @Override
    public User get(int id) {
        try {
            String sql = "SELECT user_id, login, name, email, birthday " +
                    "FROM users WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (Exception e) {
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
    public void addFriend(int userId, int friendId) {
        try {
            User user = get(userId);
        } catch (Exception e) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден!");
        }
        try {
            User friend = get(friendId);
        } catch (Exception e) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден!");
        }

        String sql = "INSERT INTO user_friend (user_id, friend_id, is_accepted) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sql,
                userId,
                friendId,
                false);
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
