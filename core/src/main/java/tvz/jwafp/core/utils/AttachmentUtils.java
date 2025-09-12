package tvz.jwafp.core.utils;

import org.springframework.web.multipart.MultipartFile;
import tvz.jwafp.core.helper.Mail;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AttachmentUtils {
    public static List<Mail.MailAttachment> fromMultipartFiles(MultipartFile[] files) {
        if (files == null || files.length == 0) return new ArrayList<>();

        return Arrays.stream(files)
                .filter(file -> !file.isEmpty())
                .map(file -> new Mail.MailAttachment(
                        file.getOriginalFilename(),
                        file.getContentType(),
                        file::getInputStream
                ))
                .toList();
    }

    public static List<Mail.MailAttachment> fromFile(File file) {
        return List.of(new Mail.MailAttachment(
                file.getName(),
                "application/octet-stream",
                () -> new FileInputStream(file)
        ));
    }
}
