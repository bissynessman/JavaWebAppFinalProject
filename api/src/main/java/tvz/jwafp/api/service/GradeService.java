package tvz.jwafp.api.service;

import org.springframework.stereotype.Service;
import tvz.jwafp.api.entity.Grade;
import tvz.jwafp.api.repo.GradeRepository;

import java.util.List;

@Service
public class GradeService extends BaseService<Grade> {
    private final GradeRepository repository;

    public GradeService(GradeRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Grade getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Grade create(Grade grade) {
        repository.create(grade);
        return grade;
    }

    @Override
    public Grade update(Grade grade) {
        repository.update(grade);
        return grade;
    }

    @Override
    public int deleteById(String id) {
        return repository.deleteById(id);
    }

    public List<Grade> getAll() {
        return repository.findAll();
    }

    public List<Grade> getByStudentId(String studentId) {
        return repository.findByStudentId(studentId);
    }

    public List<Grade> getByCourseId(String courseId) {
        return repository.findByCourseId(courseId);
    }
}
