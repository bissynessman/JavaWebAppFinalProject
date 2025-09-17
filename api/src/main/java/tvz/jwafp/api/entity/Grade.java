package tvz.jwafp.api.entity;

public class Grade extends BaseEntity {
    private Integer grade;
    private String student;
    private String course;

    public Grade() {
        super(null);
    }

    public Grade(final String id, final Integer grade, final String student, final String course) {
        super(id);
        this.grade = grade;
        this.student = student;
        this.course = course;
    }

    public String getId() {
        return super.getId();
    }

    public Integer getGrade() {
        return this.grade;
    }

    public String getStudent() {
        return this.student;
    }

    public String getCourse() {
        return this.course;
    }

    public void setId(final String id) {
        super.setId(id);
    }

    public void setGrade(final Integer grade) {
        this.grade = grade;
    }

    public void setStudent(final String student) {
        this.student = student;
    }

    public void setCourse(final String course) {
        this.course = course;
    }
}
