package tvz.jwafp.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tvz.jwafp.core.entity.Report;
import tvz.jwafp.core.helper.ReportWrapper;
import tvz.jwafp.core.rest.DatabaseApi;
import tvz.jwafp.core.utils.JsonParser;

import java.io.IOException;
import java.nio.file.Files;

@Service
@Primary
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private static final String API_URL = DatabaseApi.REPORTS_API;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String getStudentIdByReportId(String reportId) {
        Report report = null;
        try {
            report = JsonParser.parseIntoObject(
                    restTemplate.getForEntity(
                            API_URL + "/" + reportId,
                            String.class).getBody(),
                    Report.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return report.getStudent();
    }

    @Override
    public void saveReport(ReportWrapper report) {
        restTemplate.postForEntity(API_URL, report.getReport(), void.class);
        try {
            Files.deleteIfExists(report.getDataFile().toPath());
            Files.deleteIfExists(report.getSignatureFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
