package tvz.jwafp.core.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseApi {
    private final String baseApi;
    private final String publicApi;

    public DatabaseApi(@Value("${jwafp.api.base-url-internal}") String baseApi,
                       @Value("${jwafp.api.base-url-public}") String publicApi) {
        this.baseApi = baseApi;
        this.publicApi = publicApi;
    }

    public String getAssignmentsApi() {
        return baseApi + "assignments";
    }

    public String getCoursesApi() {
        return baseApi + "courses";
    }

    public String getGradesApi() {
        return baseApi + "grades";
    }

    public String getProfessorsApi() {
        return baseApi + "professors";
    }

    public String getReportsApi() {
        return baseApi + "reports";
    }

    public String getReportsPublicApi() {
        return publicApi + "reports";
    }

    public String getStudentsApi() {
        return baseApi + "students";
    }

    public String getUsersApi() {
        return baseApi + "users";
    }

    public String getCronApi() {
        return baseApi + "cron";
    }

    public String getAuthApi() {
        return baseApi + "auth";
    }
}
