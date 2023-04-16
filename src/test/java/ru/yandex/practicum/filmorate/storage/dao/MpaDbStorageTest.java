package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void testMpaGet() {
        int mpaCorrectId = 1;
        Mpa testMpa = mpaDbStorage.get(mpaCorrectId);
        assertEquals(mpaCorrectId, testMpa.getId());
    }

    @Test
    public void testMpaWrongIdGet() {
        int mpaIncorrectId = -1;
        assertThrows(NotFoundException.class, () -> mpaDbStorage.get(mpaIncorrectId));
    }

    @Test
    public void testMpaGetAll() {
        List<Mpa> mpaList = mpaDbStorage.getAll();
        assertEquals(5, mpaList.size());
    }
}
