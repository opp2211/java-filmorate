package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        try {
            return directorStorage.getById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Режиссер с id = " + id + " не найден!");
        }
    }

    public Director add(Director director) {
        int newId = directorStorage.add(director);
        return getById(newId);
    }

    public Director update(Director director) {
        boolean success = directorStorage.update(director);
        if (!success) {
            throw new NotFoundException("Режиссер с id = " + director.getId() + " не найден!");
        }
        return getById(director.getId());
    }

    public void removeById(int id) {
        directorStorage.removeById(id);
    }
}
