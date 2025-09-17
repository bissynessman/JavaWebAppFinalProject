package tvz.jwafp.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.LocaleResolver;
import tvz.jwafp.core.rest.TimeApi;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
public class ModelInitialization {
    private static final TimeApi TIME_API = new TimeApi();

    public static void initialize(
            Model model, String currentContextPath, LocaleResolver localeResolver, HttpServletRequest request) {
        String[] dateTime = TIME_API.getCurrentTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                .split("T");
        model.addAttribute("currentDate", dateTime[0].trim());
        model.addAttribute("currentTime", dateTime[1].trim());
        Locale locale = localeResolver.resolveLocale(request);
        String lang = locale.getLanguage().equalsIgnoreCase("hr") ? "HRV" : "ENG";
        model.addAttribute("language", lang);
        model.addAttribute("currentContextPath", currentContextPath);
    }
}
