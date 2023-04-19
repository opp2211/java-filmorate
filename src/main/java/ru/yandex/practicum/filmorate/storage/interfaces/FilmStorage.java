package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    int add(Film film);

    void remove(int id);

    void removeAll();

    boolean update(Film film);

    Film get(int id);

    List<Film> getAll();

    List<Film> getMostPopulars(int count);
}
