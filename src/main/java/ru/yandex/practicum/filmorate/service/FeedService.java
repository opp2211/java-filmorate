package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FeedDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.Instant;
import java.util.List;

@Service
public class FeedService {

    private final FeedDbStorage feedStorage;
    private final UserDbStorage userDbStorage;

    @Autowired
    public FeedService(FeedDbStorage feedStorage, UserDbStorage userDbStorage) {
        this.feedStorage = feedStorage;
        this.userDbStorage = userDbStorage;
    }

    public List<Event> getUserEvents(int id) {
        try {
            User user = userDbStorage.get(id);
            return feedStorage.getUserEvents(user);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("Пользователь c ID=%s не найден", id));
        }
    }

    public void addFriendEvent(int userId, int friendId) {
        Event event = Event.create(Instant.now().toEpochMilli(),
                userId,
                EventType.FRIEND,
                Operation.ADD,
                friendId);
        feedStorage.addEvent(event);
    }

    public void deleteFriendEvent(int userId, int friendId) {
        Event event = Event.create(Instant.now().toEpochMilli(),
                userId,
                EventType.FRIEND,
                Operation.REMOVE,
                friendId);
        feedStorage.addEvent(event);
    }

    public void addLikeEvent(int userId, int filmId) {
        Event event = Event.create(Instant.now().toEpochMilli(),
                userId,
                EventType.LIKE,
                Operation.ADD,
                filmId);
        feedStorage.addEvent(event);
    }

    public void removeLikeEvent(int userId, int filmId) {
        Event event = Event.create(Instant.now().toEpochMilli(),
                userId,
                EventType.LIKE,
                Operation.REMOVE,
                filmId);
        feedStorage.addEvent(event);
    }

    public void addReviewEvent(Review review) {
        Event event = Event.create(Instant.now().toEpochMilli(),
                review.getUserId(),
                EventType.REVIEW,
                Operation.ADD,
                review.getReviewId());
        feedStorage.addEvent(event);
    }

    public void removeReviewEvent(Review review) {
        Event event = Event.create(Instant.now().toEpochMilli(),
                review.getUserId(),
                EventType.REVIEW,
                Operation.REMOVE,
                review.getReviewId());
        feedStorage.addEvent(event);
    }

    public void updateReviewEvent(Review review) {
        Event event = Event.create(Instant.now().toEpochMilli(),
                review.getUserId(),
                EventType.REVIEW,
                Operation.UPDATE,
                review.getReviewId());
        feedStorage.addEvent(event);
    }
}
