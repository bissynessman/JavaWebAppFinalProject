package tvz.jwafp.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
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
    private final LocaleResolver localeResolver;

    public ViewStudentsController(StudentService studentService,
                                  AuthenticationService authenticationService,
                                  LocaleResolver localeResolver) {
        this.studentService = studentService;
        this.authenticationService = authenticationService;
        this.localeResolver = localeResolver;
    }

    @GetMapping
    public String showStudentsView(Model model, HttpServletRequest request) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");

        initModel(model, false, localeResolver, request);
        return "students";
    }

    @PostMapping
    public String handleSort(Model model, @ModelAttribute MajorWrapper majorFilter, HttpServletRequest request) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");

        boolean filter = false;
        if (majorFilter.getMajor() != null) {
            List<Student> studentList = studentService.getAll().stream()
                    .filter(student -> student.getMajor().equals(majorFilter.getMajor()))
                    .toList();
            model.addAttribute("studentList", studentList);
            filter = true;
        }
        initModel(model, filter, localeResolver, request);
        return "students";
    }

    private void initModel(Model model, Boolean filter, LocaleResolver localeResolver, HttpServletRequest request) {
        initialize(model, URL_VIEW_STUDENTS, localeResolver, request);
        if (!filter) {
            List<Student> studentList = studentService.getAll().stream()
                    .sorted(new StudentComparator())
                    .toList();
            model.addAttribute("studentList", studentList);
        }
        model.addAttribute("majorFilter", new MajorWrapper());
    }
}
