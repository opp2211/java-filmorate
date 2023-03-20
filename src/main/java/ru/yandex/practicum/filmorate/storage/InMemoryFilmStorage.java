package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmsMap = new HashMap<>();
    private int idGenerator = 0;

    @Override
    public Film add(@Valid Film film) {
        int newId = idGenerator++;
        filmsMap.put(newId, film);
        film.setId(newId);
        return film;
    }

    @Override
    public Film remove(int id) {
        if (!filmsMap.containsKey(id)) {
            throw new NotFoundException(String.format("Фильм с ID=%d не найден", id));
        }
        return filmsMap.remove(id);
    }

    @Override
    public Film update(@Valid Film film) {
        int id = film.getId();
        if (!filmsMap.containsKey(id)) {
            throw new NotFoundException(String.format("Фильм с ID=%d не найден", id));
        }
        return filmsMap.put(id, film);
    }
}
