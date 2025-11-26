package tvz.jwafp.api.service;

import org.springframework.stereotype.Service;
import tvz.jwafp.api.entity.Assignment;
import tvz.jwafp.api.repo.AssignmentRepository;

import java.util.List;

@Service
public class AssignmentService extends BaseService<Assignment> {
    private final AssignmentRepository repository;

    public AssignmentService(AssignmentRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    public Assignment getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Assignment create(Assignment assignment) {
        repository.create(assignment);
        return assignment;
    }

    @Override
    public Assignment update(Assignment assignment) {
        repository.update(assignment);
        return assignment;
    }

    @Override
    public int deleteById(String id) {
        return repository.deleteById(id);
    }

    public List<Assignment> getAll() {
        return repository.findAll();
    }

    public List<Assignment> getActiveForStudent(String studentId) {
        return repository.findActiveForStudent(studentId);
    }

    public List<Assignment> getAllForCourse(String courseId) {
        return repository.findAllForCourse(courseId);
    }

    public List<Assignment> getAllForAssigment(String assigmentId) {
        return repository.findAllForAssignment(assigmentId);
    }
}
