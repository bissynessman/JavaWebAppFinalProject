package tvz.ntpr.core.utils;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import tvz.ntpr.core.rest.TimeApi;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZoneOffset;

public class HtmlToPdf {
    private static final TimeApi TIME_API = new TimeApi();

    public static File scrapeHtmlToPdf(String url) throws Exception {
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

        Path studentReportsDirectory = Paths.get("target", "generated", "student_reports");
        String timestamp = String.valueOf(TIME_API.getCurrentTime().toEpochSecond(ZoneOffset.UTC));
        File output = Paths.get(
                studentReportsDirectory.toString(),"student_report(" + timestamp + ").pdf").toFile();
        Files.createDirectories(studentReportsDirectory);
        org.jsoup.nodes.Document htmlDoc = Jsoup.connect(url).get();

        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document pdfDocument = new Document(pdfDoc);

        String title = htmlDoc.title();
        pdfDocument.add(
                new Paragraph(title)
                        .setFont(bold)
                        .setFontSize(18)
                        .setTextAlignment(TextAlignment.CENTER));

        Elements bodyChildren = htmlDoc.body().children();
        for (Element element : bodyChildren)
            convertHtmlElementToPdf(element, pdfDocument, regular, bold);

        pdfDocument.close();

        return output;
    }

    private static void convertHtmlElementToPdf(Element element, Document pdfDocument, PdfFont regular, PdfFont bold) {
        switch (element.tagName()) {
            case "p":
                pdfDocument.add(
                        new Paragraph(element.text()).setFont(regular).setFontSize(12).setMarginTop(5));
                break;
            case "h1":
                pdfDocument.add(
                        new Paragraph(element.text()).setFont(bold).setFontSize(18).setMarginTop(15));
                break;
            case "h2":
                pdfDocument.add(
                        new Paragraph(element.text()).setFont(bold).setFontSize(16).setMarginTop(12));
                break;
            case "table":
                Table table = new Table(element.select("thead th").size());
                Elements rows = element.select("tr");
                for (Element row : rows) {
                    Elements cols = row.select("td, th");
                    for (Element col : cols)
                        table.addCell(new Cell().add(new Paragraph(col.text()).setFontSize(10)));
                }
                pdfDocument.add(table.setMarginTop(10));
                break;
            case "div":
                Elements divChildren = element.children();
                for (Element child : divChildren)
                    convertHtmlElementToPdf(child, pdfDocument, regular, bold);
                break;
            default:
                break;
        }
    }
}
