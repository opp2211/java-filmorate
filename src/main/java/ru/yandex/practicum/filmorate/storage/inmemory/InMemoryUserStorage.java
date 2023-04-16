//package ru.yandex.practicum.filmorate.storage.inmemory;
//
//import org.springframework.stereotype.Component;
//import ru.yandex.practicum.filmorate.exception.NotFoundException;
//import ru.yandex.practicum.filmorate.model.User;
//import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class InMemoryUserStorage implements UserStorage {
//    private final Map<Integer, User> usersMap = new HashMap<>();
//    private int idGenerator = 1;
//
//    @Override
//    public User add(User user) {
//        int newId = idGenerator++;
//        usersMap.put(newId, user);
//        user.setId(newId);
//        return user;
//    }
//
//    @Override
//    public User remove(int id) {
//        if (!usersMap.containsKey(id)) {
//            throw new NotFoundException(String.format("Пользователь с ID=%d не найден", id));
//        }
//        return usersMap.remove(id);
//    }
//
//    @Override
//    public User update(User user) {
//        int id = user.getId();
//        if (!usersMap.containsKey(id)) {
//            throw new NotFoundException(String.format("Пользователь с ID=%d не найден", id));
//        }
//        usersMap.put(id, user);
//        return user;
//    }
//
//    @Override
//    public User get(int id) {
//        if (!usersMap.containsKey(id)) {
//            throw new NotFoundException(String.format("Пользователь с ID=%d не найден", id));
//        }
//        return usersMap.get(id);
//    }
//
//    @Override
//    public List<User> getAll() {
//        return usersMap.values();
//    }
//}
