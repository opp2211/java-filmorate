package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriendToUser(int userId, int friendId) {
        userStorage.get(userId).getFriendsIds().add(friendId);
        userStorage.get(friendId).getFriendsIds().add(userId);
    }

    public void removeUserFriend(int userId, int friendId) {
        userStorage.get(userId).getFriendsIds().remove(friendId);
        userStorage.get(friendId).getFriendsIds().remove(userId);
    }
    public Set<User> getMutualFriends(int userId, int friendId) {
        return userStorage.get(userId).getFriendsIds().stream()
                .map(userStorage::get)
                .filter(user -> userStorage.get(friendId).getFriendsIds().contains(user))
                .collect(Collectors.toSet());
    }

}
