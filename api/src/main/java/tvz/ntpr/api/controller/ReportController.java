package tvz.ntpr.api.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import tvz.ntpr.api.entity.Report;
import tvz.ntpr.api.service.ReportService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static tvz.ntpr.api.config.Urls.URL_ID;
import static tvz.ntpr.api.config.Urls.URL_REPORT;

@RestController
@RequestMapping(URL_REPORT)
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(URL_ID)
    public void download(@PathVariable String id, HttpServletResponse response) throws IOException {
        Report report = reportService.findByStudent(id);
        if (report == null || report.getData() == null || report.getSignature() == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }

        String filename = report.getFileName();
        byte[] data = report.getData();
        byte[] signature = report.getSignature();

        response.setContentType("application/zip");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;"
                + "filename=\"" + filename.replace(".pdf", ".zip\""));

        try (ZipOutputStream os = new ZipOutputStream(response.getOutputStream())) {
            ZipEntry dataEntry = new ZipEntry(filename);
            os.putNextEntry(dataEntry);
            os.write(data);
            os.closeEntry();

            ZipEntry signatureEntry = new ZipEntry(filename + ".p7s");
            os.putNextEntry(signatureEntry);
            os.write(signature);
            os.closeEntry();
        }
    }

    @PostMapping
    public void saveReport(@RequestBody Report report) {
        reportService.create(report);
    }
}
