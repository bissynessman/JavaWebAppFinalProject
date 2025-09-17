package tvz.jwafp.api.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import tvz.jwafp.api.entity.Course;
import tvz.jwafp.api.entity.Grade;
import tvz.jwafp.api.entity.Professor;
import tvz.jwafp.api.entity.Student;
import tvz.jwafp.api.enums.Major;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@ActiveProfiles("junit")
class GradeRepositoryTest {
    @Autowired GradeRepository gradeRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired ProfessorRepository professorRepository;

    Student mockStudent;
    Course mockCourse;
    Professor mockProfessor;
    Grade mockGrade;

    @BeforeEach
    void setUp() {
        mockStudent = new Student("teststudentuuid", "1234567890", "test", "student", Major.ENGINEERING);
        studentRepository.create(mockStudent);
        mockProfessor = new Professor("testprofessoruuid", "test", "professor", true);
        professorRepository.create(mockProfessor);
        mockCourse = new Course("testcourseuuid", "testcourse", 7, mockProfessor.getId());
        courseRepository.create(mockCourse);
        mockGrade = new Grade("testgradeuuid", 5, mockStudent.getId(), mockCourse.getId());
        gradeRepository.create(mockGrade);
    }

    @Test
    void findAll() {
        List<Grade> grades = gradeRepository.findAll();

        assertEquals(1, grades.size());
    }

    @Test
    void findByStudentId() {
        List<Grade> grades = gradeRepository.findByStudentId(mockStudent.getId());

        assertEquals(mockGrade.getId(), grades.getFirst().getId());
    }

    @Test
    void findByCourseId() {
        List<Grade> grades = gradeRepository.findByCourseId(mockCourse.getId());

        assertEquals(mockGrade.getId(), grades.getFirst().getId());
    }
}