package tvz.jwafp.core.helper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tvz.jwafp.core.entity.Course;

@Getter
@Setter
@NoArgsConstructor
public class CourseWrapper {
    private Course course = Course.builder().build();
    private String professorId;
}
