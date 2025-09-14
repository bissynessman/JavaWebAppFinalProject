package tvz.jwafp.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

import static tvz.jwafp.core.utils.ModelInitialization.initialize;
import static tvz.jwafp.core.config.Urls.*;

@Controller
public class IndexController {
    private static final String DEFAULT_LOCALE = "ENG";

    private final LocaleResolver localeResolver;

    public IndexController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @GetMapping(URL_INDEX)
    public String index(Model model) {
        initModel(model);
        return "index";
    }

    @PostMapping(URL_CHANGE_LANGUAGE)
    public String changeLanguage(@RequestParam("lang") String lang, @RequestParam("redirect") String redirect,
                                 RedirectAttributes redirectAttributes,
                                 HttpServletRequest request, HttpServletResponse response) {
        Locale newLocale = DEFAULT_LOCALE.equalsIgnoreCase(lang)
            ? Locale.ENGLISH
            : new Locale("hr");
        localeResolver.setLocale(request, response, newLocale);
        redirectAttributes.addFlashAttribute("language", lang);
        return "redirect:" + redirect;
    }

    private void initModel(Model model) {
        initialize(model, URL_INDEX);
        model.addAttribute("action", "");
    }
}
