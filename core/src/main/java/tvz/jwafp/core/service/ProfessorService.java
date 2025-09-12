package tvz.jwafp.core.service;

import tvz.jwafp.core.entity.Professor;

import java.util.List;

public interface ProfessorService {
    List<Professor> getAll();
    Professor getProfessorById(String id);
    List<Professor> getUnauthorizedProfessors();
    void saveProfessor(Professor professor);
    void updateProfessor(Professor professor);
    void authorizeProfessor(String id);
    void deleteProfessors(List<String> ids);
}
