package tvz.jwafp.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import tvz.jwafp.core.helper.Messages;
import tvz.jwafp.core.comparator.CourseComparator;
import tvz.jwafp.core.comparator.StudentComparator;
import tvz.jwafp.core.entity.*;
import tvz.jwafp.core.security.AuthenticationService;
import tvz.jwafp.core.service.CourseService;
import tvz.jwafp.core.service.GradeService;
import tvz.jwafp.core.service.ProfessorService;
import tvz.jwafp.core.service.StudentService;

import java.util.List;

import static tvz.jwafp.core.utils.ModelInitialization.initialize;
import static tvz.jwafp.core.config.Urls.*;

@Controller
@RequestMapping(URL_GRADES)
@SessionAttributes({ "userLogin", "grade" })
public class GradesController {
    private final GradeService gradeService;
    private final StudentService studentService;
    private final CourseService courseService;
    private final ProfessorService professorService;
    private final AuthenticationService authenticationService;
    private final Messages messages;

    public GradesController(GradeService gradeService,
                            StudentService studentService,
                            CourseService courseService,
                            ProfessorService professorService,
                            AuthenticationService authenticationService,
                            Messages messages) {
        this.gradeService = gradeService;
        this.studentService = studentService;
        this.courseService = courseService;
        this.professorService = professorService;
        this.authenticationService = authenticationService;
        this.messages = messages;
    }

    @GetMapping
    public String showGradesView(Model model) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        Professor professor = professorService.getProfessorById(userLogin.getUserUuid());
        initModel(model, userLogin, professor);
        return "grades";
    }

    @PostMapping
    private String processGradeInput(Model model, Grade grade) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        Professor professor = professorService.getProfessorById(userLogin.getUserUuid());
        if (professor.isAuthorized()) {
            Grade newGrade = Grade.builder()
                    .student(grade.getStudent())
                    .course(grade.getCourse())
                    .grade(grade.getGrade())
                    .build();

            gradeService.saveGrade(newGrade);
            model.addAttribute("success", messages.getMessage("success.grade-input"));
        } else
            model.addAttribute("error", messages.getMessage("error.bad-authorization"));

        initModel(model, userLogin, professor);
        return "grades";
    }

    private void initModel(Model model, User userLogin, Professor professor) {
        initialize(model, URL_GRADES);
        List<Student> studentList = studentService.getAll().stream()
                .sorted(new StudentComparator())
                .toList();
        List<Course> courseList = courseService.getCoursesByProfessor(professor.getId()).stream()
                .sorted(new CourseComparator())
                .toList();
        model.addAttribute("userLogin", userLogin);
        model.addAttribute("studentList", studentList);
        model.addAttribute("courseList", courseList);
        model.addAttribute("grade", Grade.builder().build());
    }
}
