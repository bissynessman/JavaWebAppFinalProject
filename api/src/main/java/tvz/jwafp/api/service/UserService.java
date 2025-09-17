package tvz.jwafp.api.service;

import org.springframework.stereotype.Service;
import tvz.jwafp.api.entity.User;
import tvz.jwafp.api.repo.UserRepository;

import java.util.List;

@Service
public class UserService extends BaseService<User> {
    private final UserRepository repository;
    
    public UserService(UserRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public User getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public User create(User user) {
        repository.create(user);
        return user;
    }

    @Override
    public User update(User user) {
        repository.update(user);
        return user;
    }

    @Override
    public int deleteById(String id) {
        return repository.deleteById(id);
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username);
    }
}
