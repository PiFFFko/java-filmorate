package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.yandex.practicum.filmorate.exception.EntityNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbTest {

    private final UserDbStorage userDbStorage;
    private User testUser;

    @BeforeEach
    void addUser() {
        testUser = new User(0, "mail@ya.ru", "login", "name", LocalDate.of(2000, 1, 1));
        userDbStorage.add(testUser);
    }

    @Test
    void getUserById() {
        testUser = userDbStorage.get(1);
        Assertions.assertThat(testUser).hasFieldOrPropertyWithValue("id", 1);
        Assertions.assertThat(testUser).hasFieldOrPropertyWithValue("email", "test@test.ru");
        Assertions.assertThat(testUser).hasFieldOrPropertyWithValue("login", "test");
    }

    @Test
    void removeUser() {
        testUser.setId(1);
        userDbStorage.remove(testUser);
        Assertions.assertThatThrownBy(() -> userDbStorage.get(1)).isInstanceOf(EntityNotExistException.class);
    }

    @Test
    void updateUser() {
        User updateUser = new User(0, "maeil@ya.ru", "login", "name", LocalDate.of(2000, 1, 1));
        updateUser.setId(1);
        updateUser.setLogin("updateUser");
        updateUser.setEmail("update@update.ru");
        updateUser.setBirthday(LocalDate.now());
        userDbStorage.update(updateUser);
        testUser = userDbStorage.get(1);
        Assertions.assertThat(testUser).hasFieldOrPropertyWithValue("id", 1);
        Assertions.assertThat(testUser).hasFieldOrPropertyWithValue("email", "update@update.ru");
        Assertions.assertThat(testUser).hasFieldOrPropertyWithValue("login", "updateUser");
    }

}
