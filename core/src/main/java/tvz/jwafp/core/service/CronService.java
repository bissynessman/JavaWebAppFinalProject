package tvz.jwafp.core.service;

import tvz.jwafp.core.entity.Student;

import java.util.List;

public interface CronService {
    List<Student> getAllStudents();
    String getEmailByUserId(String userId);
}
