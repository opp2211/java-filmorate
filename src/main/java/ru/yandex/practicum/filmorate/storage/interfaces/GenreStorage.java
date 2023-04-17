package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    Genre get(int id);

    List<Genre> getAll();

    void addFilmGenre(int filmId, int genreId);

    List<Genre> getFilmGenres(int filmId);

    void removeAllFilmGenres();
}
