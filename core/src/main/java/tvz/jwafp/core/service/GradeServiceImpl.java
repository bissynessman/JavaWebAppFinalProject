package tvz.jwafp.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tvz.jwafp.core.entity.Grade;
import tvz.jwafp.core.rest.DatabaseApi;
import tvz.jwafp.core.utils.JsonParser;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
    @Autowired
    private DatabaseApi databaseApi;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Grade> getGrades() {
        List<Grade> grades = null;
        try {
            grades = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            databaseApi.getGradesApi(),
                            String.class).getBody(),
                    Grade.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public List<Grade> getGradesByStudent(String studentId) {
        List<Grade> grades = null;
        try {
            grades = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            databaseApi.getGradesApi() + "/student/" + studentId,
                            String.class).getBody(),
                    Grade.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public List<Grade> getGradeByCourse(String courseId) {
        List<Grade> grades = null;
        try {
            grades = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            databaseApi.getGradesApi() + "/course/" + courseId,
                            String.class).getBody(),
                    Grade.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grades;
    }

    @Override
    public void saveGrade(Grade grade) {
        restTemplate.postForObject(databaseApi.getGradesApi(), grade, Grade.class);
    }

    @Override
    public void updateGrade(Grade grade) {
        restTemplate.put(databaseApi.getGradesApi(), grade);
    }

    @Override
    public void deleteGrades(List<String> ids) {
        for (String id : ids)
            restTemplate.delete(databaseApi.getGradesApi() + "/" + id);
    }
}
