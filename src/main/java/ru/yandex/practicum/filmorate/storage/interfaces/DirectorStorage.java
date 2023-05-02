package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> getAll();

    Director getById(int id);

    int add(Director director);

    void update(Director director);

    void removeById(int id);

    List<Director> getFilmDirectors(int filmId);
}