package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> filmsMap = new HashMap<>();
    private int idGenerator = 1;

    @Override
    public Film add(Film film) {
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
    public Film update(Film film) {
        int id = film.getId();
        if (!filmsMap.containsKey(id)) {
            throw new NotFoundException(String.format("Фильм с ID=%d не найден", id));
        }
        return filmsMap.put(id, film);
    }

    @Override
    public Film get(int id) {
        if (!filmsMap.containsKey(id)) {
            throw new NotFoundException(String.format("Фильм с ID=%d не найден", id));
        }
        return filmsMap.get(id);
    }

    @Override
    public Collection<Film> getAll() {
        return filmsMap.values();
    }

    @Override
    public Collection<Film> getMostPopulars(int count) { // Где валидировать limit ?
        return filmsMap.values().stream()
                .sorted(Comparator.comparing(film -> film.getUserIdLikes().size()))
                .limit(count)
                .collect(Collectors.toSet());
    }
}
