package tvz.jwafp.core.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class TimeApi {
    private static final String API_URL = "https://api.api-ninjas.com/v1/worldtime?timezone=Europe/Zagreb";
    private static final String X_API_HEADER = "X-Api-Key";
    private static final String API_KEY = "ql3+i8rS/RDUrUHd591kJg==hG8YGGDTeWNTly2q";
    private static final String DATETIME_RESULT = "datetime";

    public LocalDateTime getCurrentTime() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set(X_API_HEADER, API_KEY);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, request, String.class);

            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = mapper.readValue(response.getBody(), new TypeReference<>() {});

            if (map != null) {
                String dateTime = map.get(DATETIME_RESULT).replace(" ", "T");
                return LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return LocalDateTime.now();
    }
}
