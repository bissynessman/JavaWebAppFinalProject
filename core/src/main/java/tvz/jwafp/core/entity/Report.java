package tvz.jwafp.core.entity;

import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Data
public class Report {
    private String id;
    private byte[] data;
    private byte[] signature;
    private String fileName;

    private String student;

    public Report() {}

    private Report(String id, Path pathToFile, Path pathToSignature, String fileName, String student) throws IOException {
        this.id = id;
        this.data = Files.readAllBytes(pathToFile);
        this.signature = Files.readAllBytes(pathToSignature);
        this.fileName = fileName;
        this.student = student;
    }

    public static ReportBuilder builder() {
        return new ReportBuilder();
    }

    public static class ReportBuilder {
        private String id = UUID.randomUUID().toString();
        private Path pathToFile;
        private Path pathToSignature;
        private String fileName;
        private String student;

        ReportBuilder() {}

        public ReportBuilder id(final String id) {
            this.id = id;
            return this;
        }

        public ReportBuilder pathToFile(final Path pathToFile) {
            this.pathToFile = pathToFile;
            return this;
        }

        public ReportBuilder pathToSignature(final Path pathToSignature) {
            this.pathToSignature = pathToSignature;
            return this;
        }

        public ReportBuilder fileName(final String fileName) {
            this.fileName = fileName;
            return this;
        }

        public ReportBuilder student(final String student) {
            this.student = student;
            return this;
        }

        public Report build() {
            try {
                return new Report(this.id, this.pathToFile, this.pathToSignature, this.fileName, this.student);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        public String toString() {
            return "Report.ReportBuilder["
                    + "id=" + this.id
                    + ", pathToFile=" + this.pathToFile
                    + ", pathToSignature=" + this.pathToSignature
                    + ", fileName=" + this.fileName
                    + ", student=" + this.student
                    + "]";
        }
    }
}
