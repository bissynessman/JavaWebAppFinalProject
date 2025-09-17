package tvz.jwafp.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import tvz.jwafp.core.entity.Assignment;
import tvz.jwafp.core.entity.Course;
import tvz.jwafp.core.entity.Professor;
import tvz.jwafp.core.entity.User;
import tvz.jwafp.core.enums.Role;
import tvz.jwafp.core.helper.Messages;
import tvz.jwafp.core.rest.AiDetectionService;
import tvz.jwafp.core.security.AuthenticationService;
import tvz.jwafp.core.service.AssignmentService;
import tvz.jwafp.core.service.CourseService;
import tvz.jwafp.core.service.ProfessorService;

import java.util.List;
import java.util.Map;

import static tvz.jwafp.core.config.Urls.*;
import static tvz.jwafp.core.utils.ModelInitialization.initialize;

@Controller
@RequestMapping(URL_ASSIGNMENT)
@SessionAttributes("userLogin")
public class AssignmentsController {
    private final AssignmentService assignmentService;
    private final CourseService courseService;
    private final ProfessorService professorService;
    private final AuthenticationService authenticationService;
    private final AiDetectionService aiDetectionService;
    private final Messages messages;
    private final LocaleResolver localeResolver;

    public AssignmentsController(AssignmentService assignmentService,
                                 CourseService courseService,
                                 ProfessorService professorService,
                                 AuthenticationService authenticationService,
                                 AiDetectionService aiDetectionService,
                                 Messages messages,
                                 LocaleResolver localeResolver) {
        this.assignmentService = assignmentService;
        this.courseService = courseService;
        this.professorService = professorService;
        this.authenticationService = authenticationService;
        this.aiDetectionService = aiDetectionService;
        this.messages = messages;
        this.localeResolver = localeResolver;
    }

    @GetMapping
    public String assignments(Model model, HttpServletRequest request) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        if (!userLogin.getRole().equals(Role.PROFESSOR))
            return "redirect:" + URL_INDEX;
        initModelProfessor(model, userLogin.getUserUuid(), localeResolver, request);
        return "assignments";
    }

    @PostMapping
    public String saveAssignment(Model model, Assignment newAssignment, HttpServletRequest request) {
        authenticationService.refresh();
        assignmentService.saveAssignment(newAssignment);
        model.addAttribute("success", messages.getMessage("assignment.save-success"));
        User userLogin = (User) model.getAttribute("userLogin");
        if (!userLogin.getRole().equals(Role.PROFESSOR))
            return "redirect:" + URL_INDEX;
        initModelProfessor(model, userLogin.getUserUuid(), localeResolver, request);
        return "assignments";
    }

    @GetMapping(URL_ASSIGNMENT_COURSE)
    public String assignmentsByCourse(@PathVariable String courseId, Model model, HttpServletRequest request) {
        authenticationService.refresh();
        initModelCourse(model, courseId, localeResolver, request);
        return "assignments-course";
    }

    @PostMapping(URL_ASSIGNMENT_COURSE)
    public String saveAssignmentCourse(
            @PathVariable String courseId, Model model, Assignment newAssignment, HttpServletRequest request) {
        authenticationService.refresh();
        assignmentService.saveAssignment(newAssignment);
        model.addAttribute("success", messages.getMessage("assignment.save-success"));
        initModelCourse(model, courseId, localeResolver, request);
        return "assignments-course";
    }

    @GetMapping(URL_ASSIGNMENT_ID)
    public String assignment(@RequestParam(required = false, defaultValue = "false") Boolean professor,
                             @PathVariable String assignmentId,
                             Model model,
                             HttpServletRequest request) {
        authenticationService.refresh();
        model.addAttribute("professor", professor);
        initModel(model, assignmentId, localeResolver, request);
        return "assignment";
    }

    @PostMapping(URL_ASSIGNMENT_ID)
    public String updateAssignment(@PathVariable String assignmentId,
                                   @RequestParam(required = false, defaultValue = "false") Boolean professor,
                                   Model model,
                                   Assignment assignment,
                                   HttpServletRequest request) {
        User userLogin = (User) model.getAttribute("userLogin");
        authenticationService.refresh();
        assignment.setId(assignmentId);

        Professor professorObj = professorService.getProfessorById(userLogin.getUserUuid());
        if (professorObj == null || professorObj.isAuthorized()) {
            assignmentService.updateAssignment(assignment);
            model.addAttribute("success", messages.getMessage("assignment.update-success"));
        } else
            model.addAttribute("error", messages.getMessage("error.bad-authorization"));

        model.addAttribute("professor", professor);
        initModel(model, assignmentId, localeResolver, request);
        return "assignment";
    }

    @GetMapping(URL_ASSIGNMENT_DETECT)
    public String detect(@PathVariable String assignmentId, Model model, HttpServletRequest request) {
        model.addAttribute("professor", true);
        authenticationService.refresh();
        initModel(model, assignmentId, localeResolver, request);
        Assignment assignment = (Assignment) model.getAttribute("assignment");
        String content = assignment.getContent();
        if (!content.isBlank()) {
            Map<String, Object> result = aiDetectionService.check(content);
            model.addAttribute("detectionResult", result.get("classification"));
            model.addAttribute("detectionLevel", result.get("classificationLevel"));
        } else {
            model.addAttribute("detectionResult", messages.getMessage("detection.result-uncertain"));
            model.addAttribute("detectionLevel", "yellow");
        }
        return "assignment";
    }

    private void initModel(
            Model model, String assignmentId, LocaleResolver localeResolver, HttpServletRequest request) {
        initialize(model, URL_ASSIGNMENT + "/" + assignmentId, localeResolver, request);
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        String courseName = courseService.getCourseById(assignment.getCourse()).getName();
        model.addAttribute("assignment", assignment);
        model.addAttribute("courseName", courseName);
    }

    private void initModelProfessor(
            Model model, String professorId, LocaleResolver localeResolver, HttpServletRequest request) {
        model.addAttribute("professor", true);
        initialize(model, URL_ASSIGNMENT, localeResolver, request);
        List<Course> courses = courseService.getCoursesByProfessor(professorId);
        model.addAttribute("courses", courses);
        model.addAttribute("newAssignment", Assignment.builder().build());
    }

    private void initModelCourse(
            Model model, String courseId, LocaleResolver localeResolver, HttpServletRequest request) {
        model.addAttribute("professor", true);
        initialize(model, URL_ASSIGNMENT + "/course/" + courseId, localeResolver, request);
        List<Assignment> assignments = assignmentService.getAllForCourse(courseId);
        Course course = courseService.getCourseById(courseId);
        model.addAttribute("assignments", assignments);
        model.addAttribute("course", course);
        model.addAttribute("newAssignment", Assignment.builder().build());
    }
}
