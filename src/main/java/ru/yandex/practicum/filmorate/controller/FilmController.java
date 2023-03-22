package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmService.getAll();
    }
    @PostMapping
    public Film addNew(@RequestBody Film film) { //todo На каком уровне должна быть валидация?
        return filmService.add(film);
    }
    @PutMapping
    public Film updateExistFilmData(@RequestBody Film film) {
        return filmService.update(film);
    }
    @GetMapping("/{id}")
    public Film get(@PathVariable int id) {
        return filmService.get(id);
    }
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") int filmId, @PathVariable int userId) { //todo Возвращаемое тело ?
        filmService.addLike(filmId, userId);
    }
    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable("id") int filmId, @PathVariable int userId) { //todo Возвращаемое тело ?
        filmService.removeLike(filmId, userId);
    }
    @GetMapping("/popular")
    public Collection<Film> getMostPopulars(@RequestParam(defaultValue = "10") int count) { //todo Валидация ?
        return filmService.getMostPopulars(count);
    }
}
