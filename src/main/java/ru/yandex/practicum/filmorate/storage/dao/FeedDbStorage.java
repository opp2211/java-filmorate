package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.interfaces.IFeedStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class FeedDbStorage implements IFeedStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FeedDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Event addEvent(Event event) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("FEED")
                .usingGeneratedKeyColumns("id");

        insert.execute(Map.of("timestamp", event.getTimestamp(), "user_id", event.getUserId(),
                "operation", event.getOperation(), "event_type", event.getEventType(),
                "event_id", event.getEventId(), "entity_id", event.getEntityId()));
        int id = jdbcTemplate.query("SELECT COUNT(ID) FROM FEED",
                (rs, rowNum) -> rs.getInt("COUNT(ID)")).get(0);
        event.setEventId(id);
        return event;
    }

    @Override
    public Event getEvent(int id) {
        String sql = "SELECT * FROM FEED WHERE ID = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToEvent, id);
    }

    @Override
    public List<Event> getUserEvents(int userId) {
        String sql = "SELECT * FROM FEED WHERE USER_ID = ?";
        List<Event> result = jdbcTemplate.query(sql, this::mapRowToEvent, userId);
        return result.size() >= 1 ? result : new ArrayList<>();
    }

    @Override
    public Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {
        EventType type = EventType.valueOf(rs.getString("event_type"));
        Operation operation = Operation.valueOf(rs.getString("operation"));
        Event event = Event.create(rs.getTimestamp("timestamp"),
                rs.getInt("user_id"), type, operation,
                rs.getInt("entity_id"));
        event.setEventId(rs.getInt("id"));
        return event;
    }

}
