package tvz.jwafp.api.controller;

import org.springframework.web.bind.annotation.*;
import tvz.jwafp.api.auth.JwToken;
import tvz.jwafp.api.auth.JwtGenerator;
import tvz.jwafp.api.auth.LoginRequest;
import tvz.jwafp.api.auth.PasswordUtils;
import tvz.jwafp.api.entity.User;
import tvz.jwafp.api.service.RefreshTokenService;
import tvz.jwafp.api.service.UserService;

import java.util.Date;

import static tvz.jwafp.api.config.Urls.*;

@RestController
@RequestMapping(URL_AUTH)
public class AuthController {
    private static final long FIFTEEN_SECONDS = 15000;
    private static final long TEN_MINUTES = 600000;
    private static final long ONE_WEEK = 604800000;

    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    public AuthController(RefreshTokenService refreshTokenService, UserService userService) {
        this.refreshTokenService = refreshTokenService;
        this.userService = userService;
    }

    @PostMapping
    public String generateRefreshToken(@RequestBody LoginRequest loginRequest) {
        User user = userService.getByUsername(loginRequest.getUsername());
        if (user != null) {
            String password = user.getPassword();
            String username = user.getUsername();
            if (PasswordUtils.verifyPassword(password, loginRequest.getPassword(), username)) {
                JwToken refreshToken = JwtGenerator.makeToken(user.getUsername(), ONE_WEEK);
                refreshTokenService.deleteByUsername(user.getUsername());
                return refreshTokenService.create(refreshToken).getToken();
            }
        }

        return null;
    }

    @PostMapping(URL_SIGNUP)
    public String generateRefreshTokenAtSignup(@RequestBody String username) {
        if (username != null && !username.isEmpty() && !username.isBlank()) {
            JwToken refreshToken = JwtGenerator.makeToken(username, ONE_WEEK);
            return refreshTokenService.create(refreshToken).getToken();
        }

        return null;
    }

    @PostMapping(URL_CRON)
    public String generateAccessTokenForCron() {
        return JwtGenerator.makeToken("cronjob", TEN_MINUTES).getToken();
    }

    @PostMapping(URL_REFRESH)
    public String refreshAccessToken(@RequestBody String refreshTokenValue) {
        JwToken refreshToken = refreshTokenService.getByToken(refreshTokenValue);
        if (refreshToken != null && refreshToken.getValidUntil().after(new Date()))
            return JwtGenerator.makeToken(refreshToken.getUsername(), FIFTEEN_SECONDS).getToken();

        return null;
    }
}
