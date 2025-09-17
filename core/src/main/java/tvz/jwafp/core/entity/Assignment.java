package tvz.jwafp.core.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class Assignment {
    private String id;
    private String assignment;
    private String title;
    private String task;
    private String content;
    private Integer grade;

    private String course;
    private String student;

    private String studentName;

    public Assignment() {}

    private Assignment(final String id,
                       final String assignment,
                       final String title,
                       final String task,
                       final String content,
                       final Integer grade,
                       final String course,
                       final String student) {
        this.id = id;
        this.assignment = assignment;
        this.title = title;
        this.task = task;
        this.content = content;
        this.grade = grade;
        this.course = course;
        this.student = student;
    }

    public static AssignmentBuilder builder() {
        return new AssignmentBuilder();
    }

    public static class AssignmentBuilder {
        private String id = UUID.randomUUID().toString();
        private String assignment;
        private String title;
        private String task;
        private String content;
        private Integer grade;
        private String course;
        private String student;

        AssignmentBuilder() {}

        public AssignmentBuilder id(final String id) {
            this.id = id;
            return this;
        }

        public AssignmentBuilder assignment(final String assignment) {
            this.assignment = assignment;
            return this;
        }

        public AssignmentBuilder title(final String title) {
            this.title = title;
            return this;
        }

        public AssignmentBuilder task(final String task) {
            this.task = task;
            return this;
        }

        public AssignmentBuilder content(final String content) {
            this.content = content;
            return this;
        }

        public AssignmentBuilder grade(final Integer grade) {
            this.grade = grade;
            return this;
        }

        public AssignmentBuilder course(final String course) {
            this.course = course;
            return this;
        }

        public AssignmentBuilder student(final String student) {
            this.student = student;
            return this;
        }

        public Assignment build() {
            return new Assignment(this.id,
                                  this.assignment,
                                  this.title,
                                  this.task,
                                  this.content,
                                  this.grade,
                                  this.course,
                                  this.student);
        }

        public String toString() {
            return "Assignment["
                    + "id=" + this.id
                    + ", assignment=" + this.assignment
                    + ", title=" + this.title
                    + ", task=" + this.task
                    + ", content=" + this.content
                    + ", grade=" + this.grade
                    + ", course=" + this.course
                    + ", student=" + this.student
                    + "]";
        }
    }
}
