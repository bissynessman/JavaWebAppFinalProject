package tvz.jwafp.core.comparator;

import tvz.jwafp.core.entity.Course;

import java.util.Comparator;

public class CourseComparator implements Comparator<Course> {
    @Override
    public int compare(Course c1, Course c2) {
        return c1.getName().compareToIgnoreCase(c2.getName());
    }
}
