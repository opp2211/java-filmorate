package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int idGenerator = 1;
    Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }
    @PostMapping
    public Film addNew(@RequestBody @Valid Film film) {
        log.debug("Получен запрос на добавление фильма:\n" + film.toString());
        films.put(idGenerator, film);
        film.setId(idGenerator++);
        log.debug("Фильм успешно добавлен:\n" + film);
        return film;
    }
    @PutMapping
    public Film updateExistFilmData(@RequestBody @Valid Film film) {
        log.debug("Получен запрос на изменение данных фильма:\n" + film.toString());
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            log.debug("Ошибка: фильм с ID=" + film.getId() + " не найден");
            throw new NotFoundException();
        }

        log.debug("Данные фильма успешно изменены:\n" + film);
        return film;
    }
}
