package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film add(Film film);
    Film remove(int id);
    Film update(Film film);
    Film get(int id);
    Collection<Film> getMostPopulars(int limit); //Какой тип лучше использовать Collection/List ?
}
