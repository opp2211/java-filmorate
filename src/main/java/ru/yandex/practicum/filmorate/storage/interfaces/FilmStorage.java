package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    int add(Film film);

    void delete(int id);

    void removeAll();

    void update(Film film);

    Film get(int id);

    List<Film> getAll();

    List<Film> getMostPopulars(int count, Integer genreId, Integer year);

    List<Film> getByDirector(int directorId, String sortBy);

    List<Film> getCommonFilms(int userId, int friendId);

    List<Film> getUsersRecommendations(int userId);

    List<Film> findByDirectorAndName(String query);

    List<Film> findByDirector(String query);

    List<Film> findByName(String query);
}
