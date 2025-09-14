package tvz.jwafp.api.config;

public class Urls {
    public static final String URL_ASSIGNMENT = "/assignments";

    public static final String URL_COURSE = "/courses";

    public static final String URL_GRADE = "/grades";

    public static final String URL_PROFESSOR = "/professors";
    public static final String URL_UNAUTHORIZED = "/unauthorized";
    public static final String URL_AUTHORIZE = "/authorize";

    public static final String URL_REPORT = "/reports";

    public static final String URL_STUDENT = "/students";

    public static final String URL_USER = "/users";

    public static final String ASSIGNMENT_FILTER = "/assignment/{assignmentId}";
    public static final String COURSE_FILTER = "/course/{courseId}";
    public static final String PROFESSOR_FILTER = "/professor/{professorId}";
    public static final String STUDENT_FILTER = "/student/{studentId}";
    public static final String USERNAME_FILTER = "/username/{username}";

    public static final String URL_AUTH = "/auth";
    public static final String URL_SIGNUP = "/signup";
    public static final String URL_REFRESH = "/refresh";

    public static final String URL_CRON = "/cron";
    public static final String URL_STUDENTS = "/students";
    public static final String URL_EMAIL = "/email/{userId}";

    public static final String URL_ID = "/{id}";
    public static final String URL_WILDCARD = "/**";

    public static class Headers {
        public static final String X_API_KEY = "X-API-KEY";
    }
}
