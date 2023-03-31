package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserStorage userStorage;

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
        User user = userStorage.get(userId);
        User friend = userStorage.get(friendId);
        user.getFriendsIds().add(friendId);
        friend.getFriendsIds().add(userId);
    }
    public void removeUserFriend(int userId, int friendId) {
        userStorage.get(userId).getFriendsIds().remove(friendId);
        userStorage.get(friendId).getFriendsIds().remove(userId);
    }
    public Collection<User> getFriends(int userId) {
        return userStorage.get(userId).getFriendsIds().stream()
                .map(userStorage::get)
                .sorted(Comparator.comparing(User::getId)) //сортировка для тестов postman
                .collect(Collectors.toList());
    }
    public Collection<User> getMutualFriends(int userId, int friendId) {
        return userStorage.get(userId).getFriendsIds().stream()
                .filter(userStorage.get(friendId).getFriendsIds()::contains)
                .map(userStorage::get)
                .collect(Collectors.toSet());
    }

}
