package tvz.jwafp.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import tvz.jwafp.core.helper.JwtHolder;
import tvz.jwafp.core.security.RequestInterceptor;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Autowired
    private JwtHolder jwtHolder;
    @Autowired
    private AppProperties appProperties;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new RequestInterceptor(() -> jwtHolder));
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    public RestTemplate tokensRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate cronRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            request.getHeaders().set("X-API-KEY", appProperties.getCronApiKey());
            return execution.execute(request, body);
        });
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }

    @Bean
    public RestTemplate apiRestTemplate() {
        return new RestTemplate();
    }
}
