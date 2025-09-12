package tvz.jwafp.api.entity;

import tvz.jwafp.api.enums.Major;

public class Student extends BaseEntity {
    private String jmbag;
    private String firstName;
    private String lastName;
    private Major major;

    private String name;

    public Student() {
        super(null);
    }

    public Student(final String id, final String jmbag, final String firstName, final String lastName, final Major major) {
        super(id);
        this.jmbag = jmbag;
        this.firstName = firstName;
        this.lastName = lastName;
        this.major = major;
    }

    public String getId() {
        return super.getId();
    }

    public String getJmbag() {
        return this.jmbag;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Major getMajor() {
        return this.major;
    }

    public String getName() {
        return name;
    }

    public void setId(final String id) {
        super.setId(id);
    }

    public void setJmbag(final String jmbag) {
        this.jmbag = jmbag;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public void setMajor(final Major major) {
        this.major = major;
    }

    public void setName(String name) {
        this.name = name;
    }
}
