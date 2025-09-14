package tvz.jwafp.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tvz.jwafp.core.entity.Course;
import tvz.jwafp.core.rest.DatabaseApi;
import tvz.jwafp.core.utils.JsonParser;

import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    @Autowired
    private DatabaseApi databaseApi;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Course> getAll() {
        List<Course> courses = null;
        try {
            courses = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            databaseApi.getCoursesApi(),
                            String.class).getBody(),
                    Course.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public Course getCourseById(String courseId){
        Course course = null;
        try {
            course = JsonParser.parseIntoObject(
                    restTemplate.getForEntity(
                            databaseApi.getCoursesApi() + "/" + courseId.toString(),
                            String.class).getBody(),
                    Course.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return course;
    }

    @Override
    public List<Course> getCoursesByProfessor(String professorId){
        List<Course> courses = null;
        try {
            courses = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            databaseApi.getCoursesApi() + "/professor/" + professorId.toString(),
                            String.class).getBody(),
                    Course.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public void saveCourse(Course course) {
        restTemplate.postForEntity(databaseApi.getCoursesApi(), course, void.class);
    }

    @Override
    public void updateCourse(Course course) {
        restTemplate.put(databaseApi.getCoursesApi(), course, void.class);
    }

    @Override
    public void deleteCourses(List<String> ids) {
        for (String id : ids)
            restTemplate.delete(databaseApi.getCoursesApi() + "/" + id);
    }
}
