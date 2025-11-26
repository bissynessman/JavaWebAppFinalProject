package tvz.jwafp.api.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import tvz.jwafp.api.entity.Course;
import tvz.jwafp.api.entity.Professor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@ActiveProfiles("junit")
class CourseRepositoryTest {
    @Autowired CourseRepository courseRepository;
    @Autowired ProfessorRepository professorRepository;

    Course mockCourse, mockCourse2;
    Professor mockProfessor, mockProfessor2;

    @BeforeEach
    void setUp() {
        mockProfessor = new Professor("testprofessoruuid", "test", "professor", true);
        mockProfessor2 = new Professor("testprofessoruuid2", "test", "professor2", true);
        professorRepository.create(mockProfessor);
        professorRepository.create(mockProfessor2);
        mockCourse = new Course("testuuid", "testcourse", 4, mockProfessor.getId());
        mockCourse2 = new Course("testuuid2", "testcourse2", 6, mockProfessor2.getId());
        courseRepository.create(mockCourse);
        courseRepository.create(mockCourse2);
    }

    @Test
    void findAll() {
        List<Course> courses = courseRepository.findAll();

        assertEquals(2, courses.size());
    }

    @Test
    void findByProfessorId() {
        List<Course> courses = courseRepository.findByProfessorId(mockProfessor2.getId());

        assertEquals(mockCourse2.getId(), courses.getFirst().getId());
    }
}