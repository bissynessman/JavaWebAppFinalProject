package tvz.jwafp.core.service;

import tvz.jwafp.core.entity.Student;

import java.util.List;

public interface StudentService {
    List<Student> getAll();
    Student getStudentById(String id);
    void saveStudent(Student student);
    void updateStudent(Student student);
    void deleteStudents(List<String> ids);
}
