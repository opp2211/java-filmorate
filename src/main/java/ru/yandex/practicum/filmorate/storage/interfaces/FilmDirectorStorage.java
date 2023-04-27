package ru.yandex.practicum.filmorate.storage.interfaces;

public interface FilmDirectorStorage {
    void batchAddFilmDirector(int filmId, int[] genreIds);

    void removeFilmDirectorsByFilmId(int filmId);
}