package tvz.jwafp.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tvz.jwafp.api.entity.Course;
import tvz.jwafp.api.entity.Grade;
import tvz.jwafp.api.entity.Student;
import tvz.jwafp.api.enums.Major;
import tvz.jwafp.api.repo.GradeRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {
    @Mock Grade grade;
    @Mock Grade grade2;
    @Mock Student student;
    @Mock Course course;

    @Mock GradeRepository gradeRepository;

    GradeService gradeService;

    @BeforeEach
    void setUp() {
        gradeService = new GradeService(gradeRepository);
        student = new Student("teststudentuuid", "1234567890", "test", "student", Major.COMPUTER_SCIENCE);
        course = new Course("testcoursseuuid", "testcourse", 4, null);
        grade = new Grade("testgradeuuid", 5, student.getId(), "randomuuid");
        grade2 = new Grade("testgradeuuid2", 3, "randomuuid", course.getId());
    }

    @Test
    void getAll() {
        when(gradeService.getAll()).thenReturn(List.of(grade, grade2));

        List<Grade> grades = gradeService.getAll();

        assertEquals(2, grades.size());
    }

    @Test
    void getByStudentId() {
        when(gradeService.getByStudentId(student.getId())).thenReturn(List.of(grade));

        List<Grade> grades = gradeService.getByStudentId(student.getId());

        assertEquals("testgradeuuid", grades.getFirst().getId());
    }

    @Test
    void getByCourseId() {
        when(gradeService.getByCourseId(course.getId())).thenReturn(List.of(grade2));

        List<Grade> grades = gradeService.getByCourseId(course.getId());

        assertEquals("testgradeuuid2", grades.getFirst().getId());
    }
}