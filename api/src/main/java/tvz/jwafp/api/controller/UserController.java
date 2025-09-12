package tvz.jwafp.api.controller;

import org.springframework.web.bind.annotation.*;
import tvz.jwafp.api.entity.Course;
import tvz.jwafp.api.entity.Grade;
import tvz.jwafp.api.entity.User;
import tvz.jwafp.api.enums.Role;
import tvz.jwafp.api.service.*;

import java.util.List;

import static tvz.jwafp.api.config.Urls.*;

@RestController
@RequestMapping(URL_USER)
public class UserController {
    private final UserService userService;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final CourseService courseService;
    private final GradeService gradeService;
    
    public UserController(UserService userService,
                          StudentService studentService,
                          ProfessorService professorService,
                          CourseService courseService,
                          GradeService gradeService) {
        this.userService = userService;
        this.studentService = studentService;
        this.professorService = professorService;
        this.courseService = courseService;
        this.gradeService = gradeService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping(URL_ID)
    public User getById(@PathVariable String id) {
        return userService.getById(id);
    }

    @GetMapping(USERNAME_FILTER)
    public User getByUsername(@PathVariable String username) {
        return userService.getByUsername(username);
    }

    @PostMapping
    public void saveUser(@RequestBody User user) {
        userService.create(user);
    }

    @PutMapping
    public void updateUser(@RequestBody User user) {
        userService.update(user);
    }

    @DeleteMapping(URL_ID)
    public void deleteGrade(@PathVariable String id) {
        User user = userService.getById(id);
        if (user.getRole().equals(Role.STUDENT)) {
            for (Grade grade : gradeService.getByStudentId(user.getUserUuid()))
                gradeService.deleteById(grade.getId());
            studentService.deleteById(user.getUserUuid());
        } else if (user.getRole().equals(Role.PROFESSOR)) {
            for (Course course : courseService.getByProfessorId(user.getUserUuid())) {
                for (Grade grade : gradeService.getByCourseId(course.getId()))
                    gradeService.deleteById(grade.getId());
                courseService.deleteById(course.getId());
            }
            professorService.deleteById(user.getUserUuid());
        }
        userService.deleteById(id);
    }
}
