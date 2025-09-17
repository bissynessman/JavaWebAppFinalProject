package tvz.jwafp.core.service;

import tvz.jwafp.core.helper.ReportWrapper;

public interface ReportService {
    void saveReport(ReportWrapper reportWrapper);
    String getStudentIdByReportId(String reportId);
}
