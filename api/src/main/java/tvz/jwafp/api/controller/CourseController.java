package tvz.jwafp.api.controller;

import org.springframework.web.bind.annotation.*;
import tvz.jwafp.api.entity.Course;
import tvz.jwafp.api.entity.Grade;
import tvz.jwafp.api.service.CourseService;
import tvz.jwafp.api.service.GradeService;

import java.util.List;

import static tvz.jwafp.api.config.Urls.*;

@RestController
@RequestMapping(URL_COURSE)
public class CourseController {
    private final CourseService courseService;
    private final GradeService gradeService;

    public CourseController(CourseService courseService, GradeService gradeService) {
        this.courseService = courseService;
        this.gradeService = gradeService;
    }

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAll();
    }

    @GetMapping(URL_ID)
    public Course getCourseById(@PathVariable String id) {
        return courseService.getById(id);
    }

    @GetMapping(PROFESSOR_FILTER)
    public List<Course> getGradesByStudent(@PathVariable String professorId) {
        return courseService.getByProfessorId(professorId);
    }

    @PostMapping
    public void saveGrade(@RequestBody Course course) {
        courseService.create(course);
    }

    @PutMapping
    public void updateGrade(@RequestBody Course course) {
        courseService.update(course);
    }

    @DeleteMapping(URL_ID)
    public void deleteCourse(@PathVariable String id) {
        for (Grade grade : gradeService.getByCourseId(id))
            gradeService.deleteById(grade.getId());
        courseService.deleteById(id);
    }
}
