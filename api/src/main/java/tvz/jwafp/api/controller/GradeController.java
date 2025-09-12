package tvz.jwafp.api.controller;

import org.springframework.web.bind.annotation.*;
import tvz.jwafp.api.entity.Grade;
import tvz.jwafp.api.service.GradeService;

import java.util.List;

import static tvz.jwafp.api.config.Urls.*;

@RestController
@RequestMapping(URL_GRADE)
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public List<Grade> getAll() {
        return gradeService.getAll();
    }

    @GetMapping(URL_ID)
    public Grade getById(@PathVariable String id) {
        return gradeService.getById(id);
    }

    @GetMapping(STUDENT_FILTER)
    public List<Grade> getGradesByStudent(@PathVariable String studentId) {
        return gradeService.getByStudentId(studentId);
    }

    @GetMapping(COURSE_FILTER)
    public List<Grade> getGradesByCourse(@PathVariable String courseId) {
        return gradeService.getByCourseId(courseId);
    }

    @PostMapping
    public void saveGrade(@RequestBody Grade grade) {
        gradeService.create(grade);
    }

    @PutMapping
    public void updateGrade(@RequestBody Grade grade) {
        gradeService.update(grade);
    }

    @DeleteMapping(URL_ID)
    public void deleteGrade(@PathVariable String id) {
        gradeService.deleteById(id);
    }
}
