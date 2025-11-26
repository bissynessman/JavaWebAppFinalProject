package tvz.jwafp.core.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.InputStream;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
    private String from;
    private String to;
    private String subject;
    private String body;
    private List<MailAttachment> attachments;

    @Getter
    @AllArgsConstructor
    public static class MailAttachment {
        private final String fileName;
        private final String contentType;
        private final InputStreamSupplier content;

        @FunctionalInterface
        public interface InputStreamSupplier {
            InputStream get() throws Exception;
        }
    }
}
