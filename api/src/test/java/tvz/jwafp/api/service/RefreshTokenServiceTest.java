package tvz.jwafp.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tvz.jwafp.api.auth.JwToken;
import tvz.jwafp.api.repo.RefreshTokenRepository;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {
    @Mock JwToken jwToken;
    @Mock JwToken jwToken2;

    @Mock RefreshTokenRepository refreshTokenRepository;

    RefreshTokenService refreshTokenService;

    @BeforeEach
    void setUp() {
        refreshTokenService = new RefreshTokenService(refreshTokenRepository);
        jwToken = new JwToken("testuuid", "testtoken", "testusername", new Date());
        jwToken2 = new JwToken("testuuid2", "testtoken2", "testusername2", new Date());
    }

    @Test
    void getByToken() {
        when(refreshTokenService.getByToken("testtoken2")).thenReturn(jwToken2);

        JwToken token = refreshTokenService.getByToken("testtoken2");

        assertEquals(jwToken2, token);
        verify(refreshTokenRepository).findByToken("testtoken2");
    }

    @Test
    void deleteByUsername() {
        when(refreshTokenService.deleteByUsername("testusername")).thenReturn(1);

        int result = refreshTokenService.deleteByUsername("testusername");

        assertEquals(1, result);
        verify(refreshTokenRepository).deleteByUsername("testusername");
    }
}