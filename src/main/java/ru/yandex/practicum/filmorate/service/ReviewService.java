package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.ReviewDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewDbStorage reviewStorage;
    private final UserService userService;
    private final FilmService filmService;

    public Review add(Review review) {
        validateFields(review);
        userService.get(review.getUserId());
        filmService.get(review.getFilmId());

        review.setReviewId(reviewStorage.add(review));
        return review;
    }

    public Review get(int id) {
        try {
            return reviewStorage.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Отзыв с id = " + id + " не найден!");
        }
    }

    public Review update(Review review) {
        validateFields(review);

        if (!reviewStorage.update(review))
            throw new NotFoundException("Отзыв с id = " + review.getReviewId() + " не найден!");
        return get(review.getReviewId());
    }

    public void delete(int id) {
        get(id);
        reviewStorage.delete(id);
    }

    public List<Review> getReviews(Integer filmId, int count) {
        if (filmId != null)
            filmService.get(filmId);
        return reviewStorage.getReviews(filmId, count);
    }

    public void addLike(int id, int userId) {
        get(id);
        userService.get(userId);
        reviewStorage.addLike(id, userId);
    }

    public void addDis(int id, int userId) {
        get(id);
        userService.get(userId);
        reviewStorage.addDis(id, userId);
    }

    public void deleteLike(int id, int userId) {
        get(id);
        userService.get(userId);
        reviewStorage.deleteLike(id, userId);
    }

    public void deleteDis(int id, int userId) {
        get(id);
        userService.get(userId);
        reviewStorage.deleteDis(id, userId);
    }

    private void validateFields(Review review) {
        if (review == null)
            throw new ValidationException("Request body is empty.");
        if (review.getContent() == null)
            throw new ValidationException("Review content is empty.");
        if (review.getIsPositive() == null)
            throw new ValidationException("Empty isPositive review field.");
        if (review.getUserId() == null)
            throw new ValidationException("Empty userId review field.");
        if (review.getFilmId() == null)
            throw new ValidationException("Empty filmId review field.");
    }
}
