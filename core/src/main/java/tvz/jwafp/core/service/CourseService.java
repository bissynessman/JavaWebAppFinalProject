package tvz.jwafp.core.service;

import tvz.jwafp.core.entity.Course;

import java.util.List;

public interface CourseService {
    List<Course> getAll();
    Course getCourseById(String id);
    List<Course> getCoursesByProfessor(String professorId);
    void saveCourse(Course course);
    void updateCourse(Course course);
    void deleteCourses(List<String> id);
}
