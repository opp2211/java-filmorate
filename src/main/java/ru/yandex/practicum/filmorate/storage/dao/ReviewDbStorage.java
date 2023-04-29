package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReviewDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public int add(Review review) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("review")
                .usingGeneratedKeyColumns("review_id");
        return simpleJdbcInsert.executeAndReturnKey(review.toMap()).intValue();
    }

    public Review get(int id) {
        String sql = "SELECT * FROM review WHERE review_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToReview, id);
    }

    public boolean update(Review review) {
        String sql = "UPDATE review " +
                "SET content = ?, is_positive = ?, user_id = ?, film_id = ? " +
                "WHERE review_id = ?";
        int rowAffected = jdbcTemplate.update(sql,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getReviewId());
        return rowAffected > 0;
    }

    public void delete(int id) {
        String sql = "DELETE FROM review WHERE review_id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Review> getReviews(Integer filmId, int count) {
        String sql = "SELECT * FROM review" +
                (filmId != null ? " WHERE film_id = " + filmId : "") +
                " ORDER BY useful DESC LIMIT ?";
        return jdbcTemplate.query(sql, this::mapRowToReview, count);
    }

    public void addLike(int id, int userId) {
        String sql = "INSERT INTO user_like_review (REVIEW_ID, USER_ID, IS_LIKE) " +
                "SELECT ?, ?, TRUE " +
                "WHERE NOT EXISTS( SELECT * FROM user_like_review WHERE review_id = ? AND user_id = ? AND is_like = TRUE)";
        if (1 == jdbcTemplate.update(sql, id, userId, id, userId))
            updateUseful(id, true);
    }

    public void addDis(int id, int userId) {
        String sql = "INSERT INTO user_like_review (REVIEW_ID, USER_ID, IS_LIKE) " +
                "SELECT ?, ?, FALSE " +
                "WHERE NOT EXISTS( SELECT * FROM user_like_review WHERE review_id = ? AND user_id = ? AND is_like = FALSE)";
        if (1 == jdbcTemplate.update(sql, id, userId, id, userId))
            updateUseful(id, false);
    }

    public void deleteLike(int id, int userId) {
        String sql = "DELETE FROM user_like_review WHERE review_id = ? AND user_id = ? AND is_like = TRUE";
        if (1 == jdbcTemplate.update(sql, id, userId))
            updateUseful(id, false);
    }

    public void deleteDis(int id, int userId) {
        String sql = "DELETE FROM user_like_review WHERE review_id = ? AND user_id = ? AND is_like = FALSE";
        if (1 == jdbcTemplate.update(sql, id, userId))
            updateUseful(id, true);
    }

    private Review mapRowToReview(ResultSet rs, int i) throws SQLException {
        return Review.builder()
                .reviewId(rs.getInt("review_id"))
                .content(rs.getString("content"))
                .isPositive(rs.getBoolean("is_positive"))
                .userId(rs.getInt("user_id"))
                .filmId(rs.getInt("film_id"))
                .useful(rs.getInt("useful"))
                .build();
    }

    private void updateUseful(int id, boolean increase) {
        String sql = "UPDATE REVIEW " +
                "SET useful = useful " + (increase ? "+ 1" : "- 1") +
                " WHERE review_id = " + id;
        jdbcTemplate.update(sql);
    }
}
