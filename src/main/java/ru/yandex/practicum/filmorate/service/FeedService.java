package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.dao.FeedDbStorage;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedService {

    private final FeedDbStorage feedStorage;

    @Autowired
    public FeedService(FeedDbStorage feedStorage) {
        this.feedStorage = feedStorage;
    }

    public void addEvent(Event event) {
        feedStorage.addEvent(event);
    }

    public Event getEvent(int id) {
        return feedStorage.getEvent(id);
    }

    public List<Event> getUserEvents(int userId) {
        return feedStorage.getUserEvents(userId);
    }

    public void addFriendEvent(int userId, int friendId) {
        Event event = Event.create(Timestamp.from(Instant.EPOCH),
                userId,
                EventType.FRIEND,
                Operation.ADD,
                friendId);
        feedStorage.addEvent(event);
    }

    public void deleteFriendEvent(int userId, int friendId) {
        Event event = Event.create(Timestamp.from(Instant.EPOCH),
                userId,
                EventType.FRIEND,
                Operation.REMOVE,
                friendId);
        feedStorage.addEvent(event);
    }

    public void addLikeEvent(int userId, int filmId) {
        Event event = Event.create(Timestamp.from(Instant.EPOCH),
                userId,
                EventType.LIKE,
                Operation.ADD,
                filmId);
        feedStorage.addEvent(event);
    }

    public void removeLikeEvent(int userId, int filmId) {
        Event event = Event.create(Timestamp.from(Instant.EPOCH),
                userId,
                EventType.LIKE,
                Operation.REMOVE,
                filmId);
        feedStorage.addEvent(event);
    }

    public void addReviewEvent(Review review) {
        Event event = Event.create(Timestamp.from(Instant.EPOCH),
                review.getUserId(),
                EventType.REVIEW,
                Operation.ADD,
                review.getFilmId());
        feedStorage.addEvent(event);
    }

    public void removeReviewEvent(Review review) {
        Event event = Event.create(Timestamp.from(Instant.EPOCH),
                review.getUserId(),
                EventType.REVIEW,
                Operation.REMOVE,
                review.getFilmId());
        feedStorage.addEvent(event);
    }

    public void updateReviewEvent(Review review) {
        Event event = Event.create(Timestamp.from(Instant.EPOCH),
                review.getUserId(),
                EventType.REVIEW,
                Operation.UPDATE,
                review.getFilmId());
        feedStorage.addEvent(event);
    }
}
