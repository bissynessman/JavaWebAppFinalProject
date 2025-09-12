package tvz.jwafp.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tvz.jwafp.core.comparator.StudentComparator;
import tvz.jwafp.core.helper.MajorWrapper;
import tvz.jwafp.core.entity.Student;
import tvz.jwafp.core.entity.User;
import tvz.jwafp.core.security.AuthenticationService;
import tvz.jwafp.core.service.StudentService;

import java.util.List;

import static tvz.jwafp.core.utils.ModelInitialization.initialize;
import static tvz.jwafp.core.config.Urls.*;

@Controller
@RequestMapping(URL_VIEW_STUDENTS)
@SessionAttributes({ "userLogin", "majorFilter" })
public class ViewStudentsController {
    private final StudentService studentService;
    private final AuthenticationService authenticationService;

    public ViewStudentsController(StudentService studentService, AuthenticationService authenticationService) {
        this.studentService = studentService;
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String showStudentsView(Model model) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");

        initModel(model, false);
        return "students";
    }

    @PostMapping
    public String handleSort(Model model, @ModelAttribute MajorWrapper majorFilter) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");

        if (majorFilter.getMajor() != null) {
            List<Student> studentList = studentService.getAll().stream()
                    .filter(student -> student.getMajor().equals(majorFilter.getMajor()))
                    .toList();
            model.addAttribute("studentList", studentList);
            initModel(model, true);
        } else initModel(model, false);
        return "students";
    }

    private void initModel(Model model, Boolean filter) {
        initialize(model, URL_VIEW_STUDENTS);
        if (!filter) {
            List<Student> studentList = studentService.getAll().stream()
                    .sorted(new StudentComparator())
                    .toList();
            model.addAttribute("studentList", studentList);
        }
        model.addAttribute("majorFilter", new MajorWrapper());
    }
}
