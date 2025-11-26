package tvz.jwafp.core.service;

import tvz.jwafp.core.entity.Assignment;

import java.util.List;

public interface AssignmentService {
    void saveAssignment(Assignment assignment);
    void updateAssignment(Assignment assignment);
    void deleteAssignments(List<String> ids);
    Assignment getAssignmentById(String id);
    List<Assignment> getAll();
    List<Assignment> getActiveForStudent(String studentId);
    List<Assignment> getAllForCourse(String courseId);
    List<Assignment> getAllForAssignment(String assignmentId);
    void gradeAssignment(Assignment assignment, Integer grade);
}
