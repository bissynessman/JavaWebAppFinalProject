package tvz.jwafp.api.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import tvz.jwafp.api.entity.Student;
import tvz.jwafp.api.enums.Major;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@ActiveProfiles("junit")
class StudentRepositoryTest {
    @Autowired StudentRepository studentRepository;

    Student mockStudent, mockStudent2;

    @BeforeEach
    void setUp() {
        mockStudent = new Student("testuuid", "1234567890", "test", "student", Major.CONSTRUCTION);
        mockStudent2 = new Student("testuuid2", "1234567891", "test2", "student2", Major.SOFTWARE_ENGINEERING);
        studentRepository.create(mockStudent);
        studentRepository.create(mockStudent2);
    }

    @Test
    void findAll() {
        List<Student> students = studentRepository.findAll();

        assertEquals(2, students.size());
    }
}