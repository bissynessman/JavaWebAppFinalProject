package tvz.jwafp.core.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tvz.jwafp.core.entity.User;

import static tvz.jwafp.core.utils.ModelInitialization.initialize;
import static tvz.jwafp.core.config.Urls.*;

@Controller
@RequestMapping(URL_ADMIN)
@SessionAttributes("userLogin")
public class AdminController {
    private static final String ACTION_AUTHORIZE_PROFESSORS = "authorizeProf";
    private static final String ACTION_EDIT_COURSES = "editCourses";
    private static final String ACTION_EDIT_USERS = "editUsers";

    private final LocaleResolver localeResolver;

    public AdminController(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @GetMapping
    public String showAdminView(Model model, HttpServletRequest request) {
        User userLogin = (User) model.getAttribute("userLogin");
        initModel(model, localeResolver, request);
        return "admin";
    }

    @PostMapping
    public String handleRedirect(Model model,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam("action") String action,
                                 HttpServletRequest request) {
        User userLogin = (User) model.getAttribute("userLogin");
        redirectAttributes.addFlashAttribute("userLogin", userLogin);

        return switch (action) {
            case ACTION_AUTHORIZE_PROFESSORS -> "redirect:" + URL_AUTHORIZATION;
            case ACTION_EDIT_COURSES -> "redirect:" + URL_COURSES;
            case ACTION_EDIT_USERS -> "redirect:" + URL_USERS;
            default -> {
                initModel(model, localeResolver, request);
                yield "admin";
            }
        };
    }

    private void initModel(Model model, LocaleResolver localeResolver, HttpServletRequest request) {
        initialize(model, URL_ADMIN, localeResolver, request);
        model.addAttribute("admin", model.getAttribute("userLogin"));
    }
}
