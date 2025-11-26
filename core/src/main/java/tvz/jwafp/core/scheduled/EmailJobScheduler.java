package tvz.jwafp.core.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tvz.jwafp.core.config.AppProperties;
import tvz.jwafp.core.helper.Mail;
import tvz.jwafp.core.helper.Messages;
import tvz.jwafp.core.entity.Student;
import tvz.jwafp.core.security.AuthenticationService;
import tvz.jwafp.core.service.CronService;
import tvz.jwafp.core.utils.AttachmentUtils;
import tvz.jwafp.core.utils.EmailService;

import java.io.File;
import java.util.List;

import static tvz.jwafp.core.config.Urls.URL_STUDENT;
import static tvz.jwafp.core.utils.HtmlToPdf.scrapeHtmlToPdf;

@Service
public class EmailJobScheduler {
    private static final String FROM = "Automatic service";

    private final EmailService emailService;
    private final CronService cronService;
    private final Messages messages;
    private final AuthenticationService authenticationService;
    private final AppProperties appProperties;

    public EmailJobScheduler(EmailService emailService,
                             CronService cronService,
                             Messages messages,
                             AuthenticationService authenticationService,
                             AppProperties appProperties) {
        this.emailService = emailService;
        this.cronService = cronService;
        this.messages = messages;
        this.authenticationService = authenticationService;
        this.appProperties = appProperties;
    }

//    @Scheduled(cron = "0 * * * * ?")
    @Scheduled(cron = "#{appProperties.emailCron}")
    public void sendStudentReports() {
        List<Student> students = cronService.getAllStudents();

        authenticationService.cron();
        for (Student student : students) {
            try {
                File pdfData = scrapeHtmlToPdf(appProperties.getApplicationUrl() + URL_STUDENT + "/" + student.getId());
                String studentEmail = cronService.getEmailByUserId(student.getId());
                Mail mail = new Mail(
                        FROM,
                        studentEmail,
                        messages.getMessage("student-report.subject"),
                        messages.getMessage("student-report.text"),
                        AttachmentUtils.fromFile(pdfData)
                );

                emailService.sendEmail(mail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
