package tvz.jwafp.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tvz.jwafp.api.entity.Student;
import tvz.jwafp.api.repo.StudentRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock Student student;
    @Mock Student student2;

    @Mock StudentRepository studentRepository;

    StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository);
        student = new Student();
        student2 = new Student();
    }

    @Test
    void getAll() {
        when(studentService.getAll()).thenReturn(List.of(student, student2));

        List<Student> students = studentService.getAll();

        assertEquals(2, students.size());
        verify(studentRepository).findAll();
    }
}