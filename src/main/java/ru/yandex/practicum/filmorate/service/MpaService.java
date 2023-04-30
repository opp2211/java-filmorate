package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public Mpa get(int id) {
        try {
            return mpaStorage.get(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("MPA с id = " + id + " не найден!");
        }
    }

    public List<Mpa> getAll() {
        return mpaStorage.getAll();
    }
}
