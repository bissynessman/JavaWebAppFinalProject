package tvz.jwafp.core.entity;

import lombok.Data;

import java.util.UUID;

@Data
public class Professor {
    private String id;
    private String firstName;
    private String lastName;
    private boolean authorized;

    private String name;

    public Professor() {}

    public String displayProfessor() {
        return this.firstName + " " + this.lastName + " (" + this.id + ")";
    }

    private Professor(final String id, final String firstName, final String lastName, final boolean authorized) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.authorized = authorized;
    }

    public static ProfessorBuilder builder() {
        return new ProfessorBuilder();
    }

    public static class ProfessorBuilder {
        private String id = UUID.randomUUID().toString();
        private String firstName;
        private String lastName;
        private boolean authorized;

        ProfessorBuilder() {}

        public ProfessorBuilder id(final String id) {
            this.id = id;
            return this;
        }

        public ProfessorBuilder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public ProfessorBuilder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public ProfessorBuilder authorized(final boolean authorized) {
            this.authorized = authorized;
            return this;
        }

        public Professor build() {
            return new Professor(this.id, this.firstName, this.lastName, this.authorized);
        }

        public String toString() {
            return "Professor.ProfessorBuilder["
                    + "id=" + this.id
                    + ", firstName=" + this.firstName
                    + ", lastName=" + this.lastName
                    + ", authorized=" + this.authorized
                    + "]";
        }
    }
}
