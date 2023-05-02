package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.interfaces.DirectorStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private final DirectorStorage directorStorage;

    public List<Director> getAll() {
        return directorStorage.getAll();
    }

    public Director getById(int id) {
        return directorStorage.getById(id);
    }

    public Director add(Director director) {
        int newId = directorStorage.add(director);
        return getById(newId);
    }

    public Director update(Director director) {
        getById(director.getId());
        directorStorage.update(director);
        return getById(director.getId());
    }

    public void removeById(int id) {
        getById(id);
        directorStorage.removeById(id);
    }
}
