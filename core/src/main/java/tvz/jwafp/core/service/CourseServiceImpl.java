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
    private static final String API_URL = DatabaseApi.COURSES_API;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public List<Course> getAll() {
        List<Course> courses = null;
        try {
            courses = JsonParser.parseIntoList(
                    restTemplate.getForEntity(
                            API_URL,
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
                            API_URL + "/" + courseId.toString(),
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
                            API_URL + "/professor/" + professorId.toString(),
                            String.class).getBody(),
                    Course.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public void saveCourse(Course course) {
        restTemplate.postForEntity(API_URL, course, void.class);
    }

    @Override
    public void updateCourse(Course course) {
        restTemplate.put(API_URL, course, void.class);
    }

    @Override
    public void deleteCourses(List<String> ids) {
        for (String id : ids)
            restTemplate.delete(API_URL + "/" + id);
    }
}
