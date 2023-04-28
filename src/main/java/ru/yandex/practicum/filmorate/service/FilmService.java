package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final FilmGenreStorage filmGenreStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final UserLikeFilmStorage userLikeFilmStorage;
    private final DirectorStorage directorStorage;
    private final FilmDirectorStorage filmDirectorStorage;

    public Film add(Film film) {
        int newId = filmStorage.add(film);
        film.setId(newId);
        updateGenres(film);
        updateDirectors(film);
        return get(newId);
    }

    public void delete(int id) {
        get(id);
        filmStorage.delete(id);
    }

    public Film update(Film film) {
        if (!filmStorage.update(film)) {
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден!");
        }
        updateGenres(film);
        updateDirectors(film);
        return get(film.getId());
    }

    public Film get(int id) {
        try {
            Film film = filmStorage.get(id);
            return buildFilm(film);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с id = " + id + " не найден!");
        }
    }

    public List<Film> getAll() {
        List<Film> films = filmStorage.getAll();
        films.forEach(this::buildFilm);
        return films;
    }

    public void addLike(int filmId, int userId) {
        userLikeFilmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        userLikeFilmStorage.removeLike(filmId, userId);
    }

    public List<Film> getMostPopulars(int count) {
        List<Film> films = filmStorage.getMostPopulars(count);
        films.forEach(this::buildFilm);
        return films;
    }

    public List<Film> getByDirector(int directorId, String sortBy) {
        try {
            directorStorage.getById(directorId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Режиссер с id = " + directorId + " не найден!");
        }
        List<Film> films = filmStorage.getByDirector(directorId, sortBy);
        films.forEach(this::buildFilm);
        return films;
    }

    private void updateGenres(Film film) {
        filmGenreStorage.removeFilmGenre(film.getId());
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            filmGenreStorage.batchAddFilmGenre(
                    film.getId(),
                    film.getGenres().stream()
                            .mapToInt(Genre::getId)
                            .distinct()
                            .toArray());
        }
    }

    private void updateDirectors(Film film) {
        filmDirectorStorage.removeFilmDirectorsByFilmId(film.getId());
        if (film.getDirectors() != null && film.getDirectors().size() > 0) {
            filmDirectorStorage.batchAddFilmDirector(
                    film.getId(),
                    film.getDirectors().stream()
                            .mapToInt(Director::getId)
                            .distinct()
                            .toArray());
        }
    }

    private Film buildFilm(Film film) {
        film.setMpa(mpaStorage.get(film.getMpa().getId()));
        film.setGenres(genreStorage.getFilmGenres(film.getId()));
        film.setDirectors(directorStorage.getFilmDirectors(film.getId()));
        return film;
    }
}
