package tvz.jwafp.core.service;

import tvz.jwafp.core.entity.Grade;

import java.util.List;

public interface GradeService {
    List<Grade> getGrades();
    List<Grade> getGradesByStudent(String studentId);
    List<Grade> getGradeByCourse(String courseId);
    void saveGrade(Grade grade);
    void updateGrade(Grade grade);
    void deleteGrades(List<String> ids);
}
