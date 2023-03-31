package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

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
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    public void addLike(int filmId, int userId) {
        if (userService.get(userId) != null) {
            filmStorage.get(filmId).getUserIdLikes().add(userId);
        }
    }
    public void removeLike(int filmId, int userId) {
        Set<Integer> userIdLikes = filmStorage.get(filmId).getUserIdLikes();
        if (!userIdLikes.contains(userId)) {
            throw new NotFoundException(String.format("Лайк пользователя (userId=%d) фильму (filmId=%d) не найден", userId, filmId));
        }
        filmStorage.get(filmId).getUserIdLikes().remove(userId);
    }
    public Collection<Film> getMostPopulars(int count) {
        return filmStorage.getMostPopulars(count);
    }
}
