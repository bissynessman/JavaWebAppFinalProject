package tvz.jwafp.api.service;

import org.springframework.stereotype.Service;
import tvz.jwafp.api.entity.Course;
import tvz.jwafp.api.repo.CourseRepository;

import java.util.List;

@Service
public class CourseService extends BaseService<Course> {
    private final CourseRepository repository;

    public CourseService(CourseRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Course getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Course create(Course course) {
        repository.create(course);
        return course;
    }

    @Override
    public Course update(Course course) {
        repository.update(course);
        return course;
    }

    @Override
    public int deleteById(String id) {
        return repository.deleteById(id);
    }

    public List<Course> getAll() {
        return repository.findAll();
    }

    public List<Course> getByProfessorId(String professorId) {
        return repository.findByProfessorId(professorId);
    }
}
