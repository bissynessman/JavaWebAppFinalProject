package tvz.jwafp.core.entity;

import lombok.Data;
import tvz.jwafp.core.enums.Major;

import java.util.UUID;

@Data
public class Student {
    private String id;
    private String jmbag;
    private String firstName;
    private String lastName;
    private Major major;

    private String name;

    public Student() {}

    public String displayStudent() {
        return this.firstName + " " + this.lastName + " (" + this.jmbag + ")";
    }

    private Student(final String id, final String jmbag, final String firstName, final String lastName, final Major major) {
        this.id = id;
        this.jmbag = jmbag;
        this.firstName = firstName;
        this.lastName = lastName;
        this.major = major;
    }

    public static StudentBuilder builder() {
        return new StudentBuilder();
    }

    public static class StudentBuilder {
        private String id = UUID.randomUUID().toString();
        private String jmbag;
        private String firstName;
        private String lastName;
        private Major major;

        StudentBuilder() {}

        public StudentBuilder id(final String id) {
            this.id = id;
            return this;
        }

        public StudentBuilder jmbag(final String jmbag) {
            this.jmbag = jmbag;
            return this;
        }
        public StudentBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StudentBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StudentBuilder major(final Major major) {
            this.major = major;
            return this;
        }

        public Student build() {
            return new Student(this.id, this.jmbag, this.firstName, this.lastName, this.major);
        }

        public String toString() {
            return "Student.StudentBuilder["
                    + "id=" + this.id
                    + ", jmbag=" + this.jmbag
                    + ", firstName=" + this.firstName
                    + ", lastName=" + this.lastName
                    + ", major=" + this.major
                    + "]";
        }
    }
}
