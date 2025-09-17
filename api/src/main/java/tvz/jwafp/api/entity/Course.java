package tvz.jwafp.api.entity;

public class Course extends BaseEntity {
    private String name;
    private Integer ects;
    private String professor;

    public Course() {
        super(null);
    }

    public Course(final String id, final String name, final Integer ects, final String professor) {
        super(id);
        this.name = name;
        this.ects = ects;
        this.professor = professor;
    }

    public String getId() {
        return super.getId();
    }

    public String getName() {
        return this.name;
    }

    public Integer getEcts() {
        return this.ects;
    }

    public String getProfessor() {
        return this.professor;
    }

    public void setId(final String id) {
        super.setId(id);
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setEcts(final Integer ects) {
        this.ects = ects;
    }

    public void setProfessor(final String professor) {
        this.professor = professor;
    }
}
