package tvz.jwafp.core.service;

import tvz.jwafp.core.helper.LoginRequest;

public interface RefreshService {
    String getRefreshToken(LoginRequest loginRequest);
    String getRefreshTokenForSignup(String username);
    String getAccessToken(String refreshToken);
    String getAccessTokenForCron();
}
