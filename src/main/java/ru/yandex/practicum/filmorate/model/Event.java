package ru.yandex.practicum.filmorate.model;


import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import ru.yandex.practicum.filmorate.enums.EventType;
import ru.yandex.practicum.filmorate.enums.Operation;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(staticName = "create")
@Data
public class Event {
    @NotNull
    @NotNull
    @EqualsAndHashCode.Exclude
    @NonFinal
    int eventId;
    @NotNull
    Timestamp eventTime;
    @NotNull
    int userId;
    EventType eventType;
    Operation operation;
    @NotNull
    int entityId;
}
