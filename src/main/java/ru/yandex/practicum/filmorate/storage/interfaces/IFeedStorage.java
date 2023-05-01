package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IFeedStorage {
    void addEvent(Event event);

    Event getEvent(int id);

    List<Event> getUserEvents(User user);

    Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException;
}
