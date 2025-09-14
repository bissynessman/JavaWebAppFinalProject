package tvz.jwafp.api.service;

import org.springframework.stereotype.Service;
import tvz.jwafp.api.entity.Student;
import tvz.jwafp.api.repo.StudentRepository;

import java.util.List;

@Service
public class StudentService extends BaseService<Student> {
    private final StudentRepository repository;
    
    public StudentService(StudentRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Student getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Student create(Student student) {
        repository.create(student);
        return student;
    }

    @Override
    public Student update(Student student) {
        repository.update(student);
        return student;
    }

    @Override
    public int deleteById(String id) {
        return repository.deleteById(id);
    }

    public List<Student> getAll() {
        return repository.findAll();
    }
}
