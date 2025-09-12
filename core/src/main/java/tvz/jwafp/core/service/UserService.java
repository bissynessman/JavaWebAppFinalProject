package tvz.jwafp.core.service;

import tvz.jwafp.core.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAll();
    User getByUsername(String username);
    User getById(String id);
    void saveUser(User user);
    void updateUser(User user);
    void deleteUser(List<String> ids);
}
