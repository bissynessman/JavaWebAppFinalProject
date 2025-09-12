package tvz.jwafp.core.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class Grade {
    private String id;
    private Integer grade;

    private String student;
    private String course;
    private String courseName;

    public Grade() {}

    private Grade(final String id, final Integer grade, final String student, final String course) {
        this.id = id;
        this.grade = grade;
        this.student = student;
        this.course = course;
    }

    public static GradeBuilder builder() {
        return new GradeBuilder();
    }

    public static class GradeBuilder {
        private String id = UUID.randomUUID().toString();
        private Integer grade;
        private String student;
        private String course;

        GradeBuilder() {}

        public GradeBuilder id(final String id) {
            this.id = id;
            return this;
        }

        public GradeBuilder grade(final Integer grade) {
            this.grade = grade;
            return this;
        }

        public GradeBuilder student(final String student) {
            this.student = student;
            return this;
        }

        public GradeBuilder course(final String course) {
            this.course = course;
            return this;
        }

        public Grade build() {
            return new Grade(this.id, this.grade, this.student, this.course);
        }

        public String toString() {
            return "Grade.GradeBuilder["
                    + "id=" + this.id
                    + ", grade=" + this.grade
                    + ", student=" + this.student
                    + ", course=" + this.course
                    + "]";
        }
    }
}
