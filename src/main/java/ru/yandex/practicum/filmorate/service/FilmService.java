package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }
    public Film remove(int id) {
        return filmStorage.remove(id);
    }
    public Film update(Film film) {
        return filmStorage.update(film);
    }
    public Film get(int id) {
        return filmStorage.get(id);
    }

    public void addLike(int filmId, int userId) {
        filmStorage.get(filmId).getUserIdLikes().add(userId);
    }
    public void removeLike(int filmId, int userId) {
        filmStorage.get(filmId).getUserIdLikes().remove(userId);
    }
    public Collection<Film> getMostPopulars(int limit) {
        return filmStorage.getMostPopulars(limit);
    }
}
