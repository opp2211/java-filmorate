package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
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
    private final UserService userService;

    private final FeedService feedService;

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
        get(film.getId());
        filmStorage.update(film);
        updateGenres(film);
        updateDirectors(film);
        return get(film.getId());
    }

    public Film get(int id) {
        Film film = filmStorage.get(id);
        return buildFilm(film);
    }

    public List<Film> getAll() {
        List<Film> films = filmStorage.getAll();
        films.forEach(this::buildFilm);
        return films;
    }

    public void addLike(int filmId, int userId) {
        get(filmId);
        userService.get(userId);
        userLikeFilmStorage.addLike(filmId, userId);
        feedService.addLikeEvent(userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        get(filmId);
        userService.get(userId);
        userLikeFilmStorage.removeLike(filmId, userId);
        feedService.removeLikeEvent(userId, filmId);
    }

    public List<Film> getMostPopulars(int count, Integer genreId, Integer year) {
        if (genreId != null)
            genreStorage.get(genreId);
        List<Film> films = filmStorage.getMostPopulars(count, genreId, year);
        films.forEach(this::buildFilm);
        return films;
    }

    public List<Film> getByDirector(int directorId, String sortBy) {
        directorStorage.getById(directorId);
        List<Film> films = filmStorage.getByDirector(directorId, sortBy);
        films.forEach(this::buildFilm);
        return films;
    }

    public List<Film> getUsersRecommendations(int userId) throws NotFoundException {
        userService.get(userId);
        List<Film> films = filmStorage.getUsersRecommendations(userId);
        films.forEach(this::buildFilm);
        return films;
    }

    public List<Film> search(String query, String by) {
        List<Film> films;
        String queryAddSymbols = "%" + query + "%";
        if (by.equals("director")) {
            films = filmStorage.findByDirector(queryAddSymbols);
        } else if (by.equals("title")) {
            films = filmStorage.findByName(queryAddSymbols);
        } else if (by.equals("director,title") || by.equals("title,director")) {
            films = filmStorage.findByDirectorAndName(queryAddSymbols);
        } else throw new IllegalStateException("Поиск по параметру " + by + " не предусмотрен");
        films.forEach(this::buildFilm);
        return films;
    }

    public List<Film> getCommonFilms(int userId, int friendId) throws NotFoundException {
        userService.get(userId);//Валидация id
        userService.get(friendId);//Валидация id
        List<Film> films = filmStorage.getCommonFilms(userId, friendId);
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
