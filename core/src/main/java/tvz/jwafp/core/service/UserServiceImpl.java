package tvz.jwafp.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import tvz.jwafp.core.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import tvz.jwafp.core.rest.DatabaseApi;
import tvz.jwafp.core.utils.JsonParser;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String API_URL = DatabaseApi.USERS_API;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<User> getAll() {
        List<User> users = null;
        try{
            users = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            API_URL,
                            String.class).getBody(),
                    User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User getByUsername(String username) {
        User users = null;
        try{
            users = JsonParser.parseIntoObject(
                    restTemplate.getForEntity(
                            API_URL + "/username/" + username,
                            String.class).getBody(),
                    User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User getById(String id) {
        User user = null;
        try {
            user = JsonParser.parseIntoObject(
                    restTemplate.getForEntity(
                            API_URL + "/" + id,
                            String.class).getBody(),
                    User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void saveUser(User user) {
        restTemplate.postForEntity(API_URL, user, void.class);
    }

    @Override
    public void updateUser(User user) {
        restTemplate.put(API_URL, user);
    }

    @Override
    public void deleteUser(List<String> ids) {
        for (String id : ids)
            restTemplate.delete(API_URL + "/" + id);
    }
}
