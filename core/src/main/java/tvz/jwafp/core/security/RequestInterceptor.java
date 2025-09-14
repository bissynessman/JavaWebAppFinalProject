package tvz.jwafp.core.security;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import tvz.jwafp.core.helper.JwtHolder;

import java.io.IOException;
import java.util.function.Supplier;

public class RequestInterceptor implements ClientHttpRequestInterceptor {
    private final Supplier<JwtHolder> tokenSupplier;

    public RequestInterceptor(Supplier<JwtHolder> tokenSupplier) {
        this.tokenSupplier = tokenSupplier;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().add("Authorization", "Bearer " + tokenSupplier.get().getAccessToken());
        return execution.execute(request, body);
    }
}
