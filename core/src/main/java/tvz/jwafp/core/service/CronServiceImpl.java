package tvz.jwafp.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tvz.jwafp.core.entity.Student;
import tvz.jwafp.core.rest.DatabaseApi;
import tvz.jwafp.core.utils.JsonParser;

import java.util.List;

@Service
@Primary
public class CronServiceImpl implements CronService {
    private static final String API_URL = DatabaseApi.CRON_API;

    private final RestTemplate restTemplate;

    @Autowired
    public CronServiceImpl(@Qualifier("cronRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = null;
        try{
            students = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            API_URL + "/students",
                            String.class).getBody(),
                    Student.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public String getEmailByUserId(String userId) {
        String email = null;
        try {
            email = restTemplate.getForEntity(API_URL + "/email/" + userId, String.class).getBody();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return email;
    }
}
