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
        int MPA_CORRECT_ID = 1;
        Mpa testMpa = mpaDbStorage.get(MPA_CORRECT_ID);
        assertEquals(MPA_CORRECT_ID, testMpa.getId());
    }

    @Test
    public void testMpaWrongIdGet() {
        int MPA_INCORRECT_ID = -1;
        assertThrows(NotFoundException.class, () -> mpaDbStorage.get(MPA_INCORRECT_ID));
    }

    @Test
    public void testMpaGetAll() {
        List<Mpa> mpaList = mpaDbStorage.getAll();
        assertEquals(5, mpaList.size());
    }
}
