package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FeedDbStorage;

import java.sql.Timestamp;
import java.time.Instant;
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

    public Event addFriendEvent(int userId, int friendId) {
        Event event = Event.create(Timestamp.from(Instant.now()),
                userId,
                EventType.FRIEND,
                Operation.ADD,
                friendId);
        return feedStorage.addEvent(event);
    }

    public Event deleteFriendEvent(int userId, int friendId) {
        Event event = Event.create(Timestamp.from(Instant.now()),
                userId,
                EventType.FRIEND,
                Operation.REMOVE,
                friendId);
        return feedStorage.addEvent(event);
    }

    public Event addLikeEvent(int userId, int filmId) {
        Event event = Event.create(Timestamp.from(Instant.now()),
                userId,
                EventType.LIKE,
                Operation.ADD,
                filmId);
        return feedStorage.addEvent(event);
    }

    public Event removeLikeEvent(int userId, int filmId) {
        Event event = Event.create(Timestamp.from(Instant.now()),
                userId,
                EventType.LIKE,
                Operation.REMOVE,
                filmId);
        return feedStorage.addEvent(event);
    }

}
