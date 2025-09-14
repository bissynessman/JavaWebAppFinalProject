package tvz.jwafp.api.controller;

import org.springframework.web.bind.annotation.*;
import tvz.jwafp.api.entity.Course;
import tvz.jwafp.api.entity.Professor;
import tvz.jwafp.api.service.CourseService;
import tvz.jwafp.api.service.ProfessorService;

import java.util.List;

import static tvz.jwafp.api.config.Urls.*;

@RestController
@RequestMapping(URL_PROFESSOR)
public class ProfessorController {
    private final ProfessorService professorService;
    private final CourseService courseService;

    public ProfessorController(ProfessorService professorService, CourseService courseService) {
        this.professorService = professorService;
        this.courseService = courseService;
    }

    @GetMapping
    public List<Professor> getAll() {
        return professorService.getAll();
    }

    @GetMapping(URL_ID)
    public Professor getById(@PathVariable String id) {
        return professorService.getById(id);
    }

    @GetMapping(URL_UNAUTHORIZED)
    public List<Professor> getUnauthorized() {
        return professorService.getAllUnauthorized();
    }

    @PostMapping
    public void saveProfessor(@RequestBody Professor professor) {
        professorService.create(professor);
    }

    @PutMapping
    public void updateProfessor(@RequestBody Professor professor) {
        professorService.update(professor);
    }

    @PutMapping(URL_AUTHORIZE)
    public void authorize(@RequestBody Professor professor) {
        professorService.authorize(professor.getId());
    }

    @DeleteMapping(URL_ID)
    public void deleteGrade(@PathVariable String id) {
        for (Course course : courseService.getByProfessorId(id)) {
            course.setProfessor(null);
            courseService.update(course);
        }
        professorService.deleteById(id);
    }
}
