package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmStorage {
    Film add(Film film);
    Film remove(int id);
    Film update(Film film);
}
