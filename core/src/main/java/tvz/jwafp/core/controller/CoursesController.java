package tvz.jwafp.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.jwafp.core.helper.Messages;
import tvz.jwafp.core.comparator.CourseComparator;
import tvz.jwafp.core.helper.BooleanWrapper;
import tvz.jwafp.core.helper.DeleteBuffer;
import tvz.jwafp.core.helper.CourseWrapper;
import tvz.jwafp.core.entity.Course;
import tvz.jwafp.core.entity.Professor;
import tvz.jwafp.core.entity.User;
import tvz.jwafp.core.security.AuthenticationService;
import tvz.jwafp.core.service.CourseService;
import tvz.jwafp.core.service.ProfessorService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static tvz.jwafp.core.utils.ModelInitialization.initialize;
import static tvz.jwafp.core.config.Urls.*;

@Controller
@RequestMapping(URL_COURSES)
@SessionAttributes("userLogin")
public class CoursesController {
    private final CourseService courseService;
    private final ProfessorService professorService;
    private final AuthenticationService authenticationService;
    private final Messages messages;
    private final LocaleResolver localeResolver;

    public CoursesController(CourseService courseService,
                             ProfessorService professorService,
                             AuthenticationService authenticationService,
                             Messages messages,
                             LocaleResolver localeResolver) {
        this.courseService = courseService;
        this.professorService = professorService;
        this.authenticationService = authenticationService;
        this.messages = messages;
        this.localeResolver = localeResolver;
    }

    @GetMapping
    public String showCoursesView(Model model, HttpServletRequest request) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        initModel(model, localeResolver, request);
        return "courses";
    }

    @PostMapping(URL_DELETE)
    public String processUpdates(
            Model model, RedirectAttributes redirectAttributes, @ModelAttribute DeleteBuffer courseBuffer) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        List<String> coursesToDelete = courseBuffer.getItems().entrySet().stream()
                .filter(entry -> entry.getValue().getValue())
                .map(Map.Entry::getKey)
                .toList();

        if (!coursesToDelete.isEmpty()) {
            courseService.deleteCourses(coursesToDelete);
            redirectAttributes.addFlashAttribute("success", messages.getMessage("success.changes-saved"));
        } else
            redirectAttributes.addFlashAttribute("error", messages.getMessage("error.missing-course"));

        return "redirect:" + URL_COURSES;
    }

    @PostMapping
    public String processAddCourse(Model model, @ModelAttribute CourseWrapper newCourse, HttpServletRequest request) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");

        newCourse.getCourse().setProfessor(professorService.getProfessorById(
                newCourse.getProfessorId()).getId());
        courseService.saveCourse(newCourse.getCourse());

        initModel(model, localeResolver, request);
        return "courses";
    }

    private void initModel(Model model, LocaleResolver localeResolver, HttpServletRequest request) {
        initialize(model, URL_COURSES, localeResolver, request);
        List<Course> coursesList = courseService.getAll().stream()
                .sorted(new CourseComparator())
                .toList();
        DeleteBuffer courseBuffer = DeleteBuffer.builder()
                .items(courseService.getAll().stream()
                        .collect(Collectors.toMap(Course::getId, course -> new BooleanWrapper(false))))
                .build();
        List<Professor> professorList = professorService.getAll();
        model.addAttribute("coursesList", coursesList);
        model.addAttribute("courseBuffer", courseBuffer);
        model.addAttribute("newCourse", new CourseWrapper());
        model.addAttribute("professorList", professorList);
    }
}
