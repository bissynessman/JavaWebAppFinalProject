package tvz.jwafp.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tvz.jwafp.api.entity.Professor;
import tvz.jwafp.api.repo.ProfessorRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfessorServiceTest {
    @Mock Professor professor;
    @Mock Professor professor2;

    @Mock ProfessorRepository professorRepository;

    ProfessorService professorService;

    @BeforeEach
    void setUp() {
        professorService = new ProfessorService(professorRepository);
        professor  = new Professor("testuuid", "test", "professor", false);
        professor2 = new Professor("testuuid2", "test", "professor2", true);
    }

    @Test
    void getAll() {
        when(professorService.getAll()).thenReturn(List.of(professor, professor2));

        List<Professor> professors = professorService.getAll();

        assertEquals(2, professors.size());
        verify(professorRepository).findAll();
    }

    @Test
    void getAllUnauthorized() {
        when(professorService.getAllUnauthorized()).thenReturn(List.of(professor));

        List<Professor> professors = professorService.getAllUnauthorized();

        assertEquals("testuuid", professors.getFirst().getId());
        verify(professorRepository).findAllUnauthorized();
    }

    @Test
    void authorize() {
        professorService.authorize(professor.getId());

        verify(professorRepository).authorize(professor.getId());
    }
}