package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

public interface UserStorage {
    User add(User user);
    User remove(int id);
    User update(User user);
}
