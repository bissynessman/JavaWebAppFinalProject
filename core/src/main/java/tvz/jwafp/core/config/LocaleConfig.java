package tvz.jwafp.core.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.time.Duration;
import java.util.Locale;

@Configuration
public class LocaleConfig {
    private static final String COOKIE_NAME = "LOCALE";
    private static final long COOKIE_MAX_AGE = 24 * 60 * 60;

    private static final String MESSAGE_SOURCE = "classpath:messages/messages";
    private static final String MESSAGE_ENCODING = "UTF-8";

    private static final String LOCALE_PARAMETER_NAME = "lang";

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver(COOKIE_NAME);
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        localeResolver.setCookieMaxAge(Duration.ofSeconds(COOKIE_MAX_AGE));
        return localeResolver;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(MESSAGE_SOURCE);
        messageSource.setDefaultEncoding(MESSAGE_ENCODING);
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName(LOCALE_PARAMETER_NAME);
        return localeChangeInterceptor;
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer(LocaleChangeInterceptor localeChangeInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry interceptorRegistry) {
                interceptorRegistry.addInterceptor(localeChangeInterceptor);
            }
        };
    }
}
