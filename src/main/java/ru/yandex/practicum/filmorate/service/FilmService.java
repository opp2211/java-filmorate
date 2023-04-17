package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmGenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmGenreStorage filmGenreStorage;

    public Film add(Film film) {
        int newId = filmStorage.add(film);
        film.setId(newId);
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            filmGenreStorage.batchAddFilmGenre(
                    film.getId(),
                    film.getGenres().stream()
                            .mapToInt(Genre::getId)
                            .toArray());
        }
        //todo: запрашивать и устанавливать названия жанров?
        return film;
    }

    public void remove(int id) {
        filmStorage.remove(id);
    }

    public Film update(Film film) {
        filmStorage.update(film);
        /* todo как обновить фильм-жанры?
            удалить существующие из базы, а затем положить новые? <--
            или запросить существующие, сравнить с новыми и затем удалить лишние и/или положить недостающие? */
        filmGenreStorage.removeFilmGenre(film.getId());
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            filmGenreStorage.batchAddFilmGenre(
                    film.getId(),
                    film.getGenres().stream()
                            .mapToInt(Genre::getId)
                            .toArray());
        }
        //todo: запрашивать и устанавливать названия жанров?
        //todo: проверять наличие фильма?
        //todo: ловить исключение?
        return film;
    }

    public Film get(int id) {
        try {
            return filmStorage.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с id = " + id + " не найден!");
        }
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
