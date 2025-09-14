package tvz.jwafp.api.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import tvz.jwafp.api.auth.JwToken;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@ActiveProfiles("junit")
class RefreshTokenRepositoryTest {
    @Autowired RefreshTokenRepository refreshTokenRepository;

    JwToken jwToken, jwToken2;

    @BeforeEach
    void setUp() {
        jwToken = new JwToken("testuuid", "testtoken", "testusername", new Date());
        jwToken2 = new JwToken("testuuid2", "testtoken2", "testusername2", new Date());
        refreshTokenRepository.create(jwToken);
        refreshTokenRepository.create(jwToken2);
    }

    @Test
    void findByToken() {
        JwToken token = refreshTokenRepository.findByToken(jwToken.getToken());

        assertEquals(jwToken.getId(), token.getId());
    }

    @Test
    void deleteByUsername() {
        assertTrue(refreshTokenRepository.findById(jwToken2.getId()).isPresent());

        int result = refreshTokenRepository.deleteByUsername(jwToken2.getUsername());
        Optional<JwToken> token = refreshTokenRepository.findById(jwToken2.getId());

        assertEquals(1, result);
        assertFalse(token.isPresent());
    }
}