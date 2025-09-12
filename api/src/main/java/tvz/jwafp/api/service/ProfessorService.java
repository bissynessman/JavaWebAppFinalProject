package tvz.jwafp.api.service;

import org.springframework.stereotype.Service;
import tvz.jwafp.api.entity.Professor;
import tvz.jwafp.api.repo.ProfessorRepository;

import java.util.List;

@Service
public class ProfessorService extends BaseService<Professor> {
    private final ProfessorRepository professorRepository;

    public ProfessorService(ProfessorRepository professorRepository) {
        super(professorRepository);
        this.professorRepository = professorRepository;
    }

    @Override
    public Professor getById(String id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Professor create(Professor professor) {
        repository.create(professor);
        return professor;
    }

    @Override
    public Professor update(Professor professor) {
        repository.update(professor);
        return professor;
    }

    @Override
    public int deleteById(String id) {
        return repository.deleteById(id);
    }

    public List<Professor> getAll() {
        return professorRepository.findAll();
    }

    public List<Professor> getAllUnauthorized() {
        return professorRepository.findAllUnauthorized();
    }

    public void authorize(String id) {
        professorRepository.authorize(id);
    }
}
