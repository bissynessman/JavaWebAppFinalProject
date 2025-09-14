package tvz.jwafp.core.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class Course {
    private String id;
    private String name;
    private Integer ects;

    private String professor;

    public Course() {}

    private Course(final String id, final String name, final Integer ects, final String professor) {
        this.id = id;
        this.name = name;
        this.ects = ects;
        this.professor = professor;
    }

    public static CourseBuilder builder() {
        return new CourseBuilder();
    }

    public static class CourseBuilder {
        private String id = UUID.randomUUID().toString();
        private String name;
        private Integer ects;
        private String professor;

        CourseBuilder() {
        }

        public CourseBuilder id(final String id) {
            this.id = id;
            return this;
        }

        public CourseBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public CourseBuilder ects(final Integer ects) {
            this.ects = ects;
            return this;
        }

        public CourseBuilder professor(final String professor) {
            this.professor = professor;
            return this;
        }

        public Course build() {
            return new Course(this.id, this.name, this.ects, this.professor);
        }

        public String toString() {
            return "Course.CourseBuilder["
                    + "id=" + this.id
                    + ", name=" + this.name
                    + ", ects=" + this.ects
                    + ", professor=" + this.professor
                    + "]";
        }
    }
}
