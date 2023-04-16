package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public void remove(int id) {
        filmStorage.remove(id);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film get(int id) {
        return filmStorage.get(id);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getMostPopulars(int count) {
        return filmStorage.getMostPopulars(count);
    }
}
