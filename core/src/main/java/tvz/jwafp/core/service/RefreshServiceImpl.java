package tvz.jwafp.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tvz.jwafp.core.helper.LoginRequest;
import tvz.jwafp.core.rest.DatabaseApi;

@Service
@Primary
public class RefreshServiceImpl implements RefreshService {
    @Autowired
    private DatabaseApi databaseApi;

    private final RestTemplate restTemplate;

    public RefreshServiceImpl(@Qualifier("tokensRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getRefreshToken(LoginRequest loginRequest) {
        return restTemplate.postForEntity(databaseApi.getAuthApi(), loginRequest, String.class).getBody();
    }

    @Override
    public String getRefreshTokenForSignup(String username) {
        return restTemplate.postForEntity(databaseApi.getAuthApi() + "/signup", username, String.class).getBody();
    }

    @Override
    public String getAccessToken(String refreshToken) {
        return restTemplate.postForEntity(databaseApi.getAuthApi() + "/refresh", refreshToken, String.class).getBody();
    }

    @Override
    public String getAccessTokenForCron() {
        return restTemplate.postForEntity(databaseApi.getAuthApi() + "/cron", null, String.class).getBody();
    }
}
