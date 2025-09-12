package tvz.jwafp.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tvz.jwafp.api.entity.User;
import tvz.jwafp.api.enums.Role;
import tvz.jwafp.api.repo.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock User user;
    @Mock User user2;

    @Mock UserRepository userRepository;

    UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        user = new User("testuuid", "test@test.test", "testusername", "testpassword", Role.STUDENT, "testuseruuid");
        user2 = new User("testuuid2", "test2@test.test", "testusername2", "testpassword", Role.PROFESSOR, "testuseruuid2");
    }

    @Test
    void getAll() {
        when(userService.getAll()).thenReturn(List.of(user, user2));

        List<User> users = userService.getAll();

        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void getByUsername() {
        when(userService.getByUsername("testusername")).thenReturn(user);

        User result = userService.getByUsername("testusername");

        assertEquals(user, result);
        verify(userRepository).findByUsername("testusername");
    }
}