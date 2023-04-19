package ru.yandex.practicum.filmorate.storage.interfaces;

public interface UserLikeFilmStorage {
    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);
}
