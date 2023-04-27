package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface IFeedStorage {
    Event addEvent(Event event);

    Event getEvent(int id);

    List<Event> getUserEvents(int userId);

    Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException;
}
