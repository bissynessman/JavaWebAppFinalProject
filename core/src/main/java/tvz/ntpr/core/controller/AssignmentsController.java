package tvz.ntpr.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.core.entity.Assignment;
import tvz.ntpr.core.entity.Course;
import tvz.ntpr.core.entity.User;
import tvz.ntpr.core.enums.Role;
import tvz.ntpr.core.helper.DetectionResult;
import tvz.ntpr.core.helper.Messages;
import tvz.ntpr.core.rest.AiDetectionService;
import tvz.ntpr.core.security.AuthenticationService;
import tvz.ntpr.core.service.AssignmentService;
import tvz.ntpr.core.service.CourseService;

import java.util.List;
import java.util.Map;

import static tvz.ntpr.core.config.Urls.*;
import static tvz.ntpr.core.utils.ModelInitialization.initialize;

@Controller
@RequestMapping(URL_ASSIGNMENT)
@SessionAttributes("userLogin")
public class AssignmentsController {
    private final AssignmentService assignmentService;
    private final CourseService courseService;
    private final AuthenticationService authenticationService;
    private final AiDetectionService aiDetectionService;
    private final Messages messages;

    public AssignmentsController(AssignmentService assignmentService,
                                 CourseService courseService,
                                 AuthenticationService authenticationService,
                                 AiDetectionService aiDetectionService,
                                 Messages messages) {
        this.assignmentService = assignmentService;
        this.courseService = courseService;
        this.authenticationService = authenticationService;
        this.aiDetectionService = aiDetectionService;
        this.messages = messages;
    }

    @GetMapping
    public String assignments(Model model) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        if (!userLogin.getRole().equals(Role.PROFESSOR))
            return "redirect:" + URL_INDEX;
        initModelProfessor(model, userLogin.getUserUuid());
        return "assignments";
    }

    @PostMapping
    public String saveAssignment(Model model, Assignment newAssignment) {
        authenticationService.refresh();
        assignmentService.saveAssignment(newAssignment);
        model.addAttribute("success", messages.getMessage("assignment.save-success"));
        User userLogin = (User) model.getAttribute("userLogin");
        if (!userLogin.getRole().equals(Role.PROFESSOR))
            return "redirect:" + URL_INDEX;
        initModelProfessor(model, userLogin.getUserUuid());
        return "assignments";
    }

    @GetMapping(URL_ASSIGNMENT_COURSE)
    public String assignmentsByCourse(@PathVariable String courseId, Model model) {
        authenticationService.refresh();
        initModelCourse(model, courseId);
        return "assignments-course";
    }

    @PostMapping(URL_ASSIGNMENT_COURSE)
    public String saveAssignmentCourse(@PathVariable String courseId, Model model, Assignment newAssignment) {
        authenticationService.refresh();
        assignmentService.saveAssignment(newAssignment);
        model.addAttribute("success", messages.getMessage("assignment.save-success"));
        initModelCourse(model, courseId);
        return "assignments-course";
    }

    @GetMapping(URL_ASSIGNMENT_ID)
    public String assignment(@RequestParam(required = false, defaultValue = "false") Boolean professor,
                             @PathVariable String assignmentId,
                             Model model) {
        authenticationService.refresh();
        User userLogin = (User) model.getAttribute("userLogin");
        model.addAttribute("professor", professor);
        initModel(model, assignmentId);
        return "assignment";
    }

    @PostMapping(URL_ASSIGNMENT_ID)
    public String updateAssignment(@PathVariable String assignmentId,
                                   @RequestParam(required = false, defaultValue = "false") Boolean professor,
                                   Model model,
                                   Assignment assignment) {
        authenticationService.refresh();
        assignment.setId(assignmentId);
        assignmentService.updateAssignment(assignment);
        model.addAttribute("success", messages.getMessage("assignment.update-success"));
        model.addAttribute("professor", professor);
        initModel(model, assignmentId);
        return "assignment";
    }

    @GetMapping(URL_ASSIGNMENT_DETECT)
    public String detect(@PathVariable String assignmentId, Model model) {
        model.addAttribute("professor", true);
        authenticationService.refresh();
        initModel(model, assignmentId);
        Assignment assignment = (Assignment) model.getAttribute("assignment");
        String content = assignment.getContent();
        if (!content.isEmpty() && !content.isBlank()) {
            Map<String, Object> result = aiDetectionService.check(content);
            model.addAttribute("detectionResult", result.get("classification"));
            model.addAttribute("detectionLevel", result.get("classificationLevel"));
        } else {
            model.addAttribute("detectionResult", messages.getMessage("detection.result-uncertain"));
            model.addAttribute("detectionLevel", "yellow");
        }
        return "assignment";
    }

    private void initModel(Model model, String assignmentId) {
        initialize(model, URL_ASSIGNMENT + "/" + assignmentId);
        Assignment assignment = assignmentService.getAssignmentById(assignmentId);
        String courseName = courseService.getCourseById(assignment.getCourse()).getName();
        model.addAttribute("assignment", assignment);
        model.addAttribute("courseName", courseName);
    }

    private void initModelProfessor(Model model, String professorId) {
        model.addAttribute("professor", true);
        initialize(model, URL_ASSIGNMENT);
        List<Course> courses = courseService.getCoursesByProfessor(professorId);
        model.addAttribute("courses", courses);
        model.addAttribute("newAssignment", Assignment.builder().build());
    }

    private void initModelCourse(Model model, String courseId) {
        model.addAttribute("professor", true);
        initialize(model, URL_ASSIGNMENT + "/course/" + courseId);
        List<Assignment> assignments = assignmentService.getAllForCourse(courseId);
        Course course = courseService.getCourseById(courseId);
        model.addAttribute("assignments", assignments);
        model.addAttribute("course", course);
        model.addAttribute("newAssignment", Assignment.builder().build());
    }
}
