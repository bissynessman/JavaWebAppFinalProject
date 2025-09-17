package tvz.jwafp.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tvz.jwafp.api.entity.Course;
import tvz.jwafp.api.entity.Professor;
import tvz.jwafp.api.repo.CourseRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock Course course;
    @Mock Course course2;
    @Mock Professor professor;

    @Mock CourseRepository courseRepository;

    CourseService courseService;

    @BeforeEach
    void setUp() {
        courseService = new CourseService(courseRepository);
        professor = new Professor("testprofessoruuid", "test", "professor", true);
        course = new Course("testuuid", "testcourse", 4, null);
        course2 = new Course("testuuid2", "testcourse2", 6, professor.getId());
    }

    @Test
    void getById() {
        when(courseRepository.findById(any())).thenReturn(Optional.of(course));

        Course course = courseService.getById("testuuid");

        assertNotNull(course);
        verify(courseRepository).findById(any());
    }

    @Test
    void getAll() {
        when(courseRepository.findAll()).thenReturn(List.of(course, course2));

        List<Course> courses = courseService.getAll();

        assertEquals(2, courses.size());
        verify(courseRepository).findAll();
    }

    @Test
    void getByProfessorId() {
        when(courseRepository.findByProfessorId(any())).thenReturn(List.of(course2));

        List<Course> courses = courseService.getByProfessorId(professor.getId());

        assertEquals(1, courses.size());
        verify(courseRepository).findByProfessorId(any());
    }
}