package tvz.jwafp.api.entity;

public class Assignment extends BaseEntity {
    private String assignment;
    private String title;
    private String task;
    private String content;
    private Integer grade;

    private String course;
    private String student;

    private String studentName;

    public Assignment() {
        super(null);
    }

    public Assignment(final String id,
                      final String assignment,
                      final String title,
                      final String task,
                      final String content,
                      final Integer grade,
                      final String course,
                      final String student) {
        super(id);
        this.assignment = assignment;
        this.title = title;
        this.task = task;
        this.content = content;
        this.grade = grade;
        this.course = course;
        this.student = student;
    }

    public String getId() {
        return super.getId();
    }

    public String getAssignment() {
        return assignment;
    }

    public String getTitle() {
        return title;
    }

    public String getTask() {
        return task;
    }

    public String getContent() {
        return content;
    }

    public Integer getGrade() {
        return grade;
    }

    public String getCourse() {
        return course;
    }

    public String getStudent() {
        return student;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setId(String id) {
        super.setId(id);
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public void setStudent(String student) {
        this.student = student;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
