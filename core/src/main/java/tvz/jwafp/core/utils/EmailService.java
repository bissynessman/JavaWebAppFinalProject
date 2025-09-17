package tvz.jwafp.core.utils;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import tvz.jwafp.core.helper.Mail;

@Service
public class EmailService {
    private static final String EMAIL_AGENT = "tvz.java.web.app@gmail.com";
    private static final String ENCODING = "UTF-8";

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(Mail mail) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, ENCODING);

        helper.setTo(mail.getTo());
        helper.setFrom(prepareFrom(mail.getFrom()));
        helper.setSubject(mail.getSubject());
        helper.setText(prepareBody(mail.getBody()), true);
        for (Mail.MailAttachment attachment : mail.getAttachments())
            helper.addAttachment(
                    attachment.getFileName(),
                    new ByteArrayDataSource(attachment.getContent().get(), attachment.getContentType())
            );

        mailSender.send(message);
    }

    private String prepareFrom(String from) {
        return from + " <" + EMAIL_AGENT + ">";
    }

    private String prepareBody(String body) {
        return body.replace("\n", "<br/>");
    }
}
