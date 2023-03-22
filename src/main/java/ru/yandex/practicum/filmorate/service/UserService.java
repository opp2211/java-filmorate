package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User add(User user) {
        return userStorage.add(user);
    }
    public User remove(int id) {
        return userStorage.remove(id);
    }
    public User update(User user) {
        return userStorage.update(user);
    }
    public User get(int id) {
        return userStorage.get(id);
    }
    public Collection<User> getAll() {
        return userStorage.getAll();
    }


    public void addFriendToUser(int userId, int friendId) {
        userStorage.get(userId).getFriendsIds().add(friendId); //todo Валидация friendId
        userStorage.get(friendId).getFriendsIds().add(userId);
    }
    public void removeUserFriend(int userId, int friendId) {
        userStorage.get(userId).getFriendsIds().remove(friendId);
        userStorage.get(friendId).getFriendsIds().remove(userId);
    }
    public Collection<User> getFriends(int userId) {
        return userStorage.get(userId).getFriendsIds().stream()
                .map(userStorage::get)
                .collect(Collectors.toSet());
    }
    public Collection<User> getMutualFriends(int userId, int friendId) {
        return userStorage.get(userId).getFriendsIds().stream()
                .filter(userStorage.get(friendId).getFriendsIds()::contains)
                .map(userStorage::get)
                .collect(Collectors.toSet());
    }

}
