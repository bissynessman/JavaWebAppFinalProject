package tvz.jwafp.api.controller;

import org.springframework.web.bind.annotation.*;
import tvz.jwafp.api.entity.Assignment;
import tvz.jwafp.api.entity.Student;
import tvz.jwafp.api.service.AssignmentService;
import tvz.jwafp.api.service.StudentService;

import java.util.List;
import java.util.UUID;

import static tvz.jwafp.api.config.Urls.*;

@RestController
@RequestMapping(URL_ASSIGNMENT)
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final StudentService studentService;

    public AssignmentController(AssignmentService assignmentService, StudentService studentService) {
        this.assignmentService = assignmentService;
        this.studentService = studentService;
    }

    @GetMapping
    public List<Assignment> getAllAssignments() {
        return assignmentService.getAll();
    }

    @GetMapping(URL_ID)
    public Assignment getAssignmentById(@PathVariable String id) {
        return assignmentService.getById(id);
    }

    @GetMapping(STUDENT_FILTER)
    public List<Assignment> getActiveForStudent(@PathVariable String studentId) {
        return assignmentService.getActiveForStudent(studentId);
    }

    @GetMapping(COURSE_FILTER)
    public List<Assignment> getAllForCourse(@PathVariable String courseId) {
        return assignmentService.getAllForCourse(courseId);
    }

    @GetMapping(ASSIGNMENT_FILTER)
    public List<Assignment> getAllForAssignment(@PathVariable String assignmentId) {
        return assignmentService.getAllForAssigment(assignmentId);
    }

    @PostMapping
    public void saveAssignment(@RequestBody Assignment assignment) {
        assignmentService.create(assignment);
        for (String studentId : studentService.getAll().stream().map(Student::getId).toList()) {
            Assignment newAssignment = new Assignment();
            newAssignment.setId(UUID.randomUUID().toString());
            newAssignment.setAssignment(assignment.getId());
            newAssignment.setStudent(studentId);
            assignmentService.create(newAssignment);
        }
    }

    @PutMapping
    public void updateAssignment(@RequestBody Assignment assignment) {
        assignmentService.update(assignment);
    }

    @DeleteMapping(URL_ID)
    public void deleteAssignment(@PathVariable String id) {
        assignmentService.deleteById(id);
    }
}
