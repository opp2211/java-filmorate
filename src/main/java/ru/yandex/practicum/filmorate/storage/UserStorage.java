package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User add(User user);
    User remove(int id);
    User update(User user);
    User get(int id);
    Collection<User> getAll();
}
