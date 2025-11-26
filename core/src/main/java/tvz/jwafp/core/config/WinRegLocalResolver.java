package tvz.jwafp.core.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

import static tvz.jwafp.core.utils.WinRegistry.*;

import java.util.Locale;

@Configuration
public class WinRegLocalResolver implements LocaleResolver {
    @Bean
    public LocaleResolver localeResolver() {
        return new WinRegLocalResolver();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String lang = readRegistryValue(CHANGE_LANGUAGE_REGISTRY_PATH, LANGUAGE_VALUE_NAME);

        if ("HRV".equals(lang))
            return new Locale("hr");
        return Locale.ENGLISH;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        String lang = locale.getLanguage().equals("hr") ? "HRV" : "ENG";
        writeRegistryValue(CHANGE_LANGUAGE_REGISTRY_PATH, LANGUAGE_VALUE_NAME, lang);
    }
}
