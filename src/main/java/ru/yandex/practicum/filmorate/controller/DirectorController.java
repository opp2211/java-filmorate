package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/directors")
@RestController
@RequiredArgsConstructor
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<Director> getAll() {
        return directorService.getAll();
    }

    @GetMapping("/{id}")
    public Director getById(@PathVariable int id) {
        return directorService.getById(id);
    }

    @PostMapping
    public Director add(@RequestBody @Valid Director director) {
        return directorService.add(director);
    }

    @PutMapping
    public Director update(@RequestBody @Valid Director director) {
        return directorService.update(director);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable int id) {
        directorService.removeById(id);
    }
}
