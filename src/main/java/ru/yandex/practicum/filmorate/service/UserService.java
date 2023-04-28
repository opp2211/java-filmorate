package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User add(User user) {
        int newId = userStorage.add(user);
        return get(newId);
    }

    public void delete(int id) {
        get(id);
        userStorage.delete(id);
    }

    public User update(User user) {
        get(user.getId());
        userStorage.update(user);
        return get(user.getId());
    }

    public User get(int id) {
        try {
            return userStorage.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден!");
        }
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }


    public void addFriendToUser(int userId, int friendId) {
        get(userId);
        get(friendId);
        boolean isAccepted = false;
        if (userStorage.hasFriendship(friendId, userId)) {
            isAccepted = true;
            userStorage.updateFriendship(friendId, userId, true);
        }
        userStorage.addFriend(userId, friendId, isAccepted);
    }

    public void removeUserFriend(int userId, int friendId) {
        userStorage.removeFriend(userId, friendId);
        userStorage.updateFriendship(friendId, userId, false);
    }

    public List<User> getFriends(int userId) {
        get(userId);
        return userStorage.getFriends(userId);
    }

    public Collection<User> getMutualFriends(int userId, int friendId) {
        return userStorage.getMutualFriends(userId, friendId);
    }
}
