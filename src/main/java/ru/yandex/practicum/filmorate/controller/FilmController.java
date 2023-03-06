package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int idGenerator = 1;
    Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
    @PostMapping
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        log.debug("Получен запрос на добавление фильма:\n" + film.toString());
        if (!filmValidation(film))
            throw new ValidationException();
        films.put(idGenerator, film);
        film.setId(idGenerator++);
        log.debug("Фильм успешно добавлен:\n" + film);
        return film;
    }
    @PutMapping
    public Film updateFilmData(@RequestBody Film film) throws ValidationException {
        log.debug("Получен запрос на изменение данных фильма:\n" + film.toString());
        if (films.containsKey(film.getId())) {
            if (!filmValidation(film))
                throw new ValidationException();
            films.put(film.getId(), film);
        } else {
            log.debug("Ошибка: фильм с ID=" + film.getId() + " не найден");
            throw new ValidationException();
        }

        log.debug("Данные фильма успешно изменены:\n" + film);
        return film;
    }

    private boolean filmValidation(Film film) {
        final int maxDescriptionLength = 200;
        final LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        if (film.getName().isBlank()) {
            log.debug("Ошибка валидации: название не может быть пустым " +
                    "-- name=\"" + film.getName() + "\"");
            return false;
        }
        if (film.getDescription().length() > maxDescriptionLength) {
            log.debug("Ошибка валидации: максимальная длина описания — 200 символов " +
                    "-- description.length=" + film.getDescription().length());
            return false;
        }
        if (film.getReleaseDate().isBefore(minReleaseDate)) {
            log.debug("Ошибка валидации: дата релиза — не раньше " + minReleaseDate.format(formatter) + " " +
                    "-- releaseDate=" + film.getReleaseDate().format(formatter));
            return false;
        }
        if (film.getDuration() < 0) {
            log.debug("Ошибка валидации: продолжительность фильма должна быть положительной " +
                    "-- duration=" + film.getDuration());
            return false;
        }

        return true;
    }
}
