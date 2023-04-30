package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    int add(User user);

    void delete(int id);

    void removeAll();

    void update(User user);

    User get(int id);

    List<User> getAll();

    void addFriend(int userId, int friendId, boolean isAccepted);

    void removeFriend(int userId, int friendId);

    boolean hasFriendship(int userId, int friendId);

    void updateFriendship(int userId, int friendId, boolean isAccepted);

    List<User> getFriends(int userId);

    List<User> getMutualFriends(int userId, int otherId);
}
