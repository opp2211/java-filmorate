package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

    public User add(User user) {
        return userStorage.add(user);
    }

    public void remove(int id) {
        userStorage.remove(id);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User get(int id) {
        return userStorage.get(id);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }


    public void addFriendToUser(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
    }

    public void removeUserFriend(int userId, int friendId) {
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        return userStorage.getFriends(userId);
    }

    public Collection<User> getMutualFriends(int userId, int friendId) {
        return userStorage.getMutualFriends(userId, friendId);
    }

}
