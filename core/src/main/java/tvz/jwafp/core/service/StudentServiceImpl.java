package tvz.jwafp.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tvz.jwafp.core.entity.Student;
import tvz.jwafp.core.rest.DatabaseApi;
import tvz.jwafp.core.utils.JsonParser;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private static final String API_URL = DatabaseApi.STUDENTS_API;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Student> getAll() {
        List<Student> students = null;
        try{
            students = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            API_URL,
                            String.class).getBody(),
                    Student.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public Student getStudentById(String id) {
        Student student = null;
        try{
            student = JsonParser.parseIntoObject(
                    restTemplate.getForEntity(
                            API_URL + "/" + id,
                            String.class).getBody(),
                    Student.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return student;
    }

    @Override
    public void saveStudent(Student student) {
        restTemplate.postForEntity(API_URL, student, void.class);
    }

    @Override
    public void updateStudent(Student student) {
        restTemplate.put(API_URL, student);
    }

    @Override
    public void deleteStudents(List<String> ids) {
        for (String id : ids)
            restTemplate.delete(API_URL + "/" + id);
    }
}
