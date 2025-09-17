package tvz.jwafp.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tvz.jwafp.core.entity.Assignment;
import tvz.jwafp.core.rest.DatabaseApi;
import tvz.jwafp.core.utils.JsonParser;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    private final DatabaseApi databaseApi;

    @Autowired
    private final RestTemplate restTemplate;

    @Override
    public void saveAssignment(Assignment assignment) {
        restTemplate.postForEntity(databaseApi.getAssignmentsApi(), assignment, void.class);
    }

    @Override
    public void updateAssignment(Assignment assignment) {
        restTemplate.put(databaseApi.getAssignmentsApi(), assignment, void.class);
    }

    @Override
    public void deleteAssignments(List<String> ids) {
        for (String id : ids)
            restTemplate.delete(databaseApi.getAssignmentsApi() + "/" + id);
    }

    @Override
    public List<Assignment> getAll() {
        List<Assignment> assignments = null;
        try {
            assignments = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            databaseApi.getAssignmentsApi(),
                            String.class).getBody(),
                    Assignment.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assignments;
    }

    @Override
    public Assignment getAssignmentById(String assignmentId){
        Assignment assignment = null;
        try {
            assignment = JsonParser.parseIntoObject(
                    restTemplate.getForEntity(
                            databaseApi.getAssignmentsApi() + "/" + assignmentId.toString(),
                            String.class).getBody(),
                    Assignment.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assignment;
    }

    @Override
    public List<Assignment> getActiveForStudent(String studentId) {
        List<Assignment> assignments = null;
        try {
            assignments = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            databaseApi.getAssignmentsApi() + "/student/" + studentId,
                            String.class).getBody(),
                    Assignment.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assignments;
    }

    @Override
    public List<Assignment> getAllForCourse(String courseId) {
        List<Assignment> assignments = null;
        try {
            assignments = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            databaseApi.getAssignmentsApi() + "/course/" + courseId,
                            String.class).getBody(),
                    Assignment.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assignments;
    }

    @Override
    public List<Assignment> getAllForAssignment(String assignmentId) {
        List<Assignment> assignments = null;
        try {
            assignments = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            databaseApi.getAssignmentsApi() + "/assignment/" + assignmentId,
                            String.class).getBody(),
                    Assignment.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assignments;
    }

    @Override
    public void gradeAssignment(Assignment assignment, Integer grade) {
        assignment.setGrade(grade);
        updateAssignment(assignment);
    }
}
