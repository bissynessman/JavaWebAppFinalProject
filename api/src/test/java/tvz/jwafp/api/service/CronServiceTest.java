package tvz.jwafp.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tvz.jwafp.api.entity.Student;
import tvz.jwafp.api.repo.StudentRepository;
import tvz.jwafp.api.repo.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CronServiceTest {
    @Mock Student student;
    @Mock Student student2;

    @Mock StudentRepository studentRepository;
    @Mock UserRepository userRepository;

    CronService cronService;

    @BeforeEach
    void setUp() {
        cronService = new CronService(studentRepository, userRepository);
        student = new Student();
        student2 = new Student();
    }

    @Test
    void getAllStudents() {
        when(cronService.getAllStudents()).thenReturn(List.of(student, student2));

        List<Student> students = cronService.getAllStudents();

        assertEquals(2, students.size());
    }

    @Test
    void getEmailByUserId() {
        when(cronService.getEmailByUserId(any())).thenReturn("test@test.test");

        String result = cronService.getEmailByUserId("123");

        assertEquals("test@test.test", result);
    }
}