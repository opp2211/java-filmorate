package ru.yandex.practicum.filmorate.storage.interfaces;

public interface FilmGenreStorage {
    void batchAddFilmGenre(int filmId, int[] genreIds);

    void removeFilmGenre(int filmId);
}
