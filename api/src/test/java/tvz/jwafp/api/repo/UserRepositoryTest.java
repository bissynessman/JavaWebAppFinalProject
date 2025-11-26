package tvz.jwafp.api.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import tvz.jwafp.api.entity.User;
import tvz.jwafp.api.enums.Role;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@ActiveProfiles("junit")
class UserRepositoryTest {
    @Autowired UserRepository userRepository;

    User mockUser, mockUser2;

    @BeforeEach
    void setUp() {
        mockUser = new User("testuuid",
                "test@test.test",
                "testusername",
                "testpassword",
                Role.STUDENT,
                "testuseruuid");
        mockUser2 = new User("testuuid2",
                "test2@test.test",
                "testusername2",
                "testpassword2",
                Role.PROFESSOR,
                "testuseruuid2");
        userRepository.create(mockUser);
        userRepository.create(mockUser2);
    }

    @Test
    void findAll() {
        List<User> users = userRepository.findAll();

        assertEquals(2, users.size());
    }

    @Test
    void findByUsername() {
        User user = userRepository.findByUsername("testusername");

        assertEquals(mockUser.getId(), user.getId());
    }
}